package gui;

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
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import application.XGMath;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGKnob extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	private final XGKnobBar bar;
	private final XGValueLabel label;
	private final XGValue value;
	private final XGAddress address;

	public XGKnob(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n);

		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getValues().get(this.address);
		if(this.value.getOpcode().isMutable()) this.value.addParameterListener(this);
		this.value.addValueListener(this);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);

		this.bar = new XGKnobBar(this.value);
		this.add(this.bar, gbc);

		this.label = new XGValueLabel(this.value);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.label, gbc);

		this.addMouseListener(this);
		this.addFocusListener(this);
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
		this.setVisible(p != XGParameter.NO_PARAMETER);
		this.setEnabled(p.isValid());
		this.borderize();
	}

/******************************************************************************************************************************************/

	private class XGKnobBar extends JComponent implements XGUI, MouseListener, MouseMotionListener, MouseWheelListener
	{
		private static final long serialVersionUID = 1L;

/*****************************************************************************************/

		private final XGValue value;
		private XGParameter parameter;
		private int size, radius, lengthArc, originArc;
		private Point middle = new Point(), strokeStart = new Point(), strokeEnd = new Point();
		private Graphics2D g2;

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
		{
			this.g2 = (Graphics2D)g.create();
			this.g2.addRenderingHints(AALIAS);
			this.size = Math.min(this.getWidth() - DEF_STROKEWIDTH, this.getHeight());
			this.radius = this.size / 2;
			this.middle.x = this.getWidth() / 2;
			this.middle.y = 4 + this.radius;// getY() liefert IMMER 15! (sowohl mit als auch ohen Border), daher die "4"
	
	// paint background arc
			//this.g2.setColor(COL_BAR_BACK);
			this.g2.setColor(new XGColor(this.getBackground()).add(COL_STEP, 0));
			this.g2.setStroke(DEF_ARCSTROKE);
			this.g2.drawArc(this.middle.x - this.radius, this.middle.y - this.radius, this.size, this.size, START_ARC, LENGTH_ARC);
	// paint foreground arc
			this.parameter = this.value.getParameter();
			this.originArc = XGMath.linearIO(this.parameter.getOriginIndex(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, LENGTH_ARC);//originArc(mitte (64)) = -135 => START_ARC + originArc = 90
			this.lengthArc = XGMath.linearIO(this.value.getIndex(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, LENGTH_ARC);//falscher winkel - aber richtige kreisbogenlänge (beim malen korrigieren)
			this.g2.setColor(COL_BAR_FORE);
			this.g2.drawArc(this.middle.x - this.radius, this.middle.y - this.radius, this.size, this.size, this.originArc + START_ARC, this.lengthArc - this.originArc);
	// paint marker
			double endRad = Math.toRadians(this.lengthArc + START_ARC);
			this.strokeStart.x = (int)(this.middle.x + this.radius * Math.cos(endRad));
			this.strokeStart.y = (int)(this.middle.y - this.radius * Math.sin(endRad));
			this.strokeEnd.x = (int)(this.middle.x + this.radius/2 * Math.cos(endRad));
			this.strokeEnd.y = (int)(this.middle.y - this.radius/2 * Math.sin(endRad));
			this.g2.drawLine(this.strokeStart.x, this.strokeStart.y, this.strokeEnd.x, this.strokeEnd.y);
			this.g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{
		}

		@Override public void mousePressed(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
			e.consume();
		}

		@Override public void mouseReleased(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
			e.consume();
		}

		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	this.value.editIndex(this.value.getIndex() + e.getWheelRotation());
			e.consume();
		}

		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGUI.VARIABLES.dragEvent.getX();
			this.value.addIndex(distance);
			XGUI.VARIABLES.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e)
		{
		}

		@Override public void mouseEntered(MouseEvent e)
		{
		}

		@Override public void mouseExited(MouseEvent e)
		{
		}
	}
}
