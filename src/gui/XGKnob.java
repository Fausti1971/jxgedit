package gui;

import static application.XGLoggable.log;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import application.JXG;
import application.Rest;
import device.XGDevice;
import module.XGModule;
import msg.XGMessageParameterChange;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int PREF_W = 66, PREF_H = 88;

/*****************************************************************************************************************************/

	private final XGKnobBar bar;
	private final XGValueLabel label;

	public XGKnob(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.setSizes(PREF_W, PREF_H);
		this.addMouseListener(this);
		this.addFocusListener(this);
		this.value.addParameterListener(this);
		this.value.addValueListener(this);

		this.bar = new XGKnobBar(this.value);
		this.addGB(this.bar, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.NORTH, new Insets(0,0,2,0), 0, 0);

		this.label = new XGValueLabel(this.value);
		this.addGB(this.label, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0.5, 0, GridBagConstraints.SOUTH, new Insets(1,1,0,1), 0, 0);

		this.parameterChanged(this.value.getParameter());
		log.info("knob initialized: " + this.getName());
		}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
		this.label.repaint();
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setName(p.getShortName());
			this.setToolTipText(p.getLongName());
			this.bar.setEnabled(true);
			this.label.setText(this.value.toString());
			this.label.setEnabled(true);
			this.setEnabled(true);
		}
		else
		{	this.setName("n/a");
			this.setToolTipText("no parameter");
			this.bar.setEnabled(false);
			this.label.setEnabled(false);
			this.setEnabled(false);
		}
		this.borderize();
	}

	@Override protected void paintComponent(Graphics g)
	{	if(this.isEnabled()) super.paintComponent(g);
	}

	private class XGKnobBar extends JComponent implements GuiConstants, MouseListener, MouseMotionListener, MouseWheelListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/**************************************************************************************************************/

		private final XGValue value;
		private XGParameter parameter;
		private int size, radius, lengthArc, originArc;
		private Point middle = new Point(), strokeStart = new Point(), strokeEnd = new Point();

		private XGKnobBar(XGValue v)
		{	this.value = v;
			this.setBorder(null);
			this.size = Math.min(this.getWidth(), this.getHeight()) - 2 * DEF_STROKEWIDTH;
			this.setSize(size, size);
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public void paintComponent(Graphics g)
		{	if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(XGComponent.AALIAS);
			this.size = Math.min(this.getWidth() - DEF_STROKEWIDTH, this.getHeight());
			this.radius = this.size / 2;
			this.middle.x = this.getWidth() / 2;
			this.middle.y = 4 + this.radius;// getY() liefert IMMER 15! (sowohl mit als auch ohen Border), daher die "4"
	
	// paint background arc
			g2.setColor(COL_BAR_BACK);
			g2.setStroke(DEF_ARCSTROKE);
			g2.drawArc(this.middle.x - this.radius, this.middle.y - this.radius, this.size, this.size, START_ARC, LENGTH_ARC);
	// paint foreground arc
			this.parameter = this.value.getParameter();
			this.originArc = Rest.linearIO(parameter.getOrigin(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, LENGTH_ARC);//originArc(mitte (64)) = -135 => START_ARC + originArc = 90
			this.lengthArc = Rest.linearIO(this.value.getContent(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, LENGTH_ARC);//falscher winkel - aber richtige kreisbogenlänge (beim malen korrigieren)
			g2.setColor(COL_BAR_FORE);
			g2.drawArc(this.middle.x - this.radius, this.middle.y - this.radius, this.size, this.size, this.originArc + START_ARC, this.lengthArc - originArc);
	// paint marker
			double endRad = Math.toRadians(this.lengthArc + START_ARC);
			strokeStart.x = (int)(middle.x + radius * Math.cos(endRad));
			strokeStart.y = (int)(middle.y - radius * Math.sin(endRad));
			strokeEnd.x = (int)(middle.x + radius/2 * Math.cos(endRad));
			strokeEnd.y = (int)(middle.y - radius/2 * Math.sin(endRad));
			g2.drawLine(strokeStart.x, strokeStart.y, strokeEnd.x, strokeEnd.y);
			g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{
		}

		@Override public void mousePressed(MouseEvent e)
		{	JXG.dragEvent = e;
			e.consume();
		}

		@Override public void mouseReleased(MouseEvent e)
		{	JXG.dragEvent = e;
			e.consume();
		}

		@Override public void mouseEntered(MouseEvent e)
		{
		}

		@Override public void mouseExited(MouseEvent e)
		{
		}

		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	boolean changed = this.value.addContent(e.getWheelRotation());
			if(changed)
			{	XGDevice dev = this.value.getSource().getDevice();
				try
				{	new XGMessageParameterChange(dev, dev.getMidi(), this.value).transmit();
				}
				catch(InvalidXGAddressException | InvalidMidiDataException e1)
				{	e1.printStackTrace();
				}
			}
			e.consume();
		}

		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - JXG.dragEvent.getX();
			boolean changed = this.value.addContent(distance);
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
	}
}
