package obj;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import device.XGDevice;
import gui.XGTree;
import gui.XGWindow;
import gui.XGWindowSourceTreeNode;
import xml.XMLNode;

public class XGInstance implements XGAddressable, XGWindowSourceTreeNode
{	private XGTree tree;
	private final XGType type;
	private final XGAddress adress;
	private XGWindow window;
	private boolean isSelected = false;

	public XGInstance(XGDevice dev, XGAddress adr) throws InvalidXGAddressException
	{	this.type = dev.getType(adr);
		this.adress = new XGAddress(INDEFINITE, adr.getMid(), INDEFINITE);
	}

	@Override public XGAddress getAddress()
	{	return this.adress;
	}

	public XGType getType()
	{	return this.type;
	}

//TODO: die Darstellung muss vom Type definiert werden...
	@Override public String toString()
	{	try
		{	return String.format("%03d", this.adress.getMid() + 1);
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return this.type.getNodeText();
		}
	}

	@Override public TreeNode getParent()
	{	return this.type;
	}

	@Override public boolean getAllowsChildren()
	{	return false;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.emptyEnumeration();
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	public XMLNode getTemplate()
	{	return this.type.getTemplate();
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return new JLabel("XML-Templates noch nicht implementiert...");
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setSelected(boolean s)
	{	this.isSelected = s;
	}

	@Override public Set<String> getContexts()
	{	return new LinkedHashSet<>();
	}

	@Override public void actionPerformed(ActionEvent e)
	{
		new XGWindow(this, XGWindow.getRootWindow(), false, this.toString());
	}

	@Override public void setTreeComponent(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTreeComponent()
	{	return this.tree;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public String getNodeText()
	{	return this.toString();
	}
}
