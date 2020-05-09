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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import gui.XGComponent;
import gui.XGContext;
import gui.XGDeviceDetector;
import gui.XGFrame;
import gui.XGPathSelector;
import gui.XGSpinner;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import module.XGModule;
import msg.XGMessageDumpRequest;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGTable;
import tag.XGTagableSet;
import value.ChangeableContent;
import value.XGValue;
import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, Configurable, XGTreeNode, XGContext, XGWindowSource, XGMessenger
{
	private static Set<XGDevice> DEVICES = new LinkedHashSet<>();
//	private static XGDevice DEF = new XGDevice();
	static
	{	ACTIONS.add(ACTION_CONFIGURE);
		ACTIONS.add(ACTION_REMOVE);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

	public static Set<XGDevice> getDevices()
	{	return DEVICES;
	}

//	public static XGDevice getDefaultDevice()
//	{	return DEF;
//	}

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
	{	@Override public Integer getContent()
		{	return getSysexID();
		}
		@Override public boolean setContent(Integer i)
		{	int old = getSysexID();
			setSysexID(i);
			return old != i;
		}
	};

	private final ChangeableContent<Path> defaultDumpFolder = new ChangeableContent<Path>()
	{	@Override public Path getContent()
		{	return Paths.get(config.getStringAttribute(ATTR_DEFAULTDUMPFOLDER.toString(), JXG.HOMEPATH.toString()));
		}
		@Override public boolean setContent(Path s)
		{	config.setStringAttribute(ATTR_DEFAULTDUMPFOLDER, s.toString());
			return true;
		}
	};

	private ChangeableContent<String> name = new ChangeableContent<String>()
	{	@Override public String getContent()
		{	return config.getStringAttribute(ATTR_NAME, DEF_DEVNAME);
		}
		@Override public boolean setContent(String s)
		{	config.setStringAttribute(ATTR_NAME, s);
			return true;
		}
	};
	private XGTree tree;
	private Color color;
	private boolean isSelected = false;
	private XMLNode config;
//	private final XGAddressableSet<XGMessageBulkDump> data = new XGAddressableSet<XGMessageBulkDump>();
	private final Map<String, XGTable> tables = new HashMap<>();
	private final XGTagableSet<XGParameter> parameters = new XGTagableSet<>();
	private final Map<Integer, Map<Integer, XGParameter>> parameterSets = new HashMap<>();//Map<programNr, parameterIndex<Parameter>> //progNr wird bei parameterMaster erfragt
	private final XGAddressableSet<XGBulkDump> bulks = new XGAddressableSet<>();//wird von XGOpcode.init() mit initialisiert
	private final XGAddressableSet<XGModule> modules = new XGAddressableSet<>();//wird von XGOpcode.init() mit initialisiert
	private final XGAddressableSet<XGOpcode> opcodes = new XGAddressableSet<>();
	private final XGAddressableSet<XGValue> values = new XGAddressableSet<>();
	private final XGAddressableSet<XGTemplate> templates = new XGAddressableSet<>();

	private int info1, info2;
	private final Queue<XGSysexFile> files = new LinkedList<>();
	private final XGSysexFile defaultSyx;
	private XGMidi midi;
	private int sysexID;
	private XGWindow childWindow;

	public XGDevice(XMLNode cfg)
	{	this.config = cfg;
		if(this.config == null)
		{	this.config = new XMLNode(TAG_DEVICE, null);
			this.midi = new XGMidi(this);
			this.configure();
		}
		this.sysex.setContent(this.config.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID));
		this.midi = new XGMidi(this);
		this.name.setContent(this.config.getStringAttribute(ATTR_NAME, DEF_DEVNAME));
		this.setColor(new Color(this.config.getIntegerAttribute(ATTR_COLOR, DEF_DEVCOLOR)));
		this.defaultDumpFolder.setContent(Paths.get(this.config.getStringAttribute(ATTR_DEFAULTDUMPFOLDER, JXG.HOMEPATH.toString())));

		XGTemplate.init(this);
		XGTable.init(this);
		XGParameter.init(this);
		XGOpcode.init(this);
		XGValue.init(this);

		this.defaultSyx = new XGSysexFile(this, this.defaultDumpFolder.getContent().resolve("default.syx").toString());
		this.defaultSyx.load(this);
		log.info("device initialized: " + this);
	}

	public File getResourceFile(String fName) throws FileNotFoundException
	{	Path extPath = this.getResourcePath();
		File extFile = extPath.resolve(fName).toFile();
		File intFile = RSCPATH.resolve(this.name.getContent()).resolve(fName).toFile();
		if(extFile.canRead()) return extFile;
		if(intFile.canRead()) return intFile;
		throw new FileNotFoundException("can't read files, neither: " + extFile + " nor: " + intFile);//TODO: interner Pfad wird außerhalb von eclipse nicht aufgelöst!
	}

	public Path getResourcePath()
	{	return HOMEPATH.resolve(this.name.getContent());
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

	public XGTagableSet<XGParameter> getParameters()
	{	return this.parameters;
	}

	public Map<Integer, Map<Integer, XGParameter>> getParameterSets()
	{	return parameterSets;
	}

	public XGAddressableSet<XGModule> getModules()
	{	return this.modules;
	}

	public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}

	public XGAddressableSet<XGOpcode> getOpcodes()
	{	return this.opcodes;
	}

	public XGAddressableSet<XGValue> getValues()
	{	return this.values;
	}

	public XGAddressableSet<XGTemplate> getTemplates()
	{	return this.templates;
	}

	public Map<String, XGTable> getTables()
	{	return this.tables;
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		this.config.setIntegerAttribute(ATTR_SYSEXID, this.sysexID);
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageDumpRequest(this, this.midi, XGAddressConstants.XGMODELNAMEADRESS);
			try
			{	XGResponse r = this.midi.request(m);
				String s = r.getString(r.getBaseOffset(), r.getBaseOffset() + r.getBulkSize());
				this.name.setContent(s.trim());
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
	{	return this.name.getContent() + " (" + this.sysexID + ")";
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
	{	return Collections.enumeration(this.getModules());
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

		XGComponent c = this.getMidi().getConfigComponent();
		root.addGB(c.getJComponent(), 0, 0);

		c = new XGSpinner("sysexID", this.sysex, 0, 15, 1);
		root.addGB(c.getJComponent(), 0, 1, 1, 1);

		c = new XGDeviceDetector("device name", this.name, this);
		root.addGB(c.getJComponent(), 0, 2);

		c = new XGPathSelector("default dump folder", this.defaultDumpFolder);
		root.addGB(c.getJComponent(), 0, 3);

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
	{	return this.name.getContent();
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{
		int end = msg.getBulkSize() + msg.getBaseOffset(),
			offset = msg.getAddress().getLo().getValue(),
			hi = msg.getAddress().getHi().getValue(),
			mid = msg.getAddress().getMid().getValue(),
			size = 1;

		for(int i = msg.getBaseOffset(); i < end;)
		{	XGAddress adr = new XGAddress(hi, mid, offset);
			XGValue v = this.values.get(adr);
			if(v != null)
			{	v.setContent(v.decodeBytes(msg));
				size = v.getOpcode().getAddress().getLo().getSize();
			}
			else
			{	log.info("value not found: " + adr);
				size = 1;
			}
			offset += size;
			i += size;
		}
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{	//TODO:
		return null;
	}
}