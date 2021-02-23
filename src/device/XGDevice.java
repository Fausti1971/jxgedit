package device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.*;
import java.net.*;import java.nio.file.*;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressField;
import adress.XGAddressableSet;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import file.XGSysexFileConstants;
import gui.XGColor;
import gui.XGDeviceDetector;
import gui.XGFileSelector;
import gui.XGSpinner;
import gui.XGTemplate;
import gui.XGUI;
import gui.XGWindow;
import module.XGDrumsetModuleType;
import module.XGModule;
import module.XGModuleType;
import msg.XGBulkDumper;
import msg.XGMessageBulkRequest;
import msg.XGMessageParameterChange;
import msg.XGMessenger;
import msg.XGMessengerException;
import msg.XGRequest;
import msg.XGResponse;
import parm.XGDefaultsTable;
import parm.XGOpcode;
import parm.XGParameterTable;
import parm.XGTable;
import tag.XGTagableSet;
import value.ChangeableContent;
import value.XGValue;
import value.XGValueStore;
import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, Configurable, XGBulkDumper
{
	private static Set<XGDevice> DEVICES = new LinkedHashSet<>();
//	private static XGDevice DEF = new XGDevice();
	static
	{	ACTIONS.add(ACTION_CONFIGURE);
		ACTIONS.add(ACTION_REMOVE);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_RESET);
		ACTIONS.add(ACTION_XGON);
		ACTIONS.add(ACTION_GMON);
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
	private final StringBuffer defaultFileName, name;
	private XGColor color;
	private boolean isSelected = false;

	private XMLNode config;
	private final XGTagableSet<XGTable> tables = new XGTagableSet<>();
	private final XGTagableSet<XGParameterTable> parameterTables = new XGTagableSet<>();
	private final XGTagableSet<XGDefaultsTable> defaultsTables = new XGTagableSet<>();
	private final XGAddressableSet<XGTemplate> templates = new XGAddressableSet<>();

	private final XGAddressableSet<XGModuleType> moduleTypes = new XGAddressableSet<>();//Prototypen (inkl. XGAddress bulks); initialisiert auch XGOpcodes
	private final XGAddressableSet<XGOpcode> opcodes = new XGAddressableSet<>();//Prototypen

	private final XGAddressableSet<XGModule> modules = new XGAddressableSet<>();//Instanzen
	private final XGValueStore values;

	private int info1, info2;
	private final XMLNode files;
	private XGSysexFile defaultFile;
	private XGMidi midi;
	private int sysexID;
	private XGWindow childWindow;

//	private XGDevice()//für DefaultDevice
//	{	this.defaultFileName = new StringBuffer(DEF_SYXFILENAME);
//		this.files = new XMLNode("files");
//		this.name = new StringBuffer("XG");
//		this.parameterTables = XGParameter.init(this);
//		this.defaultsTables = XGDefaults.init(this);
//		XGTemplate.init(this);
//		XGTable.init(this);
//		this.values = null;
//	}

	public XGDevice(XMLNode cfg) throws InvalidXGAddressException
	{	this.config = cfg;
		if(this.config == null)
		{	this.config = new XMLNode(TAG_DEVICE);
			this.name = this.config.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME);
			this.midi = new XGMidi(this);
			this.files = this.config.getChildNodeOrNew(TAG_FILES);
			this.defaultFileName = this.files.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, Paths.get(APPPATH).resolve(DEF_SYXFILENAME).toString());
//			this.configure();
		}
		else
		{	this.name = this.config.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME);
			this.midi = new XGMidi(this);
			this.files = this.config.getChildNodeOrNew(TAG_FILES);
			this.defaultFileName = this.files.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, Paths.get(APPPATH).resolve(DEF_SYXFILENAME).toString());
		}
		this.sysex.setContent(this.config.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID));
		this.setColor(new XGColor(this.config.getIntegerAttribute(ATTR_COLOR, XGUI.DEF_DEVCOLOR)));

		XGTable.init(this);
		XGDefaultsTable.init(this);
		XGParameterTable.init(this);
		this.initStructure();
		this.values = new XGValueStore(this);
		this.initInstances();

//		try
//		{	this.defaultFile = new XGSysexFile(this, this.defaultFileName.toString());
//			this.defaultFile.parse();
//			this.transmitAll(this.defaultFile, this.values);
//		}
//		catch(IOException e)
//		{	LOG.severe(e.getMessage());
//			for(XGModuleType mt : this.getModuleTypes()) mt.resetValues();
//		}
		LOG.info("device initialized: " + this);
	}

	private void initStructure()
	{	XMLNode xml;
		try
		{
			xml = XMLNode.parse(this.getResourceFile(XML_STRUCTURE));
		}
		catch(IOException e)
		{	
			LOG.warning(e.getMessage());
			return;
		}
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS));
			if(adr.getHi().getMin() >= 48)//falls Drumset
			{	for(int h : adr.getHi())//erzeuge für jedes Drumset ein ModuleType
				{	this.moduleTypes.add(new XGDrumsetModuleType(this, n, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo())));
				}
				continue;
			}
			this.moduleTypes.add(new XGModuleType(this, n));
		}
		LOG.info(this.moduleTypes.size() + " moduleTypes initialized for " + this);
	}

	private void initInstances()
	{	for(XGModuleType mt : this.moduleTypes)
		{	for(int m : mt.getAddress().getMid())
			{	try
				{	for(XGOpcode opc : mt.getOpcodes()) this.values.add(new XGValue(opc, m));
					XGModule mod = new XGModule(mt, m);
					this.modules.add(mod);
					for(XGValue val : mod.getValues()) val.initValueDepencies();
				}
				catch(InvalidXGAddressException e)
				{	LOG.warning(e.getMessage());
				}
			}
			if(mt instanceof XGDrumsetModuleType)
			{	((XGDrumsetModuleType)mt).initDepencies();
			}
		}
		for(XGValue v : this.values) v.setDefaultValue();

		LOG.info(this.modules.size() + " moduleInstances initialized for " + this);
		LOG.info(this.values.size() + " Values initialized for " + this);
	}

	public void exit()
	{	this.midi.close();
	}

/**
* returniert das angegebene File aus dem Applikationspfad; falls dieses nicht vorhanden ist, wird versucht, es aus dem internen Pfad (*.jar) dorthin zu kopieren
*/
	public File getResourceFile(String fName) throws IOException //Merke: SAX scheint mit mac-Aliases nicht zurecht zu kommen, daher bei Bedarf Softlinks erzeugen (ln -s Quelle Ziel)
	{	Path extPath = Paths.get(JXG.APPPATH);
		File extFile = extPath.resolve(fName).toFile();
		if(!extFile.canRead())
		{	InputStream link = ClassLoader.getSystemResourceAsStream(fName);
			LOG.info(extFile + " doesn't exist; copy " + link + " to " + extFile);
			Files.copy(link, extFile.toPath());
		}
		return extFile;
	}

	public XGColor getColor()
	{	return this.color;
	}

	public void setColor(XGColor color)
	{	this.color = color;
	}

	public XGTagableSet<XGDefaultsTable> getDefaultsTables()
	{	return this.defaultsTables;
	}

	public XGTagableSet<XGParameterTable> getParameterTables()
	{	return parameterTables;
	}

	public XGValueStore getValues()
	{	return this.values;
	}

	public XGAddressableSet<XGModuleType> getModuleTypes()
	{	return moduleTypes;
	}

	public XGAddressableSet<XGTemplate> getTemplates()
	{	return this.templates;
	}

	public XGTagableSet<XGTable> getTables()
	{	return this.tables;
	}

	public XGAddressableSet<XGModule> getModules()
	{	return this.modules;
	}

	public XGAddressableSet<XGOpcode> getOpcodes()
	{	return this.opcodes;
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
		{	m = new XGMessageBulkRequest(this.midi, this.midi, XGAddressConstants.XGMODELNAMEADRESS);// TODO: Krücke, für dest existiert hier noch kein XGMessenger, der allerdings für getDevice() erforderlich ist
			try
			{	m.request();
				if(m.isResponsed())
				{	XGResponse r = m.getResponse();
					int offs = r.getBaseOffset();
					String s = r.getString(offs, offs + 14);
					this.name.replace(0, this.name.length(), s.trim());
					this.info1 = r.decodeLSB(offs + 14);
					this.info2 = r.decodeLSB(offs + 15);
				}
				else JOptionPane.showMessageDialog(null, "no response for " + m);
			}
			catch(InvalidXGAddressException | XGMessengerException e)
			{	LOG.severe(e.getMessage());
			}
		}
		catch(InvalidXGAddressException | InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}

	private void resetXG()
	{	try
		{	new XGMessageParameterChange(this.values, this.midi, new byte[]{0,0,0,0,0,0,0x7E,0,0}, true).transmit();
			for(XGModuleType mt : this.getModuleTypes()) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

	private void resetGM()
	{	try
		{	this.midi.transmit(new SysexMessage(new byte[]{(byte)0xF0,0x7E,0x7F,0x09,0x01,(byte)0xF7}, 6));
			for(XGModuleType mt : this.getModuleTypes()) mt.resetValues();
		}
		catch(InvalidMidiDataException e)
		{	e.printStackTrace();
		}
	}

	private void resetAll()
	{	try
		{	new XGMessageParameterChange(this.values, this.midi, new byte[]{0,0,0,0,0,0,0x7F,0,0}, true).transmit();
			for(XGModuleType mt : this.getModuleTypes()) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

	private void load()
	{	XMLNode last = this.files.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "open sysex file...", "open", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(this.childWindow))
		{	case JFileChooser.APPROVE_OPTION:
			{	XGSysexFile f;
				try
				{	f = new XGSysexFile(this, last.getTextContent().toString());
					f.parse();
					this.transmitAll(f, this.values);
					f.close();
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	last.removeNode();
				LOG.severe("fileselection aborted");
				break;
			}
		}
	}

	private void save()
	{	XMLNode last = this.files.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "save sysex file...", "save", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(this.childWindow))
		{	case JFileChooser.APPROVE_OPTION:
			{	XGSysexFile f;
				try
				{	f = new XGSysexFile(this, last.getTextContent().toString());
					f.parse();// damit die in einer Datei enthaltenen (aber vom ValueStore nicht unterstützen) Messages erhalten bleiben
					this.transmitAll(this.values, f);
					f.save();
					f.close();
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	last.removeNode();
				LOG.warning("fileselection aborted");
				break;
			}
		}
	}

	public XGMessenger getMidi()
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
	{	return this.name.toString();
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	public JComponent getConfigComponent()
	{	GridBagConstraints gbc = new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, 2, 2, 0.5, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());

		JComponent c = this.midi.getConfigComponent();
		root.add(c, gbc);

		c = new XGSpinner("sysexID", this.sysex, 0, 15, 1);
		gbc.gridx = 0;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0;
		root.add(c, gbc);

		c = new XGDeviceDetector("device name", this.name, this);
		root.add(c, gbc);

		c = new XGFileSelector(this.defaultFileName, "default dump file", "select", XGSysexFileConstants.SYX_FILEFILTER).small();
		root.add(c, gbc);

		return root;
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModule mi : this.modules) set.addAll(mi.getBulks());
		return set;
	}
}