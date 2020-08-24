package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGSlider extends XGFrame implements KeyListener, XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final XGAddress address;
	private final XGValue value;
	private final XGSliderBar bar;
	private final XGValueLabel label;

	public XGSlider(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n, mod);
		this.setLayout(new GridBagLayout());
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getDevice().getValues().getFirstIncluded(this.address);

		if(this.isEnabled())
		{	this.setToolTipText(null);
			this.setFocusable(true);
		}
//		this.setName(this.value.getParameter().getShortName());
		this.borderize();
		this.addMouseListener(this);
		this.addFocusListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);
		this.bar = new XGSliderBar(this.value);
		this.add(this.bar, gbc);

		this.label = new XGValueLabel(this.value);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(this.label, gbc);

		this.logInitSuccess();
	}

	@Override public void paint(Graphics g)
	{	if(this.isEnabled()) super.paint(g);
	}

	@Override public String getName()
	{	return this.value.getParameter().getShortName();
	}

	@Override public String getToolTipText()
		{	return this.value.getParameter().getLongName();
		}

	@Override public boolean isEnabled()
	{	return super.isEnabled() && this.value != null && this.value.getParameter() != null;
	}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
	}

	@Override public void keyTyped(KeyEvent e)
	{
	}

	@Override public void keyPressed(KeyEvent e)
	{
	}

	@Override public void keyReleased(KeyEvent e)
	{
	}

	@Override public boolean isManagingFocus()
	{	return true;
	}
	
	@Override public boolean isFocusTraversable()
	{	return true;
	}


	private class XGSliderBar extends JComponent implements XGValueChangeListener, MouseMotionListener, MouseWheelListener, MouseListener
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private final XGValue value;
		private XGParameter parameter;
		private int barWidth, originWidth;
		private Graphics2D g2;
//		private Cursor lastCursor;

		private XGSliderBar(XGValue v)
		{	//super();
			//this.setBorder(null);	buggy: getX() liefert IMMER inset.left; getY() IMMER inset.top; (5, 15)! Bug?; deshalb beim malen diese koordinaten ignorieren...
			this.value = v;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.value.addValueListener(this);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public boolean isEnabled()
		{	return super.isEnabled() && this.value != null && this.value.getParameter() != null;
		}

		@Override protected void paintComponent(Graphics g)
		{	if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
			this.g2 = (Graphics2D)g.create();
			this.g2.addRenderingHints(AALIAS);
			this.parameter = this.value.getParameter();
	// draw background
			this.g2.setColor(COL_BAR_BACK);
			this.g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
	// draw foreground
			this.originWidth = XGMath.linearIO(this.parameter.getOrigin(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, this.getWidth());
			this.barWidth = XGMath.linearIO(this.value.getIndex(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, this.getWidth()) - this.originWidth;
			this.g2.setColor(COL_BAR_FORE);
			this.g2.fillRoundRect(0 + Math.min(this.originWidth, this.originWidth + this.barWidth), 0, Math.abs(this.barWidth), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			this.g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	boolean changed = false;
			if(e.getButton() != MouseEvent.BUTTON1) return;
			if(this.getX() + this.barWidth < e.getX()) changed = this.value.setIndex(this.value.getIndex() + 1);
			else changed = this.value.setIndex(this.value.getIndex() - 1);
			if(changed) this.value.transmit();
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	boolean changed = this.value.setIndex(this.value.getIndex() + e.getWheelRotation());
			if(changed) this.value.transmit();
			e.consume();
		}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGComponent.dragEvent.getX();
			boolean changed = this.value.setIndex(this.value.getIndex() + distance);
			if(changed) this.value.transmit();
			XGComponent.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e)
		{
		}

		@Override public void contentChanged(XGValue v)
		{	this.repaint();
		}

		@Override public void mousePressed(MouseEvent e)
		{	XGComponent.dragEvent = e;
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGComponent.dragEvent = e;
		}


		@Override public void mouseEntered(MouseEvent e)
		{
		}

		@Override public void mouseExited(MouseEvent e)
		{	
		}
	}
}
