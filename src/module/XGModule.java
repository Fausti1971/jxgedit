package module;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import msg.XGBulkDumper;
import parm.XGOpcode;
import value.XGValue;
import value.XGValueChangeListener;

public class XGModule implements XGAddressable, XGModuleConstants, XGValueChangeListener, XGLoggable, XGTreeNode, XGWindowSource, XGBulkDumper
{	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/***************************************************************************************************************/

	private final Set<XGValue> infoValues = new LinkedHashSet<>();
	private final XGAddress address;
	private final XGModuleType type;
	private XGWindow window;
	private boolean selected;

	public XGModule(XGModuleType mt, int id) throws InvalidXGAddressException
	{	this.type = mt;
		this.address = new XGAddress(mt.getAddress().getHi(), new XGAddressField(id), mt.getAddress().getLo());
		for(XGOpcode opc : mt.getOpcodes())
		{	
		}
		this.registerValueListener();
	}

	void registerValueListener()
	{	for(XGAddress adr : this.type.getInfoAddresses())
		{	XGAddress a;
			try
			{	a = adr.complement(this.address);
				XGValue v = this.type.getDevice().getValues().get(a);
				if(v != null)
				{	this.infoValues.add(v);
					v.addValueListener(this);
				}
			}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());
			}
		}
	}

	public XGModuleType getType()
	{	return this.type;
	}

	public String getTranslatedID()
	{	try
		{	return this.type.idTranslator.getByIndex(this.address.getMid().getValue()).getName();
		}
		catch(InvalidXGAddressException | NullPointerException e)
		{	return this.address.getMid().toString();
		}
	}

	private void editWindow()
	{	if(this.window == null) new XGWindow(this, XGWindow.getRootWindow(), false, false, this.type.getDevice() + "/" + this + " " + this.getTranslatedID());
		else this.window.toFront();
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGDevice dev = this.type.getDevice();
		switch(e.getActionCommand())
		{	case ACTION_EDIT:		this.editWindow(); break;
			case ACTION_REQUEST:	new Thread(() -> {	this.transmitAll(dev.getMidi(), dev.getValues());}).start(); break;
			case ACTION_TRANSMIT:	new Thread(() -> {	this.transmitAll(dev.getValues(), dev.getMidi());}).start(); break;
			default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
		}
	}

	@Override public String toString()
	{	return this.type.getName() + this.getTranslatedID();
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
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.setSelected(false);
		this.setChildWindow(null);
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return XGComponent.init(this.type);
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGAddress bd : this.type.getBulks())
		{	try
			{	set.add(bd.getAddress().complement(this.address));
			}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());
			}
		}
		return set;
	}

	@Override public void contentChanged(XGValue v)
	{
	}

	@Override public String getNodeText()
	{	String s = this.getTranslatedID() + ":\t";
		for(XGValue v : this.infoValues) s += "\t" + v;
		return s;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public Component getSourceComponent()
	{	return null;
	}

}
