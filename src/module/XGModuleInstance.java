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
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import msg.XGBulkDump;
import msg.XGBulkDumper;
import value.XGValue;
import value.XGValueChangeListener;

public class XGModuleInstance implements XGAddressable, XGModuleConstants, XGValueChangeListener, XGLoggable, XGTreeNode, XGWindowSource, XGBulkDumper
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
	private final XGModule type;
	private XGWindow window;
	private boolean selected;

	public XGModuleInstance(XGModule mod, XGAddress adr) throws InvalidXGAddressException
	{	this.type = mod;
		this.address = mod.getAddress().complement(adr);
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

	@Override public XGTree getTreeComponent()
	{	return this.getRootNode().getTreeComponent();
	}

	@Override public Component getSourceComponent()
	{	return this.getNodeComponent();
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
		this.setChildWindow(null);
		this.repaintNode();
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

	@Override public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaintNode();
	}

	@Override public String getNodeText()
	{	String s = this.getTranslatedID() + ":\t";
		for(XGValue v : this.infoValues) s += "\t" + v;
		return s;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

}
