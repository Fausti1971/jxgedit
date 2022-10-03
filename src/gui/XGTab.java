package gui;

import application.XGStrings;import xml.XMLNode;import java.util.Objects;

public class XGTab extends XGFrame
{
	final String label;
	final String title;

	public XGTab(XMLNode node)
	{	super(null, XGStrings.toRectangle(Objects.requireNonNull(node.getStringAttribute(ATTR_CONSTRAINT))).getSize());
		this.label = node.getStringAttribute(ATTR_LABEL);
		this.title = node.getStringAttribute(ATTR_TITLE);
	}
}
