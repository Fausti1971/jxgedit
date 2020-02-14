package device;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
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
import gui.XGAction;
import gui.XGDeviceConfigurator;
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

public class XGDevice implements XGDeviceConstants, Configurable, XGTreeNode, XGAction, XGWindowSource
{	private static Logger log = Logger.getAnonymousLogger();
	private static Set<XGDevice> DEVICES = new HashSet<>();
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
	{	for(XMLNode n : JXG.getJXG().getConfig().getChildren())
		{	if(n.getTag().equals(TAG_DEVICE))
			{	XGDevice d = new XGDevice(n);
				if(DEVICES.add(d)) d.reloadTree();
			}
		}
		log.info(DEVICES.size() + " devices initialized");
	}

/***************************************************************************************************************************/

	private boolean isSelected = false;
//	private final XMLNode template;
	private final XMLNode config;
	private final XGValueStore values;
//	private final XGTagableSet<XGModule> modules;	//modules - bulks - opcodes
	private final XGTagableAdressableSet<XGOpcode> opcodes;
	private final XGTagableSet<XGType> types;
	private final XGTagableSet<XGTranslationMap> translations;
	private final XGTagableSet<XGParameter> parameters;
	private final String name;
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
	}

	public XGDevice(XMLNode cfg)
	{	if(cfg == null)
		{	this.config = new XMLNode(TAG_DEVICE, null);
			this.midi = new XGMidi(this);
		}
		else
		{	this.config = cfg;
			this.sysexID = this.config.parseChildNodeIntegerContentOrNew(TAG_SYSEXID, DEF_SYSEXID);
			this.midi = new XGMidi(this);
		}
		this.name = this.requestName();
		if(this.name == null)
		{	log.info("device (ID " + this.sysexID + ") not responding for " + this.midi.getInputName());
		}
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

	void setSysexID(int id)
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

	@Override public Set<String> getActions()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	switch(e.getActionCommand())
		{	case ACTION_CONFIGURE:
				new XGWindow(this, XGWindow.getRootWindow(), true, this.toString()); break;
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

	@Override public JComponent getChildWindowContent()
	{	return new XGDeviceConfigurator(this).getGuiComponent();
	}
}