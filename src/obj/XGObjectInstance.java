package obj;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import device.XGDevice;
import gui.XGTreeNode;
import gui.XGWindow;
import xml.XMLNode;

public class XGObjectInstance implements XGAdressable, XGTreeNode
{	private final XGObjectType type;
	private final XGAdress adress;
	private XGWindow window;

	public XGObjectInstance(XGDevice dev, XGAdress adr) throws InvalidXGAdressException
	{	this.type = dev.getType(adr);
		this.adress = new XGAdress(INVALIDFIELD, adr.getMid(), INVALIDFIELD);
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

	public int getChildCount()
	{	return 0;
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

	public void nodeSelected()
	{	if(this.getWindow() == null) this.setWindow(new XGWindow(this, XGWindow.getRootWindow(), false, this.getTemplate()));
	}

	public void selectNode()
	{	System.out.println(this + " selected");
	}

	public void unselectNode()
	{	System.out.println(this + " unselected");
	}
}
