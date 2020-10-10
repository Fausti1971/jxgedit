package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import adress.InvalidXGAddressException;
import adress.XGMemberNotFoundException;
import module.XGModule;
import xml.XMLNode;

public class XGFrame extends XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	public XGFrame(String text)
	{	super(text);
		this.borderize();
	}

	public XGFrame(XMLNode n, XGModule mod)
	{	super(n, mod);
		if(n.hasAttribute(ATTR_NAME)) this.borderize();
		for(XMLNode x : n.getChildNodes())
		{	try
			{	this.add(newItem(x, mod));
			}
			catch(XGMemberNotFoundException | InvalidXGAddressException e)
			{	LOG.severe(e.getMessage());
			}
		}
//		this.logInitSuccess();
	}

	@Override public Component add(Component comp)
	{	Dimension dim = this.getSize();
		Insets ins = this.getInsets();
		comp.setLocation(comp.getX() + ins.left, comp.getY() + ins.top);
		super.add(comp);
		dim.width = Math.max(dim.width, comp.getX() + comp.getWidth() + ins.right);
		dim.height = Math.max(dim.height, comp.getY() + comp.getHeight() + ins.bottom);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setSize(dim);
		return comp;
	}
}
