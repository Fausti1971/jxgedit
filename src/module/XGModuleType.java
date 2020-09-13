package module;

import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.XGDevice;
import gui.XGTemplate;
import gui.XGTreeNode;
import gui.XGWindow;
import msg.XGBulkDumper;
import parm.XGOpcode;
import parm.XGTable;
import xml.XMLNode;

/**
 * Moduletypen, keine Instanzen
 * @author thomas
 *
 */
public class XGModuleType implements XGAddressable, XGModuleConstants, XGLoggable, XGTreeNode, XGBulkDumper
{	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{//	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/********************************************************************************************************************/

	private final XGDevice device;
	private final Set<XGAddress> infoAddresses = new LinkedHashSet<>();
	protected final String name;
	protected final XGAddress address;
	private final XGTemplate guiTemplate;
	protected final XGTable idTranslator;
	private final XMLNode config;
	private final XGAddressableSet<XGAddress> bulks = new XGAddressableSet<>();

	public XGModuleType(XGDevice dev, XMLNode cfg)//für Prototypen
	{	this.config = cfg;
		this.device = dev;
		this.address = new XGAddress(cfg.getStringAttribute(ATTR_ADDRESS));
		this.name = cfg.getStringAttributeOrDefault(ATTR_NAME, DEF_MODULENAME);
		this.idTranslator = this.device.getTables().get(cfg.getStringAttribute(ATTR_TRANSLATOR));

		XGAddressableSet<XGTemplate> tSet = this.device.getTemplates().getAllIncluded(this.address);
		if(tSet.size() > 1) throw new RuntimeException("found " + tSet.size() + " templates for address " + this.address);
		this.guiTemplate = tSet.iterator().next();

		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO1)));
		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO2)));
		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO3)));
		 
		for(XMLNode x : cfg.getChildNodes(TAG_BULK))
		{	XGAddress adr = new XGAddress(x.getStringAttribute(ATTR_ADDRESS), this.address);
			this.bulks.add(adr);
			for(XMLNode o : x.getChildNodes(TAG_OPCODE))
			{	dev.getOpcodes().add(new XGOpcode(this, adr, o));
			}
		}
	}

	public XGDevice getDevice()
	{	return this.device;
	}

	public XGAddressableSet<XGModule> getModules()
	{	return this.device.getModules().getAllIncluded(this.address);
	}

	public XGTemplate getGuiTemplate()
	{	return this.guiTemplate;
	}

	public Set<XGAddress> getInfoAddresses()
	{	return this.infoAddresses;
	}

	public XMLNode getConfig()
	{	return this.config;
	}

	public String getName()
	{	return this.name;
	}

	public XGAddressableSet<XGOpcode> getOpcodes()
	{	return this.device.getOpcodes().getAllIncluded(this.address);
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	switch(e.getActionCommand())
		{	case ACTION_REQUEST:	new Thread(() -> {this.transmitAll(this.device.getMidi(), this.device.getValues());}).start(); break;
			case ACTION_TRANSMIT:	new Thread(() -> {this.transmitAll(this.device.getValues(), this.device.getMidi());}).start(); break;
			default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
		}
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public void setSelected(boolean s)
	{
	}

	@Override public boolean isSelected()
	{	return false;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public String getNodeText()
	{	return this.name + " (" + this.getModules().size() + ")";
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	return this.bulks;
	}
}