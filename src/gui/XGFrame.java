package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JPanel;
import adress.InvalidXGAddressException;
import adress.XGMemberNotFoundException;
import module.XGModule;
import xml.XGProperties;
import xml.XMLNode;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	private final XMLNode config;
	private final boolean root;

	public XGFrame(XMLNode n)
	{	this.config = n;
		if(n.hasAttribute(ATTR_NAME))
		{	this.setName(n.getStringAttribute(ATTR_NAME));
			this.borderize();
		}
		this.root = n.getParentNode() == null || n.getParentNode().getTag().equals(TAG_TEMPLATES);
		this.setOpaque(root);
	}

	public XGFrame(String text)
	{	this(new XMLNode(text, new XGProperties(ATTR_NAME, text)));
	}

	protected XGFrame(XMLNode n, XGModule mod)
	{	this(n);
		for(XMLNode x : n.getChildNodes())
		{	try
			{	this.add(XGComponent.newItem(x, mod));
			}
			catch(XGMemberNotFoundException | InvalidXGAddressException e)
			{	LOG.severe(e.getMessage());
			}
		}
		if(this.root) this.setBackground(mod.getType().getDevice().getColor());
	}

	@Override public Component add(Component comp)
	{	Dimension dim = this.getSize();
		Insets ins = this.getInsets();
		comp.setLocation(comp.getX() + ins.left, comp.getY() + ins.top);
		super.add(comp);
		dim.width = Math.min(this.getMaximumSize().width, Math.max(dim.width, comp.getX() + comp.getWidth() + ins.right));
		dim.height = Math.min(this.getMaximumSize().height, Math.max(dim.height, comp.getY() + comp.getHeight() + ins.bottom));
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setSize(dim);
		return comp;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}
}
