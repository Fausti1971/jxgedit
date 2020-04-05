package module;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Set;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.TimeoutException;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTree;
import gui.XGWindow;
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
	private XGWindow window;
	private final String name;
	private final XGModuleTag tag;
	private final XGAddress address;
	private final XGDevice device;
	private final XGModule parentModule;
	private final XGAddressableSet<XGModule> childModule = new XGAddressableSet<XGModule>();
	private final XGAddressableSet<XGBulkDump> bulks;
	private final XMLNode guiTemplate;
/*
	protected XGSuperModule(XGDevice dev)
	{	this.device = dev;
		this.tag = XGModuleTag.unknown;
		this.address = XGALLADDRESS;
		this.name = "root";
		this.parentModule = null;
		this.bulks = null;
		this.guiTemplate = dev.getTemplates().getChildNodeWithID(TAG_TEMPLATE, this.tag.name());
	}
*/
	public XGSuperModule(XGDevice dev, XMLNode n)
	{	this.parentModule = null;
		this.name = n.getStringAttribute(ATTR_NAME);
		this.device = dev;
		this.tag = XGModuleTag.valueOf(n.getStringAttribute(ATTR_ID));
		this.address = new XGAddress(null, n);
		this.bulks = XGBulkDump.init(this, n);
		if(dev.getTemplates() != null) this.guiTemplate = dev.getTemplates().getChildNodeWithID(TAG_TEMPLATE, this.tag.name());
		else this.guiTemplate = null;
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

	@Override public void windowOpened(WindowEvent e)
	{	this.setSelected(true);
		this.repaintNode();
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.setSelected(false);
		this.repaintNode();
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	log.info(e.getActionCommand());
		switch(e.getActionCommand())
		{	case ACTION_EDIT:	new XGWindow(this, XGWindow.getRootWindow(), false, this.toString()); break;
		}
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return XGComponent.init(this.guiTemplate);
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
