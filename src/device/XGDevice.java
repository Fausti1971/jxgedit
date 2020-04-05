package device;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import gui.XGContext;
import gui.XGDeviceDetector;
import gui.XGFrame;
import gui.XGPathSelector;
import gui.XGSpinner;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import module.XGModule;
import msg.XGMessageBulkDump;
import msg.XGMessageDumpRequest;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import parm.XGTranslationMap;
import tag.XGTagableSet;
import value.ChangeableContent;
import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, Configurable, XGTreeNode, XGContext, XGWindowSource, XGMessenger
{	private static Logger log = Logger.getAnonymousLogger();
	private static Set<XGDevice> DEVICES = new LinkedHashSet<>();
	private static XGDevice DEF = new XGDevice();
	static
	{	ACTIONS.add(ACTION_CONFIGURE);
		ACTIONS.add(ACTION_REMOVE);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

	public static Set<XGDevice> getDevices()
	{	return DEVICES;
	}

	public static XGDevice getDefaultDevice()
	{	return DEF;
	}

	public static void init()
	{	for(XMLNode n : JXG.getJXG().getConfig().getChildNodes())
		{	if(n.getTag().equals(TAG_DEVICE))
			{	XGDevice d = new XGDevice(n);
				if(DEVICES.add(d)) d.reloadTree();
			}
		}
		log.info(DEVICES.size() + " devices initialized");
	}

/***************************************************************************************************************************/

	private final ChangeableContent<Integer> sysex = new ChangeableContent<Integer>()
	{	@Override public Integer get()
		{	return getSysexID();
		}
		@Override public void set(Integer s)
		{	setSysexID(s);
		}
	};

	private final ChangeableContent<Path> defaultDumpFolder = new ChangeableContent<Path>()
	{	@Override public Path get()
		{	return Paths.get(config.getChildNodeTextContent(TAG_DEFAULTDUMPFOLDER.toString(), JXG.HOMEPATH.toString()));
		}
		@Override public void set(Path s)
		{	config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).setTextContent(s.toString());
		}
	};

	private ChangeableContent<String> name = new ChangeableContent<String>()
	{	@Override public String get()
		{	return config.getChildNodeTextContent(TAG_DEVICE_NAME, DEF_DEVNAME);
		}
		@Override public void set(String s)
		{	config.getChildNodeOrNew(TAG_DEVICE_NAME).setTextContent(s);
		}
	};
	private XGTree tree;
	private Color color;
	private boolean isSelected = false;
	private XMLNode config;
	private final XGAddressableSet<XGMessageBulkDump> data = new XGAddressableSet<XGMessageBulkDump>();
	private final XGTagableSet<XGTranslationMap> translations = new XGTagableSet<XGTranslationMap>();
	private final XGAddressableSet<XGModule> modules;
	private int info1, info2;
	private final Queue<XGSysexFile> files = new LinkedList<>();
	private final XGSysexFile defaultSyx;
	private XGMidi midi;
	private int sysexID;
	private XGWindow childWindow;
	private final XMLNode templates;

	private XGDevice()	//DefaultDevice (XG)
	{	this.config = null;
		this.midi = null;
		this.name = new ChangeableContent<String>()
		{	@Override public String get()
			{	return "XG";
			}
			@Override public void set(String s)
			{
			}
		};
		this.info1 = 1;
		this.info2 = 1;
		this.modules = XGModule.init(this);
		XMLNode x = null;
		try
		{	x = XMLNode.parse(this.getResourceFile(XML_TEMPLATE));
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
		}
		this.templates = x;
		this.defaultSyx = null;
		log.info("device initialized: " + this);
	}

	public XGDevice(XMLNode cfg)
	{	this.config = cfg;
		if(this.config == null)
		{	this.config = new XMLNode(TAG_DEVICE, null);
			this.midi = new XGMidi(this);
			this.configure();
		}
		this.sysex.set(this.config.parseChildNodeIntegerContentOrNew(TAG_SYSEXID, DEF_SYSEXID));
		this.midi = new XGMidi(this);
		this.name.set(this.config.getChildNodeTextContent(TAG_DEVICE_NAME, DEF_DEVNAME));
		this.modules = XGModule.init(this);
		this.setColor(new Color(this.config.parseChildNodeIntegerContent(TAG_COLOR, DEF_DEVCOLOR)));
		XMLNode x = null;
		try
		{	x = XMLNode.parse(this.getResourceFile(XML_TEMPLATE));
			System.out.println(x);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
		}
		this.templates = x;

		this.defaultDumpFolder.set(Paths.get(this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).getTextContent()));
		this.defaultSyx = new XGSysexFile(this, this.defaultDumpFolder.get().resolve("default.syx").toString());
		this.defaultSyx.load(this);
		log.info("device initialized: " + this);
	}

	public File getResourceFile(String fName) throws FileNotFoundException
	{	Path extPath = this.getResourcePath();
		File extFile = extPath.resolve(fName).toFile();
		File intFile = RSCPATH.resolve(this.name.get()).resolve(fName).toFile();
		if(extFile.canRead()) return extFile;
		if(intFile.canRead()) return intFile;
		throw new FileNotFoundException("can't read files, neither: " + extFile + " nor: " + intFile);
	}

	public Path getResourcePath()
	{	return HOMEPATH.resolve(this.name.get());
	}

	public Color getColor()
	{	return color;
	}

	public void setColor(Color color)
	{	this.color = color;
	}

	public XGSysexFile getLastDumpFile()
	{	return this.files.peek();
	}

	public XGAddressableSet<XGModule> getModules()
	{	return this.modules;
	}

	public XMLNode getTemplates()
	{	return templates;
	}

	public XGTagableSet<XGTranslationMap> getTranslations()
	{	return this.translations;
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		this.config.getChildNodeOrNew(TAG_SYSEXID).setTextContent(this.sysexID);
	}

	public XGAddressableSet<XGMessageBulkDump> getData()
	{	return this.data;
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageDumpRequest(this, this.midi, XGAddressConstants.XGMODELNAMEADRESS);
			try
			{	XGResponse r = this.midi.request(m);
				String s = r.getString(r.getBaseOffset(), r.getBaseOffset() + r.getBulkSize());
				this.name.set(s.trim());
			}
			catch(TimeoutException e)
			{	JOptionPane.showMessageDialog(this.getChildWindow(), e.getMessage());
			}
		}
		catch(InvalidXGAddressException | InvalidMidiDataException e)
		{	e.printStackTrace();
		}
	}

	public XGMidi getMidi()
	{	return this.midi;
	}

	public void configure()
	{	new XGWindow(this, XGWindow.getRootWindow(), true, this.toString());
	}

	@Override public int hashCode()
	{	return HASH * this.midi.hashCode() + HASH * this.sysexID;
	}

	@Override public boolean equals(Object o)
	{	if(this == o) return true;
		if(!(o instanceof XGDevice)) return false;
		return this.hashCode() == o.hashCode();
	}

	@Override public String toString()
	{	return this.name.get() + " (" + this.sysexID + ")";
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public TreeNode getParent()
	{	return JXG.getJXG();
	}

	@Override public boolean getAllowsChildren()
	{	return true;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.modules);
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setSelected(boolean s)
	{	this.isSelected = s;
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	switch(e.getActionCommand())
		{	case ACTION_CONFIGURE:
				this.configure(); break;
			case ACTION_REMOVE:
				break;
			case ACTION_LOADFILE:
				break;
			case ACTION_SAVEFILE:
				break;
			default:
				break;
		}
	}

	@Override public XGWindow getChildWindow()
	{	return this.childWindow;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.childWindow = win;
	}

	@Override public void windowOpened(WindowEvent e)
	{	XGWindowSource.super.windowOpened(e);
		this.setSelected(true);
	}

	@Override public void windowClosed(WindowEvent e)
	{	XGWindowSource.super.windowClosed(e);
		this.setSelected(false);
	}

	@Override public JComponent getChildWindowContent()
	{	return this.getConfigComponent();
	}

	public JComponent getConfigComponent()
	{	XGFrame root = new XGFrame("device");

		root.addGB(this.getMidi().getConfigComponent(), 0, 0);

		XGFrame frame = new XGFrame("sysexID");
		frame.addGB(new XGSpinner(this.sysex, 0, 15, 1), 0, 0);
		root.addGB(frame, 0, 1);

		frame = new XGFrame("device name");
		frame.addGB(new XGDeviceDetector(this.name, this), 0, 0);
		root.addGB(frame, 0, 2);

		frame = new XGFrame("default dump folder");
		frame.addGB(new XGPathSelector(this.defaultDumpFolder), 0, 0);
		root.addGB(frame, 0, 3);

		return root;
	}

	@Override public void setTreeComponent(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTreeComponent()
	{	return this.tree;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public XGDevice getDevice()
	{	return this;
	}

	@Override public String getMessengerName()
	{	return this.name.get();
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{	if(msg instanceof XGMessageBulkDump) this.data.add((XGMessageBulkDump)msg);
		//TODO: XGMessageParameterChange
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{	if(req instanceof XGMessageDumpRequest) return this.data.get(req.getAddress());
		//TODO: XGMessageParameterRequest
		return null;
	}
}