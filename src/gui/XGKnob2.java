package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.UIManager;
import adress.XGAddress;
import adress.XGAddressableSet;
import value.XGValue;
import xml.XMLNode;

public class XGKnob2 extends JComponent implements XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final static float START = 225;
	private final static float LENGTH = 270;
	private final static float PI = (float) 3.1415;
	private final static float START_ANG = (START/360)*PI*2;
	private final static float LENGTH_ANG = (LENGTH/360)*PI*2;
	private final static Color DEFAULT_FOCUS_COLOR = UIManager.getColor("Focus.color");

/*****************************************************************************************************************************/

	private final XGValue value;
	private final String tag;

	public XGKnob2(XMLNode n, XGAddressableSet<XGValue> set)
	{	this.tag = n.getStringAttribute(ATTR_ID);
		this.value = set.getFirstValid(this.address);
	}

	@Override public void paint(Graphics g)
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int size = Math.min(width, height);
		int middle = size/2;

		if (g instanceof Graphics2D)
		{	Graphics2D g2d = (Graphics2D) g;
			g2d.setBackground(getParent().getBackground());
			g2d.addRenderingHints(AALIAS);
		
// For the size of the "mouse click" area
//			hitArc.setFrame(0, 0, size, size);
		}
	
// Paint the "markers"
		for (float a2 = START_ANG; a2 >= START_ANG - LENGTH_ANG; a2=a2 -(float)(LENGTH_ANG/10.01))
		{	int x = 10 + middle + (int)((6+middle) * Math.cos(a2));
			int y = 10 + middle - (int)((6+middle) * Math.sin(a2));
			g.drawLine(10 + size/2, 10 + size/2, x, y);
			
		}
	
		g.setColor(Color.gray);
		g.drawArc(0, 0, size, size, 315, 270);
		
		int x = middle + (int)(middle * Math.cos(START_ANG - (float) LENGTH_ANG * this.value.getContent()));
		int y = middle - (int)(middle * Math.sin(START_ANG - (float) LENGTH_ANG * this.value.getContent()));
	
		g.setColor(Color.blue);
		g.drawArc(0, 0, size, size, 225, -270);
		
		g.setColor(Color.black);
		int dx = (int)(2 * Math.sin(START_ANG - (float) LENGTH_ANG * this.value.getContent()));
		int dy = (int)(2 * Math.cos(START_ANG - (float) LENGTH_ANG * this.value.getContent()));
		g.drawLine(dx + middle, dy + middle, x, y);
		}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
