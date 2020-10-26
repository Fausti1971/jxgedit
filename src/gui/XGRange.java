package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import application.XGMath;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGRange extends JPanel implements XGComponent, XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*******************************************************************************************************/

	private final XMLNode config;
	private final XGRangeBar bar;
	private final XGRangeLabel label;
	private final XGValue loValue, hiValue;
	private final XGAddress loAddress, hiAddress;

	public XGRange(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	this.config = n;
		this.setBounds();
		this.setLayout(new GridBagLayout());
		this.loAddress = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_LO), mod.getAddress());
		this.hiAddress = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_HI), mod.getAddress());
		this.loValue = mod.getType().getDevice().getValues().getFirstIncluded(this.loAddress);
		this.hiValue = mod.getType().getDevice().getValues().getFirstIncluded(this.hiAddress);
		this.loValue.addValueListener(this);
		this.hiValue.addValueListener(this);

		if(this.isEnabled())
		{	this.setToolTipText(null);
			this.setFocusable(true);
		}
		this.setName(n.getStringAttribute(ATTR_NAME));
		this.borderize();
		this.addMouseListener(this);
		this.addFocusListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);
		this.bar = new XGRangeBar(this.loValue, this.hiValue);
		this.add(this.bar, gbc);

		this.label = new XGRangeLabel(this.loValue, this.hiValue);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(this.label, gbc);

		boolean ena = this.isEnabled();
		this.bar.setEnabled(ena);
		this.label.setEnabled(ena);

//		this.logInitSuccess();
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void paint(Graphics g)
	{	if(this.isEnabled()) super.paint(g);
	}

	@Override public String getToolTipText()
		{	return this.loValue.getValue() + "..." + this.hiValue.getValue();
		}

	@Override public boolean isEnabled()
	{	return super.isEnabled() && this.loValue != null && this.loValue.getParameter() != null  && this.hiValue != null && this.hiValue.getParameter() != null;
	}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(this.loValue + "..." + this.hiValue);
	}

	@Override public boolean isManagingFocus()
	{	return true;
	}
	
	@Override public boolean isFocusTraversable()
	{	return true;
	}

/***************************************************************************************************/

	private class XGRangeBar extends JComponent implements XGValueChangeListener, MouseMotionListener, MouseWheelListener, MouseListener
	{
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private final XGValue loValue, hiValue;
		private XGValue curValue;
		private XGParameter loParameter, hiParameter;
		private int loX, hiX, curX;
		private Graphics2D g2;
//		private Cursor lastCursor;

		private XGRangeBar(XGValue lo, XGValue hi)
		{	//super();
			//this.setBorder(null);	buggy: getX() liefert IMMER inset.left; getY() IMMER inset.top; (5, 15)! Bug?; deshalb beim malen diese koordinaten ignorieren...
			this.loValue = lo;
			this.hiValue = hi;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.loValue.addValueListener(this);
			this.hiValue.addValueListener(this);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		private void limitize()
		{	int min = this.loValue.getIndex();
			int max = this.hiValue.getIndex();
			this.loValue.setIndex(Math.min(min, max));
			this.hiValue.setIndex(Math.max(min, max));
		}

		private void setCurrent(int x)
		{	if(Math.abs(x - this.loX) < Math.abs(x - this.hiX))
			{	this.curX = this.loX;
				this.curValue = this.loValue;
			}
			else
			{	this.curX = this.hiX;
				this.curValue = this.hiValue;
			}
		}

		@Override public boolean isEnabled()
		{	return super.isEnabled() && this.loValue != null && this.loValue.getParameter() != null && this.hiValue != null && this.hiValue.getParameter() != null;
		}

		@Override protected void paintComponent(Graphics g)
		{	if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
			this.g2 = (Graphics2D)g.create();
			this.g2.addRenderingHints(AALIAS);
			this.loParameter = this.loValue.getParameter();
			this.hiParameter = this.hiValue.getParameter();
	// draw background
			this.g2.setColor(this.getBackground().brighter());
			this.g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
	// draw foreground
			this.loX = XGMath.linearIO(this.loValue.getIndex(), this.loParameter.getMinIndex(), this.loParameter.getMaxIndex(), 0, this.getWidth());
			this.hiX = XGMath.linearIO(this.hiValue.getIndex(), this.hiParameter.getMinIndex(), this.hiParameter.getMaxIndex(), 0, this.getWidth());
//			this.originWidth = XGMath.linearIO(this.loParameter.getOrigin(), this.loParameter.getMinIndex(), this.loParameter.getMaxIndex(), 0, this.getWidth());
			this.g2.setColor(COL_BAR_FORE);
			this.g2.fillRoundRect(this.loX, 0, this.hiX - this.loX, this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			this.g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	boolean changed = false;
			if(e.getButton() != MouseEvent.BUTTON1) return;
			this.setCurrent(e.getX());
			if(e.getX() > this.curX)this.curValue.addIndex(1);
			else this.curValue.addIndex(-1);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	this.loValue.addIndex(e.getWheelRotation());
			this.hiValue.addIndex(e.getWheelRotation());
			e.consume();
		}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGComponent.GLOBALS.dragEvent.getX();
			this.curValue.addIndex(distance);
			XGComponent.GLOBALS.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e)
		{
		}

		@Override public void contentChanged(XGValue v)
		{	this.repaint();
		}

		@Override public void mousePressed(MouseEvent e)
		{	XGComponent.GLOBALS.dragEvent = e;
			this.setCurrent(e.getX());
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGComponent.GLOBALS.dragEvent = e;
			this.setCurrent(e.getX());
		}


		@Override public void mouseEntered(MouseEvent e)
		{
		}

		@Override public void mouseExited(MouseEvent e)
		{	
		}
	}
}
