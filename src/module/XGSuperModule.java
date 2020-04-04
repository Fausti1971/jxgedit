package module;

import java.awt.event.ActionEvent;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.TimeoutException;
import device.XGDevice;
import gui.XGTree;
import msg.XGRequest;
import msg.XGResponse;
import xml.XMLNode;
import xml.XMLNodeConstants;

public abstract class XGSuperModule implements XGModule, XMLNodeConstants
{	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/***************************************************************************************************************/

	private boolean selected;
	private final String name;
	private final XGModuleTag tag;
	private final XGAddress address;
	private final XGDevice device;
	private final XGModule parentModule;
	private final XGAddressableSet<XGModule> childModule = new XGAddressableSet<XGModule>();
	private final XGAddressableSet<XGBulkDump> bulks;
	private final XMLNode guiTemplate;

	protected XGSuperModule(XGDevice dev)
	{	this.device = dev;
		this.tag = XGModuleTag.unknown;
		this.address = XGALLADDRESS;
		this.name = "root";
		this.parentModule = null;
		this.bulks = null;
		this.guiTemplate = null;
	}

	public XGSuperModule(XGDevice dev, XMLNode n)
	{	this.parentModule = null;
		this.name = n.getStringAttribute(ATTR_NAME);
		this.device = dev;
		this.tag = XGModuleTag.valueOf(n.getStringAttribute(ATTR_ID));
		this.address = new XGAddress(null, n);
		this.bulks = XGBulkDump.init(this, n);
		this.guiTemplate = null; //TODO:
	}
	public XGSuperModule(XGModule par, XGAddress adr)
	{	this.parentModule = par;
		this.address = adr;
		this.name = par.getName();
		this.device = par.getDevice();
		this.tag = (XGModuleTag)par.getTag();
		this.bulks = par.getBulks();
		this.guiTemplate = par.getGuiTemplate();
		par.getChildModules().add(this);
	}

	@Override public XGModuleTag getTag()
	{	return this.tag;
	}

	@Override public XGModule getParentModule()
	{	return this.parentModule;
	}

	@Override public XGDevice getDevice()
	{	if(this.parentModule == null) return this.device;
		else return this.parentModule.getDevice();
	}

	@Override public XMLNode getGuiTemplate()
	{	return this.guiTemplate;
	}

	@Override public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}

	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public void setSelected(boolean s)
	{	this.selected = s;
	}

	@Override public boolean isSelected()
	{	return this.selected;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{
	}

	@Override public String getMessengerName()
	{	return this.getDevice() + " (" + this.name + ")";
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{	return null;
	}

	@Override public String getName()
	{	return this.name;
	}

	@Override public Set<XGModule> getChildModules()
	{	return this.childModule;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String toString()
	{	return this.name;
	}
}
