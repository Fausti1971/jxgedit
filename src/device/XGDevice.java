package device;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComponent;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import gui.XGContext;
import gui.XGDeviceConfigurator;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import msg.XGMessageDumpRequest;
import msg.XGRequest;
import msg.XGResponse;
import obj.XGType;
import opcode.XGOpcode;
import parm.XGParameter;
import parm.XGTranslationMap;
import tag.XGTagableAdressableSet;
import tag.XGTagableSet;
import value.XGValue;
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
	private String name;
	private final int info1, info2;
	private Path defDumpPath;
	private final Queue<XGSysexFile> files = new LinkedList<>();
	private final XGSysexFile defaultSyx;
	private final XGMidi midi;
	private int sysexID;
	private XGWindow childWindow;

	private XGDevice()	//DefaultDevice (XG)
	{	this.config = null;
		this.midi = null;
		this.name = "XG";
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
			this.configure();
		}
		this.sysexID = this.config.parseChildNodeIntegerContentOrNew(TAG_SYSEXID, DEF_SYSEXID);
		this.midi = new XGMidi(this);

		this.setName(this.config.getChildNodeTextContent(TAG_NAME, DEF_DEVNAME));
		this.setColor(new Color(this.config.parseChildNodeIntegerContent(TAG_COLOR, DEF_DEVCOLOR)));
		this.defDumpPath = Paths.get(this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).getTextContent());
		this.info1 = requestInfo1();
		this.info2 = requestInfo2();
		this.values = new XGValueStore(this);
		this.opcodes = XGOpcode.init(this);
		this.types = XGType.init(this);
		this.translations = XGTranslationMap.init(this);
		this.parameters = XGParameter.init(this);
		this.defaultSyx = new XGSysexFile(this, this.defDumpPath.resolve("default.syx").toString());
		this.defaultSyx.load(this.values);
		log.info("device initialized: " + this);
	}

	public File getResourceFile(String fName) throws FileNotFoundException
	{	Path extPath = this.getResourcePath();
		File extFile = extPath.resolve(fName).toFile();
		File intFile = RSCPATH.resolve(this.name).resolve(fName).toFile();
		if(!extFile.canRead() && intFile.canRead())
		{	try
			{	Files.createDirectories(extPath);
				Files.copy(intFile.toPath(), extFile.toPath());
				log.info("can't read file: " + extFile + "; default was copied from: " + intFile);
			}
			catch(IOException e)
			{	log.info(e.getMessage() + ", using default");
				return intFile;
			}
		}
		if(!extFile.canRead() && !intFile.canRead()) throw new FileNotFoundException("can't read files: " + extFile + " nor: " + intFile);
		return extFile;
	}

	public Path getResourcePath()
	{	return HOMEPATH.resolve(this.name);
	}

	public String getName()
	{	return this.name;
	}

	public void setName(String n)
	{	this.name = n;
		this.config.getChildNodeOrNew(TAG_NAME).setTextContent(n);
//TODO: re-init wegen pfadänderung?
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
	{	if(this.defDumpPath == null) return JXG.HOMEPATH;
		return this.defDumpPath;
	}

	public void setDefDumpPath(Path defDumpPath)
	{	this.defDumpPath = defDumpPath;
		this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).setTextContent(defDumpPath.toString());
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		this.config.getChildNodeOrNew(TAG_SYSEXID).setTextContent(this.sysexID);
	}

	public String requestName()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageDumpRequest(this.midi, XGAddressConstants.XGMODELNAMEADRESS);
			m.setDestination(this.midi);
			XGResponse r = m.request();
			XGValue v = r.getValues().get(XGAddressConstants.XGMODELNAMEADRESS);
			if(v != null) return v.toString().strip();
		}
		catch(InvalidXGAddressException | TimeoutException | MidiUnavailableException e)
		{	e.printStackTrace();
		}
		return "unknown device";
	}

	public int requestInfo1()
	{	return 0;
	}

	public int requestInfo2()
	{	return 0;
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
	{	return this.name;
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
	{	return new XGDeviceConfigurator(this.config);
	}

	@Override public void setTree(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTree()
	{	return this.tree;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}
}