package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import application.XGMath;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;

public class XGKnob extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{
	private static final long serialVersionUID = 1L;
	public enum KnobBehavior {HORIZONTAL, VERTICAL, RADIAL};

/*****************************************************************************************************************************/

	private final XGKnobBar bar;
	private final XGValueLabel label;
	private final XGValue value;

	public XGKnob(XGValue val)
	{	super("");
		this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			this.bar = null;
			this.label = null;
			return;
		}
		if(this.value.getType().hasMutableParameters()) this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);

		this.bar = new XGKnobBar(this);
		this.add(this.bar, "0,0,1,4");

		this.label = new XGValueLabel(this.value);
		this.add(this.label, "0,4,1,1");

		this.parameterChanged(this.value.getParameter());
		}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getShortName());
		this.setToolTipText(p.getName());
		this.label.setText(this.value.toString());
		this.setVisible(p.isValid());
		this.setEnabled(p.isValid());
	}

/******************************************************************************************************************************************/

	private class XGKnobBar extends JComponent implements XGComponent, MouseMotionListener, MouseWheelListener
	{
		private static final long serialVersionUID = 1L;

/*****************************************************************************************/

		private final XGKnob knob;
		private final Point middle = new Point(), strokeStart = new Point(), strokeEnd = new Point();
		private XGParameter parm;
		private double lastAng;

		private XGKnobBar(XGKnob knob)
		{	this.knob = knob;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			int size = Math.min(this.getWidth(), this.getHeight());
			int radius = size / 2 - DEF_STROKEWIDTH;
			this.middle.x = this.getWidth() >> 1;
			this.middle.y = (int)(this.getHeight() / 1.7854f);

	// paint background arc
			g2.setColor(COL_BAR_BACK);
			g2.setStroke(DEF_ARCSTROKE);
			g2.drawArc(this.middle.x - radius, this.middle.y - radius, 2 * radius, 2 * radius, START_ARC, LENGTH_ARC);
	// paint foreground arc
			parm = this.knob.value.getParameter();
			int originArc = XGMath.linearIO(parm.getOriginIndex(), parm.getMinIndex(), parm.getMaxIndex(), 0, LENGTH_ARC);//originArc(mitte (64)) = -135 => START_ARC + originArc = 90
			int lengthArc = XGMath.linearIO(this.knob.value.getIndex(), parm.getMinIndex(), parm.getMaxIndex(), 0, LENGTH_ARC);//falscher winkel - aber richtige kreisbogenlänge (beim malen korrigieren)
			g2.setColor(COL_BAR_FORE);
			g2.drawArc(this.middle.x - radius, this.middle.y - radius, 2 * radius, 2 * radius, originArc + START_ARC, lengthArc - originArc);
	// paint marker
			double endRad = Math.toRadians(lengthArc + START_ARC);
			this.strokeStart.x = (int)(this.middle.x + radius * Math.cos(endRad));
			this.strokeStart.y = (int)(this.middle.y - radius * Math.sin(endRad));
			this.strokeEnd.x = (int)(this.middle.x + radius/2 * Math.cos(endRad));
			this.strokeEnd.y = (int)(this.middle.y - radius/2 * Math.sin(endRad));
			g2.drawLine(this.strokeStart.x, this.strokeStart.y, this.strokeEnd.x, this.strokeEnd.y);
			g2.dispose();
		}

		private int getMouseDistance(MouseEvent e)
		{	int result = this.knob.value.getIndex();
			switch(ENVIRONMENT.knobBehavior)
			{	case HORIZONTAL:
					result += e.getX() - XGUI.ENVIRONMENT.dragEvent.getX();
					break;
				case VERTICAL:
					result += XGUI.ENVIRONMENT.dragEvent.getY() - e.getY();
					break;
				case RADIAL:
					double ang = Math.atan2(e.getX() - this.middle.x, e.getY() - this.middle.y);
					ang = Math.toDegrees(ang);
					if (ang < 0) ang += 360;
					result = XGMath.linearIO(Math.round(Math.round(ang)), 315, 45, this.parm.getMinIndex(), parm.getMaxIndex());
					break;
			}
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
			return result;
		}

		@Override public void mouseWheelMoved(MouseWheelEvent e){	this.knob.value.addIndex(XGUI.getWheelRotation(e), true);}

		@Override public void mouseDragged(MouseEvent e){	this.knob.value.setIndex(this.getMouseDistance(e), true, true);}

		@Override public void mouseMoved(MouseEvent e){}

		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}
}
