package gui;

import application.XGMath;import application.XGStrings;import java.awt.*;
import javax.swing.*;import javax.swing.border.BevelBorder;import javax.swing.border.Border;import javax.swing.border.TitledBorder;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	private JLabel name = null;

/**
* erzeugt ein JPanel mit einem XGLayout mit den übergebenen Grid-Dimensionen, optional mit Label an Position "0,0,w++,1"
*/
	public XGFrame(int columns, int rows)
	{	this.setLayout(new XGLayout(new Dimension(columns, rows)));
		this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	//	this.setBorder(this);
	}

	public XGFrame(Dimension dim)
	{	this(dim.width, dim.height);
	}

	public XGFrame()
	{	this(1, 1);
	}

	public XGFrame(String text)
	{	this();
		this.addNameLabel(text);
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
