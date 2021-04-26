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
		this.root = n.getParentNode() == null || n.getParentNode().getTag().equals("TAG_TEMPLATES");
		this.setOpaque(root);
	}

	public XGFrame(String text)
	{	this(new XMLNode(text, new XGProperties(ATTR_NAME, text)));
	}
}
