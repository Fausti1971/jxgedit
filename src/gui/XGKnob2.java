package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import adress.XGAddressableSet;
import value.XGFixedValue;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob2 extends JPanel implements XGBorderable, XGValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getAnonymousLogger();
	private final static RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final static float START = 225;
	private final static float LENGTH = 270;
	private final static float PI = (float) 3.1415;
	private final static float START_ANG = (START/360)*PI*2;
	private final static float LENGTH_ANG = (LENGTH/360)*PI*2;
	private final static Color DEFAULT_FOCUS_COLOR = UIManager.getColor("Focus.color");
//	private final static Dimension MIN_SIZE = new Dimension(40, 40);
	private final static Dimension PREF_SIZE = new Dimension(64, 80);

/*****************************************************************************************************************************/

	private final XGValue value;
	private int axis = 0;

	public XGKnob2(XMLNode n, XGAddressableSet<XGValue> set)
	{
		String s = null;
		if(n.hasAttribute(ATTR_Y))
		{	s = n.getStringAttribute(ATTR_Y);
			this.axis = DIR_X;
		}
		if(n.hasAttribute(ATTR_X))
		{	s = n.getStringAttribute(ATTR_X);
			this.axis |= DIR_Y;
		}
		XGValue val = null;
		if(axis == 0) this.setEnabled(false);
		for(XGValue v : set)
		{	if(v.getTag().equals(s))
			{	val = v;
				break;
			}
		}
		if(val == null) val = new XGFixedValue(s, 0);
		this.value = val;
		this.setName(this.value.getOpcode().getParameter(0).getShortName());
		this.setToolTipText(this.value.getOpcode().getParameter(0).getLongName());
		this.setMinimumSize(PREF_SIZE);
		this.setPreferredSize(PREF_SIZE);
		this.borderize();
		log.info("control " + s + " initialized: " + this.getName());
		}

	@Override public void paint(Graphics g)
	{	super.paint(g);
		if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
		Graphics2D g2 = (Graphics2D)g;
		g2.addRenderingHints(AALIAS);
		Insets ins = this.getInsets();
		Rectangle area = new Rectangle(ins.left, ins.top, this.getWidth() - ins.left - ins.right, this.getHeight() - ins.top - ins.bottom);
		int diameter = Math.min(area.width, area.height);
		int x = area.x + (area.width - diameter)/2;
//		int y = area.y + (area.height - diameter)/2;

		g2.setColor(COL_BORDER);
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.drawArc(x, area.y, diameter, diameter, 225, -270);

		String s = this.value.toString();
		g2.setColor(COL_NODETEXT);
		int tx = g2.getFontMetrics().stringWidth(s);
		g2.drawString(s, area.x + area.width/2 - tx/2, area.y + area.height);
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}
}
