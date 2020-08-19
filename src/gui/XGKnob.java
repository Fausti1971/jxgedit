package gui;

import static application.XGLoggable.LOG;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import application.JXG;
import application.XGMath;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob extends XGComponent implements XGParameterChangeListener, XGValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	private final XGKnobBar bar;
	private final XGValueLabel label;

	public XGKnob(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.setLayout(new GridBagLayout());
		this.addMouseListener(this);
		this.addFocusListener(this);
		this.value.addParameterListener(this);
		this.value.addValueListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);
		this.bar = new XGKnobBar(this.value);
		this.add(this.bar, gbc);

		this.label = new XGValueLabel(this.value);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.label, gbc);

		this.parameterChanged(this.value.getParameter());
		LOG.info("knob initialized: " + this.getName());
		}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
//		this.label.repaint();
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
			this.originArc = XGMath.linearIO(parameter.getOrigin(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, LENGTH_ARC);//originArc(mitte (64)) = -135 => START_ARC + originArc = 90
			this.lengthArc = XGMath.linearIO(this.value.getContent(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, LENGTH_ARC);//falscher winkel - aber richtige kreisbogenlänge (beim malen korrigieren)
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
		{	boolean changed = this.value.setContent(this.value.getContent() + e.getWheelRotation());
			if(changed) this.value.transmit();
			e.consume();
		}

		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - JXG.dragEvent.getX();
			boolean changed = this.value.setContent(this.value.getContent() + distance);
			if(changed) this.value.transmit();
			JXG.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e)
		{
		}
	}
}
