package device;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
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
import msg.XGMessageDumpRequest;
import msg.XGRequest;
import obj.XGType;
import opcode.XGOpcode;
import parm.XGParameter;
import parm.XGTranslationMap;
import tag.XGTagableAdressableSet;
import tag.XGTagableSet;
import value.ObservableValue;
import value.XGValueStore;
import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, Configurable, XGTreeNode, XGContext, XGWindowSource
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

	private final ObservableValue<Integer> sysex = new ObservableValue<Integer>()
	{	@Override public Integer get()
		{	return getSysexID();
		}
		@Override public void set(Integer s)
		{	setSysexID(s);
		}
	};

	private final ObservableValue<Path> defaultDumpFolder = new ObservableValue<Path>()
	{	@Override public Path get()
		{	return getDefDumpPath();
		}
		@Override public void set(Path s)
		{	setDefDumpPath(s);
		}
	};

	private ObservableValue<String> name = new ObservableValue<String>()
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
//	private final XMLNode template;
	private XMLNode config;
	private final XGValueStore values;
//	private final XGTagableSet<XGModule> modules;	//modules - bulks - opcodes
	private final XGTagableAdressableSet<XGOpcode> opcodes;
	private final XGTagableSet<XGType> types;
	private final XGTagableSet<XGTranslationMap> translations;
	private final XGTagableSet<XGParameter> parameters;
	private int info1, info2;
	private final Queue<XGSysexFile> files = new LinkedList<>();
	private final XGSysexFile defaultSyx;
	private XGMidi midi;
	private int sysexID;
	private XGWindow childWindow;

	private XGDevice()	//DefaultDevice (XG)
	{	this.config = null;
		this.midi = null;
		this.name = new ObservableValue<String>()
		{	@Override public String get()
			{	return "XG";
			}
			@Override public void set(String s)
			{
			}
		};
		this.info1 = 1;
		this.info2 = 1;
		this.values = null;
		this.opcodes = XGOpcode.init(this);
		this.types = XGType.init(this);
		this.translations = XGTranslationMap.init(this);
		this.parameters = XGParameter.init(this);
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
		this.setColor(new Color(this.config.parseChildNodeIntegerContent(TAG_COLOR, DEF_DEVCOLOR)));
		this.defaultDumpFolder.set(Paths.get(this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).getTextContent()));
		this.values = new XGValueStore(this);
		this.opcodes = XGOpcode.init(this);
		this.types = XGType.init(this);
		this.translations = XGTranslationMap.init(this);
		this.parameters = XGParameter.init(this);
		this.defaultSyx = new XGSysexFile(this, this.defaultDumpFolder.get().resolve("default.syx").toString());
		this.defaultSyx.load(this.values);
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

	public XGValueStore getValues()
	{	return values;
	}

	public XGTagableAdressableSet<XGOpcode> getOpcodes()
	{	return XGDevice.getDefaultDevice().opcodes;
	}

	public XGTagableSet<XGType> getTypes()
	{	if(this.types == null) return XGDevice.getDefaultDevice().getTypes();
		else return this.types;
	}

	public XGType getType(XGAddress adr)
	{	for(XGType t : this.getTypes()) if(t.include(adr)) return t;
		for(XGType t : XGDevice.getDefaultDevice().getTypes()) if(t.include(adr)) return new XGType(this, t);
		return new XGType(this, adr);
	}

	public XGTagableSet<XGTranslationMap> getTranslations()
	{	return translations;
	}

	public XGTagableSet<XGParameter> getParameters()
	{	return XGDevice.getDefaultDevice().parameters;
	}

	public Path getDefDumpPath()
	{	return Paths.get(this.getConfig().getChildNodeTextContent(TAG_DEFAULTDUMPFOLDER.toString(), JXG.HOMEPATH.toString()));
	}

	public void setDefDumpPath(Path defDumpPath)
	{	this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).setTextContent(defDumpPath.toString());
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		this.config.getChildNodeOrNew(TAG_SYSEXID).setTextContent(this.sysexID);
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageDumpRequest(this.midi, XGAddressConstants.XGMODELNAMEADRESS);
			m.setDestination(this.values);
			this.midi.request(m);
			//TODO: wait for response...
			this.name.set(this.values.get(XGAddressConstants.XGMODELNAMEADRESS).toString().strip());
			this.info1 = (int)this.values.get(XGAddressConstants.XGMODELINFO1ADDRESS).getContent();
			this.info2 = (int)this.values.get(XGAddressConstants.XGMODELINFO2ADDRESS).getContent();
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
	{	return this.name.get();
	}

	@Override public String getNodeText()
	{	return(this.name + "/" + this.sysexID);
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
	{	return this.values.getTypes().enumeration();
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
	{	XGFrame root = new XGFrame("device", BoxLayout.Y_AXIS);

		root.add(this.getMidi().getConfigComponent());

		XGFrame frame = new XGFrame("sysexID", 0);
		frame.add(new XGSpinner(this.sysex, 0, 15, 1));
		root.add(frame);

		frame = new XGFrame("default dump folder", 0);
		frame.add(new XGPathSelector(this.defaultDumpFolder));
		root.add(frame);

		frame = new XGFrame("device name", 0);
		frame.add(new XGDeviceDetector(this.name));
		root.add(frame);
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
}