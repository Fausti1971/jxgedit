package gui;

import application.XGStrings;import xml.XMLNode;

public class XGTab extends XGFrame
{
	final String label;
	final String title;

	public XGTab(XMLNode node)
	{	super(null, XGStrings.toRectangle(node.getStringAttribute(ATTR_CONSTRAINT)).width, XGStrings.toRectangle(node.getStringAttribute(ATTR_CONSTRAINT)).height);
		this.label = node.getStringAttribute(ATTR_LABEL);
		this.title = node.getStringAttribute(ATTR_TITLE);
	}
}
