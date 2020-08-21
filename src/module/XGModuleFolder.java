package module;

import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressableSet;
import device.XGDevice;
import gui.XGTreeNode;
import msg.XGBulkDump;
import xml.XMLNode;

public class XGModuleFolder extends XGModule
{	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/***************************************************************************************************************/

	public XGModuleFolder(XGDevice dev, XGTreeNode par, String cat, XGAddress adr, XMLNode cfg)
	{	super(dev, par, cat, adr, cfg);
	}

	@Override public boolean isLeaf()
	{	return false;
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	switch(e.getActionCommand())
		{	case ACTION_REQUEST:	new Thread(() ->	{	this.transmitAll(this.device.getMidi(), this.device.getValues());}).start(); break;
			case ACTION_TRANSMIT:	new Thread(() ->	{	this.transmitAll(this.device.getValues(), this.device.getMidi());}).start(); break;
		}
	}

	@Override public String toString()
	{	return this.category + " (" + this.getChildCount() + ")";
	}

	@Override public XGAddressableSet<XGBulkDump> getBulks()
	{	XGAddressableSet<XGBulkDump> set = new XGAddressableSet<>();
		for(XGModule m : this.childModules) set.addAll(m.getBulks());
		return set;
	}
}
