package gui;

import application.XGStrings;import xml.XMLNode;import java.awt.*;
import javax.swing.*;import javax.swing.border.Border;import javax.swing.border.TitledBorder;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

	private static Border NAMELESS_BORDER = BorderFactory.createRaisedSoftBevelBorder();

	public static XGFrame newFrame(XMLNode xml)
	{	Rectangle r = new Rectangle(0,0,1,1);
		if(xml.hasAttribute(ATTR_CONSTRAINT)) r = XGStrings.toRectangle(xml.getStringAttribute(ATTR_CONSTRAINT));
		return new XGFrame(xml.getStringAttribute(ATTR_LABEL), r.getSize());
	}

/********************************************************************************************************************/

/**
* erzeugt ein JPanel mit einem XGLayout mit den übergebenen Grid-Dimensionen, optional mit Label: label==null -> no border, label=="" -> nameless border, label=="..." -> named border "..."
*/
	public XGFrame(String label, int columns, int rows)
	{	this.setLayout(new XGLayout(new Dimension(columns, rows)));
		this.setName(label);
//		if(border) this.setBorder(NAMELESS_BORDER);
	//	this.setBorder(this);
	}

	public XGFrame(String label, Dimension dim)
	{	this(label, dim.width, dim.height);
	}

	public XGFrame(String label)
	{	this(label, 1, 1);
	}

//TODO: Konstruktoren ab hier wieder löschen wenn die XGEditWindows nicht mehr "gebraucht" werden
	XGFrame(boolean temp)
	{	this((String)null);
	}

	XGFrame(Dimension temp)
	{	this(null, temp);
	}

	XGFrame(int rows, int cols, boolean temp)
	{	this("", new Dimension(cols, rows));
	}

	@Override public void setName(String name)
	{	super.setName(name);
		if(name == null)
		{	this.setBorder(null);
			return;
		}
		if(name.isEmpty())
		{	this.setBorder(NAMELESS_BORDER);
			return;
		}
		else this.setBorder(BorderFactory.createTitledBorder(NAMELESS_BORDER, name, TitledBorder.CENTER, TitledBorder.BELOW_TOP));
	}
}
