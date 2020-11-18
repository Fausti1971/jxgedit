package module;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
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
import value.XGValue;

public class XGModule implements XGAddressable, XGModuleConstants, XGLoggable, XGTreeNode, XGWindowSource, XGBulkDumper
{
	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
		ACTIONS.add(ACTION_RESET);
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

		for(XGAddress adr : this.type.getInfoAddresses())
		{	try
			{	XGValue v = this.type.getDevice().getValues().get(adr.complement(this.address));
				this.infoValues.add(v);
				v.addValueListener((XGValue val)->{this.repaintNode();});
			}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());
			}
		}
	}

	public XGModuleType getType()
	{	return this.type;
	}

	public boolean isSingleton()
	{	return this.type.getAddress().getMid().isFix();
	}

	public XGAddressableSet<XGValue> getValues()
	{	return this.type.getDevice().getValues().getAllIncluded(this.address);
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
	{	if(this.window == null) new XGWindow(this, XGWindow.getRootWindow(), false, false, this.type.getDevice() + "/" + this);
		else this.window.toFront();
	}

	public void resetValues()
	{	for(XGValue v : this.getValues()) v.setDefaultValue();
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGDevice dev = this.type.getDevice();
		switch(e.getActionCommand())
		{	case ACTION_EDIT:		this.editWindow(); break;
			case ACTION_REQUEST:	new Thread(() -> {this.transmitAll(dev.getMidi(), dev.getValues());}).start(); break;
			case ACTION_TRANSMIT:	new Thread(() -> {this.transmitAll(dev.getValues(), dev.getMidi());}).start(); break;
			case ACTION_RESET:		if(JOptionPane.showConfirmDialog(XGWindow.getRootWindow(), "Do you really want to reset " + this, "Reset Module?", JOptionPane.CANCEL_OPTION) == JOptionPane.OK_OPTION) this.resetValues(); break;
			default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
		}
	}

	@Override public String toString()
	{	return this.type.getName() + " " + this.getTranslatedID();
	}

	@Override public void setSelected(boolean s)
	{	boolean changed = s != this.selected;
		this.selected = s;
		if(changed) this.repaintNode();
	}

	@Override public boolean isSelected()
	{	return this.selected;
	}

	@Override public void nodeFocussed(boolean b)
	{	if(this.window != null) this.window.toFront();
	}

	@Override public void windowOpened(WindowEvent e)
	{	this.setSelected(true);
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.setSelected(false);
		this.setChildWindow(null);
	}

	@Override public Point getSourceLocationOnScreen()
	{	return this.locationOnScreen();
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return XGComponent.init(this);
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGAddress bd : this.type.getBulkAdresses())
		{	try
			{	set.add(bd.getAddress().complement(this.address));
			}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());
			}
		}
		return set;
	}

	@Override public String getNodeText()
	{	String s;
		if(this.isSingleton()) s = this.type.getName();
		else s = this.getTranslatedID() + ":";
		for(XGValue v : this.infoValues) s += "\t" + v;
		return s;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public TreeNode getParent()
	{	if(this.isSingleton()) return this.type.getParent();
		else return this.type;
	}

	@Override public Collection<? extends TreeNode> getChildNodes()
	{	return Collections.emptySet();
	}
}
