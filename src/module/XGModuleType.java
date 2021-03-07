package module;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.Configurable;
import application.XGLoggable;
import device.XGDevice;
import gui.XGTemplate;
import gui.XGTreeNode;
import gui.XGWindow;
import msg.XGBulkDumper;
import parm.XGOpcode;
import static parm.XGOpcode.OPCODES;import parm.XGTable;
import static parm.XGTable.TABLES;import tag.*;import xml.XMLNode;

/**
 * Moduletypen, keine Instanzen
 * @author thomas
 *
 */
public class XGModuleType implements XGAddressable, XGModuleConstants, XGLoggable, XGBulkDumper, Configurable, XGTagable
{
	public static final XGTagableAddressableSet<XGModuleType> TYPES = new XGTagableAddressableSet<>();//Prototypen (inkl. XGAddress bulks); initialisiert auch XGOpcodes
	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/********************************************************************************************************************/

	private final Set<XGOpcode> infoOpcodes = new LinkedHashSet<>();
	protected final String name;
	protected final StringBuffer id;
	protected final XGAddress address;
	protected final XGTable idTranslator;
	private final XMLNode config;
	private final XGAddressableSet<XGAddress> bulks = new XGAddressableSet<>();

	public XGModuleType(XMLNode cfg, XGAddress adr, String name)
	{	this.config = cfg;
		this.address = adr;
		this.name = name;
		this.id = cfg.getStringBufferAttributeOrNew(ATTR_ID, "missing id " + adr);
		this.idTranslator = TABLES.get(cfg.getStringAttribute(ATTR_TRANSLATOR));

		for(XMLNode x : cfg.getChildNodes(TAG_BULK))
		{	XGAddress a = new XGAddress(x.getStringAttribute(ATTR_ADDRESS), this.address);
			this.bulks.add(a);
			for(XMLNode o : x.getChildNodes(TAG_OPCODE))
			{	try
				{	OPCODES.add(new XGOpcode(this, a, o));
				}
				catch(InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
				}
			}
		}

		for(XMLNode n : cfg.getChildNodes(TAG_INFO))
		{	XGOpcode opc = OPCODES.get(n.getStringAttribute(ATTR_REF));
			if(opc != null) this.infoOpcodes.add(opc);
		}
	}

	public XGModuleType(XMLNode cfg)
	{	this(cfg, new XGAddress(cfg.getStringAttribute(ATTR_ADDRESS)), cfg.getStringAttributeOrDefault(ATTR_NAME, DEF_MODULENAME));
	}

	public XGAddressableSet<XGModule> getModules()
	{	return XGModule.INSTANCES.getAllIncluded(this.address);
	}

	public Set<XGOpcode> getInfoOpcodes()
	{	return this.infoOpcodes;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	public String getName()
	{	return this.name;
	}

	//public XGAddressableSet<XGOpcode> getOpcodes()
	//{	return XGOpcode.OPCODES.getAllIncluded(this.address);
	//}

	public XGAddressableSet<XGAddress> getBulkAdresses()
	{	return this.bulks;
	}

	public void resetValues()
	{	for(XGModule m : this.getModules()) m.resetValues();
	}

	//@Override public void actionPerformed(ActionEvent e)
	//{	switch(e.getActionCommand())
	//	{	case ACTION_REQUEST:	new Thread(() -> {this.transmitAll(this.device.getMidi(), this.device.getValues());}).start(); break;
	//		case ACTION_TRANSMIT:	new Thread(() -> {this.transmitAll(this.device.getValues(), this.device.getMidi());}).start(); break;
	//		default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
	//	}
	//}

	@Override public String toString()
	{	return this.name;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModule m : this.getModules()) set.addAll(m.getBulks());
		return set;
	}

	public String getTag()
	{	return this.id.toString();
	}
}