package gui;

import static application.XGLoggable.log;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import application.JXG;
import application.Rest;
import device.XGDevice;
import msg.XGMessageParameterChange;
import parm.XGParameter;
import value.ChangeableContent;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob extends JComponent implements XGComponent, XGValueChangeListener, MouseMotionListener, MouseWheelListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	private final static BasicStroke DEF_STROKE = new BasicStroke(2f);

	private final static int START_ARC = 225;
	private final static int END_ARC = 315;
	private final static int LENGTH_ARC = -270;
	private final static int PREF_W = 2, PREF_H = 2;

//	private static float toRadiant(int ang)
//	{	return PI * ang/180;
//	}

/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGAddress address;
	private final XMLNode config;
	private XGParameter parameter;
	private int lengthArc;
	private int originArc;
	private String valueString;
	private final Rectangle knobArea = new Rectangle();
	private final Rectangle valueArea = new Rectangle();
	private final Point middle = new Point();
	private final Point strokeStart = new Point();
	private final Point strokeEnd = new Point();

	public XGKnob(XMLNode n, XGAddressableSet<XGValue> set)
	{	this.config = n;
		this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), null);
		XGValue v = set.getFirstValid(this.address);
		this.setEnabled(true);
		if(v == null)
		{	v = DEF_VALUE;
			this.setEnabled(false);
		}
		this.value = v;
		this.parameter = this.value.getParameter();
		this.value.addListener(this);
		this.setName(this.value.getParameter().getShortName());
		this.setToolTipText(this.value.getParameter().getLongName());
		this.setSizes(PREF_W, PREF_H);
		this.setFocusable(true);
		this.borderize();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addFocusListener(this);
		log.info("knob initialized: " + this.getName());
		}

	@Override public void paintComponent(Graphics g)
	{	if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
		Graphics2D g2 = (Graphics2D)g.create();
		g2.addRenderingHints(AALIAS);
		Insets ins = this.getInsets();
		int w_gap = ins.left + ins.right + DEF_STROKEWIDTH;
		int h_gap = ins.top + ins.bottom + DEF_STROKEWIDTH;
		knobArea.x = valueArea.x = ins.left;
		knobArea.y = ins.top;
		knobArea.width = knobArea.height = Math.min(this.getWidth() - w_gap, this.getHeight() - h_gap);
		valueArea.y = ins.top + knobArea.height;
		valueArea.height = g2.getFontMetrics(FONT).getHeight();
		valueArea.width = this.getWidth() - (ins.left - ins.right);
		int radius = knobArea.width / 2;
		middle.x = this.getWidth() / 2;
		middle.y = knobArea.y + radius;

// paint background arc
		g2.setColor(COL_BAR_BACK);
		g2.setStroke(DEF_ARCSTROKE);
		g2.drawArc(middle.x - radius, middle.y - radius, knobArea.width, knobArea.height, START_ARC, LENGTH_ARC);
// paint foreground arc
		this.parameter = this.value.getParameter();
		this.originArc = Rest.linearIO(parameter.getOrigin(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, LENGTH_ARC);//originArc(mitte (64)) = -135 => START_ARC + originArc = 90
		this.lengthArc = Rest.linearIO(this.value.getContent(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, LENGTH_ARC);//falscher winkel - aber richtige kreisbogenlänge (beim malen korrigieren)
		g2.setColor(COL_BAR_FORE);
		g2.drawArc(middle.x - radius, middle.y - radius, knobArea.width, knobArea.height, originArc + START_ARC, this.lengthArc - originArc);
// paint marker
		g2.setStroke(DEF_STROKE);
		double endRad = Math.toRadians(this.lengthArc + START_ARC);
		strokeStart.x = (int)(middle.x + radius * Math.cos(endRad));
		strokeStart.y = (int)(middle.y - radius * Math.sin(endRad));
		strokeEnd.x = (int)(middle.x + radius/2 * Math.cos(endRad));
		strokeEnd.y = (int)(middle.y - radius/2 * Math.sin(endRad));
		g2.drawLine(strokeStart.x, strokeStart.y, strokeEnd.x, strokeEnd.y);
// paint value
		this.valueString = this.value.toString();
		g2.setColor(COL_NODE_TEXT);
		g2.setFont(FONT);
		g2.drawString(this.valueString, (int)(middle.x - g2.getFontMetrics().stringWidth(this.valueString) / 2), valueArea.y + valueArea.height / 2);
		g2.dispose();
	}

	@Override public void mouseDragged(MouseEvent e)
	{	int distance = e.getX() - JXG.dragEvent.getX();
		boolean changed = this.value.setContent(this.value.getContent() + distance);
		if(changed)
		{	XGDevice dev = this.value.getSource().getDevice();
			try
			{	new XGMessageParameterChange(dev, dev.getMidi(), this.value).transmit();
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e1)
			{	e1.printStackTrace();
			}
		}
		JXG.dragEvent = e;
		e.consume();
	}

	@Override public void mouseMoved(MouseEvent e)
	{
	}

	@Override public void mouseWheelMoved(MouseWheelEvent e)
	{	ChangeableContent<Integer> v = this.value;
		boolean changed = v.setContent(v.getContent() + e.getWheelRotation());
		if(v instanceof XGValue && changed)
		{	XGValue x = (XGValue)v;
			XGDevice dev = x.getSource().getDevice();
			try
			{	new XGMessageParameterChange(dev, dev.getMidi(), x).transmit();
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e1)
			{	e1.printStackTrace();
			}
		}
		e.consume();
	}


	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}

	@Override public boolean isManagingFocus()
	{	return true;
	}
	
	@Override public boolean isFocusTraversable()
	{	return true;
	}
}
