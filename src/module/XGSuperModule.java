package module;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Set;
import javax.swing.JComponent;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressableSet;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGWindow;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGSuperModule implements XGModule, XMLNodeConstants
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
	private final XGAddress address;
	private final XGDevice device;
	private final XGModule parentModule;
	private final XGAddressableSet<XGModule> childModule = new XGAddressableSet<XGModule>();
	private final XGTemplate guiTemplate;

	public XGSuperModule(XGDevice dev, XMLNode n)
	{	this.parentModule = null;
		this.name = n.getStringAttribute(ATTR_NAME);
		this.device = dev;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
		this.guiTemplate = dev.getTemplates().get(this.address);
		this.initChildren();
		log.info("module initialized: " + this.name);
	}

	public XGSuperModule(XGModule par, XGAddress adr, String n)
	{	this.parentModule = par;
		this.address = adr;
		if(n == null) this.name = par.getName();
		else this.name = n;
		this.device = par.getDevice();
		this.guiTemplate = par.getGuiTemplate();
		par.getChildModules().add(this);
	}

	private void initChildren()
	{	XGModule hiMod = this;
		for(int h : this.getAddress().getHi())
		{	if((h & 0x20) != 0) hiMod = new XGSuperModule(this, new XGAddress(new XGAddressField(h), this.getAddress().getMid(), this.getAddress().getLo()), this.name + " " + (h - 47));
			for(int m : hiMod.getAddress().getMid())
				new XGSuperModule(hiMod, new XGAddress(hiMod.getAddress().getHi(), new XGAddressField(m), hiMod.getAddress().getLo()), this.name + " " + (m + 1));
		}
	}

	@Override public XGModule getParentModule()
	{	return this.parentModule;
	}

	@Override public XGAddressableSet<XGModule> getChildModules()
	{	return this.childModule;
	}

	@Override public XGDevice getDevice()
	{	if(this.parentModule == null) return this.device;
		else return this.parentModule.getDevice();
	}

	@Override public XGTemplate getGuiTemplate()
	{	return this.guiTemplate;
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
	{	return XGComponent.init(this).getJComponent();
	}

	@Override public String getMessengerName()
	{	return this.getDevice() + " (" + this.name + ")";
	}

	@Override public String getName()
	{	return this.name;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String toString()
	{	return this.name;
	}
}
