package device;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressField;
import adress.XGAddressableSet;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import file.XGSysexFileConstants;
import gui.XGContext;
import gui.XGDeviceDetector;
import gui.XGFileSelector;
import gui.XGFrame;
import gui.XGSpinner;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
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

public class XGDevice implements XGDeviceConstants, Configurable, XGTreeNode, XGContext, XGWindowSource, XGBulkDumper
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

	public static Set<XGDevice> getDevices()
	{	return DEVICES;
	}

//	public static XGDevice getDefaultDevice()
//	{	return DEF;
//	}

	public static void init(XMLNode x)
	{	for(XMLNode n : x.getChildNodes(TAG_DEVICE))
		{
			new Thread(()->
			{
				XGDevice d = null;
				try
				{	d = new XGDevice(n);
					synchronized(DEVICES)
					{	if(DEVICES.add(d)) d.reloadTree();
					}
				}
				catch(InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
				}
			}).start();
		}
		LOG.info(DEVICES.size() + " devices initialized");
	}

	private static void removeDevice(XGDevice dev)
	{	if(JOptionPane.showConfirmDialog(XGWindow.getRootWindow(), "Do you really want to remove " + dev, "remove device...", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
		if(DEVICES.remove(dev))
		{	dev.exit();
			dev.getConfig().removeNode();
			dev.reloadTree();
			LOG.info(dev + " removed");
		}
		else LOG.info(dev + " not found");
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
	private Color color;
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
			this.defaultFileName = this.files.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, JXG.getApp().getConfigPath().resolve(DEF_SYXFILENAME).toString());
			this.configure();
System.out.println("Configure...");
		}
		else
		{	this.name = this.config.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME);
			this.midi = new XGMidi(this);
			this.files = this.config.getChildNodeOrNew(TAG_FILES);
			this.defaultFileName = this.files.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, JXG.getApp().getConfigPath().resolve(DEF_SYXFILENAME).toString());
		}
		this.sysex.setContent(this.config.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID));
		this.setColor(new Color(this.config.getIntegerAttribute(ATTR_COLOR, DEF_DEVCOLOR)));

		XGTemplate.init(this);
		XGTable.init(this);
		XGDefaultsTable.init(this);
		XGParameterTable.init(this);
		this.initStructure();
		this.values = new XGValueStore(this);
		this.initInstances();

		try
		{	this.defaultFile = new XGSysexFile(this, this.defaultFileName.toString());
			this.defaultFile.parse();
			this.transmitAll(this.defaultFile, this.values);
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
		LOG.info("device initialized: " + this);
	}

	private void initStructure()
	{	
		File file;
		try
		{	file = this.getResourceFile(XML_STRUCTURE);
		}
		catch(FileNotFoundException e)
		{	LOG.warning(e.getMessage());
			return;
		}
		XMLNode xml = XMLNode.parse(file);
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS));
			if(adr.getHi().getMin() >= 48)//falls Drumset
			{	for(int h : adr.getHi())//erzeuge für jedes Drumset ein ModuleType
				{	this.moduleTypes.add(new XGModuleType(this, n, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo()), n.getStringAttribute(ATTR_NAME) + (h - 47)));
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
		}
		LOG.info(this.modules.size() + " moduleInstances initialized for " + this);
		LOG.info(this.values.size() + " Values initialized for " + this);
	}

	public void exit()
	{	this.midi.close();
	}

	public File getResourceFile(String fName) throws FileNotFoundException//Merke: SAX scheint mit mac-Aliases nicht zurecht zu kommen, daher bei Bedarf Softlinks erzeugen (ln -s Quelle Ziel)
	{	Path extPath = this.getResourcePath();
		File extFile = extPath.resolve(fName).toFile();
		if(extFile.canRead()) return extFile;
		throw new FileNotFoundException("can't read file " + extFile);
	}

	public Path getResourcePath()
	{	return JXG.getApp().getConfigPath().resolve(this.name.toString());
	}

	public Color getColor()
	{	return color;
	}

	public void setColor(Color color)
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
					String s = r.getString(r.getBaseOffset(), r.getBaseOffset() + r.getBulkSize());
					this.name.replace(0, this.name.length(), s.trim());
				}
				else JOptionPane.showMessageDialog(this.getChildWindow(), "no response for " + m);
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
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

	private void resetGM()
	{	try
		{	this.midi.transmit(new SysexMessage(new byte[]{(byte)0xF0,0x7E,0x7F,0x09,0x01,(byte)0xF7}, 6));
		}
		catch(InvalidMidiDataException e)
		{	e.printStackTrace();
		}
	}

	private void resetAll()
	{	try
		{	new XGMessageParameterChange(this.values, this.midi, new byte[]{0,0,0,0,0,0,0x7F,0,0}, true).transmit();
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

	public void configure()
	{	JDialog d = new JDialog(XGWindow.getRootWindow(), this.toString(), true);
		d.add(this.getChildWindowContent());
		d.pack();
		d.setVisible(true);
		
//		new XGWindow(this, XGWindow.getRootWindow(), true, false, this.toString());// seit XGWindow von JFrame erbt, kann es nicht mehr modal gemacht werden
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
		{	case ACTION_CONFIGURE:	this.configure(); break;
			case ACTION_REMOVE:		removeDevice(this); break;
			case ACTION_LOADFILE:	this.load(); break;
			case ACTION_SAVEFILE:	this.save(); break;
			case ACTION_TRANSMIT:	new Thread(() -> {this.transmitAll(this.values, this.midi);}).start(); break;
			case ACTION_REQUEST:	new Thread(() -> {this.transmitAll(this.midi, this.values);}).start(); break;
			case ACTION_RESET:		this.resetAll(); break;
			case ACTION_XGON:		this.resetXG(); break;
			case ACTION_GMON:		this.resetGM(); break;
			default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
		}
	}

	@Override public XGWindow getChildWindow()
	{	return this.childWindow;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.childWindow = win;
	}

	@Override public Point childWindowLocationOnScreen()
	{	return this.locationOnScreen();
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
	{	GridBagConstraints gbc = new GridBagConstraints();
		XGFrame root = new XGFrame("device");
		root.setLayout(new GridBagLayout());

		JComponent c = this.midi.getConfigComponent();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		root.add(c, gbc);

		c = new XGSpinner("sysexID", this.sysex, 0, 15, 1);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		root.add(c, gbc);

		c = new XGDeviceDetector("device name", this.name, this);
		gbc.gridx = 0;
		gbc.gridy = 2;
		root.add(c, gbc);

		c = new XGFileSelector(this.defaultFileName, "default dump file", "select", XGSysexFileConstants.SYX_FILEFILTER).small();
		gbc.gridx = 0;
		gbc.gridy = 3;
		root.add(c, gbc);

		return root;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public String getNodeText()
	{	return this.name.toString() + "." + this.sysexID;
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModule mi : this.modules) set.addAll(mi.getBulks());
		return set;
	}

	@Override public TreeNode getParent()
	{	return JXG.getApp();
	}

	@Override public XGTree getTreeComponent()
	{	return this.getRootNode().getTreeComponent();
	}

	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public Collection<? extends TreeNode> getChildNodes()
	{	Set<XGTreeNode> set = new LinkedHashSet<>();
		for(XGModuleType t : this.getModuleTypes())
		{	XGAddressableSet<XGModule> temp = t.getModules();
			if(temp.size() == 0) continue;
			if(temp.size() == 1) set.add(temp.get(0));
			else set.add(t);
		}
		return set;
	}
}