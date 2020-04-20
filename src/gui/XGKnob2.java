package gui;

import static application.XGLoggable.log;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import adress.XGAddress;
import adress.XGAddressableSet;
import application.XGLoggable;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob2 extends JPanel implements XGLoggable, XGBorderable, XGValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final XGValue DEF_VALUE = new XGValue("n/a", 0);
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
	private final XGAddress address;
	private int axis = 0;

	public XGKnob2(XMLNode n, XGAddressableSet<XGValue> set)
	{	this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), null);
		this.value = set.getFirstValidOrDefault(this.address, DEF_VALUE);
		this.axis = XGComponent.getAxis(n.getStringAttribute(ATTR_AXIS));
		this.setName(this.value.getParameter().getShortName());
		this.setToolTipText(this.value.getParameter().getLongName());
		this.setMinimumSize(PREF_SIZE);
		this.setPreferredSize(PREF_SIZE);
		this.borderize();
		this.value.addListener(this);
		log.info("knob initialized: " + this.getName());
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
