package obj;

import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import device.XGDevice;
import gui.XGTreeNodeComponent;
import gui.XGWindow;
import gui.XGWindowSourceTreeNode;
import xml.XMLNode;

public class XGObjectInstance implements XGAdressable, XGWindowSourceTreeNode
{	private final XGObjectType type;
	private final XGAdress adress;
	private XGWindow window;
	private final XGTreeNodeComponent nodeComponent;
	private boolean isSelected = false;

	public XGObjectInstance(XGDevice dev, XGAdress adr) throws InvalidXGAdressException
	{	this.type = dev.getType(adr);
		this.adress = new XGAdress(INVALIDFIELD, adr.getMid(), INVALIDFIELD);
		this.nodeComponent = new XGTreeNodeComponent(this);
	}

	public XGAdress getAdress()
	{	return this.adress;
	}

	public XGObjectType getType()
	{	return this.type;
	}

//TODO: ausgewählte Values anhängen
	@Override public String toString()
	{	try
		{	return String.format("%03d", this.adress.getMid() + 1);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return this.type.getName();
		}
	}

	public String getInfo()
	{	return this.getType().getName() + " " + this.toString();
	}

	public TreeNode getParent()
	{	return this.type;
	}

	public boolean getAllowsChildren()
	{	return false;
	}

	public Enumeration<? extends TreeNode> children()
	{	return null;
	}

	public XGWindow getWindow()
	{	return this.window;
	}

	public XMLNode getTemplate()
	{	return this.type.getTemplate();
	}

	public void setWindow(XGWindow win)
	{	this.window = win;
	}

	public JComponent getChildWindowContent()
	{	return new JLabel("XML-Templates noch nicht implementiert...");
	}

	public void nodeClicked()
	{	if(this.getWindow() != null) this.getWindow().toFront();
		else this.setWindow(new XGWindow(this, XGWindow.getRootWindow(), false, this.getInfo()));
	}

	public JComponent getGuiComponent()
	{	return this.nodeComponent;
	}

	public JTree getTree()
	{	return (JTree)XGWindow.getRootWindow().getRootComponent();
	}

	public boolean isSelected()
	{	return this.isSelected;
	}

	public void setSelected(boolean s)
	{	this.isSelected = s;
		this.nodeComponent.setStatus();
	}

}
