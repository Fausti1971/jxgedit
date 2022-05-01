package gui;

import application.XGStrings;import xml.XMLNode;import java.awt.*;
import javax.swing.*;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	private JLabel name = null;

/**
* erzeugt ein JPanel mit einem XGLayout mit den übergebenen Grid-Dimensionen, optional mit Label an Position "0,0,w++,1"
*/
	public XGFrame(int columns, int rows, boolean border)
	{	this.setLayout(new XGLayout(new Dimension(columns, rows)));
		if(border) this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	//	this.setBorder(this);
	}

	public XGFrame(Dimension dim)
	{	this(dim.width, dim.height, false);
	}

	public XGFrame(boolean border)
	{	this(1, 1, border);
	}

	public XGFrame(String text)
	{	this(true);
		this.addNameLabel(text);
	}

	public XGFrame(XMLNode xml)
	{	Rectangle r = new Rectangle(0,0,1,1);

		if(xml.hasAttribute(ATTR_CONSTRAINT)) r = XGStrings.toRectangle(xml.getStringAttribute(ATTR_CONSTRAINT));
		this.setLayout(new XGLayout(new Dimension(r.width, r.height)));

		if(xml.hasAttribute(ATTR_BORDER))
		{	if(Boolean.getBoolean(xml.getStringAttribute(ATTR_BORDER)))	this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		}

		if(xml.hasAttribute(ATTR_LABEL)) this.addNameLabel(xml.getStringAttribute(ATTR_LABEL));
	}

	private void addNameLabel(String text)
	{	super.setName(text);
		this.name = new JLabel(this.getName());
		this.name.setHorizontalAlignment(JLabel.CENTER);
		this.name.setVerticalAlignment(JLabel.CENTER);
		super.add(this.name, "0,0,1,1");
	}

	@Override public void setName(String name)
	{	if(this.name == null) throw new RuntimeException(this + " has no name(label)");
		else this.name.setText(name);
	}

	@Override public void add(Component comp, Object constraints)
	{	Rectangle r = XGLayout.constraintsObjectToRectangle(constraints);
		if(this.getName() != null)
		{	r.y++;
			super.remove(this.name);
			super.add(this.name, new int[]{0,0,r.x + r.width,1});
		} 
		super.add(comp, r);
	}
}
