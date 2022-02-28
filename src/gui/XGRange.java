package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.JPanel;
import application.XGMath;
import application.XGStrings;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;

public class XGRange extends JPanel implements XGParameterConstants, XGValueChangeListener, MouseListener, FocusListener, XGComponent
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*******************************************************************************************************/

	private final XGRangeBar bar;
	private final XGRangeLabel label;
	private final XGValue loValue, hiValue;

	public XGRange(XGValue lo, XGValue hi)
	{
		this.setLayout(new GridBagLayout());
		this.loValue = lo;
		this.hiValue = hi;
		if(this.loValue == null || this.hiValue == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.bar = null;
			this.label = null;
			return;
		}
		this.loValue.getValueListeners().add(this);
		this.hiValue.getValueListeners().add(this);

		this.setName(XGStrings.commonString(this.loValue.getParameter().getName(), this.hiValue.getParameter().getName()));
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

	@Override public String getToolTipText()
		{	return this.loValue.getValue() + "..." + this.hiValue.getValue();
		}

	@Override public boolean isVisible()
	{	return this.loValue != null && this.loValue.getParameter() != null  && this.hiValue != null && this.hiValue.getParameter() != null;
	}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(this.loValue + "..." + this.hiValue);
	}

	public void focusGained(FocusEvent event)
	{
	}

	public void focusLost(FocusEvent event)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
	}

	public void mousePressed(MouseEvent event)
	{
	}

	public void mouseReleased(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}


/***************************************************************************************************/

	private class XGRangeBar extends JPanel implements XGValueChangeListener, MouseMotionListener, MouseWheelListener, MouseListener
	{
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private final XGValue loValue, hiValue;
		private XGValue currentValue, otherValue;
		private XGParameter loParameter, hiParameter;
		private int loX, hiX, curX;
		private Graphics2D g2;
//		private Cursor lastCursor;

		XGRangeBar(XGValue lo, XGValue hi)
		{	//super();
			//this.setBorder(null);	buggy: getX() liefert IMMER inset.left; getY() IMMER inset.top; (5, 15)! Bug?; deshalb beim malen diese koordinaten ignorieren...
			this.loValue = lo;
			this.hiValue = hi;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.loValue.getValueListeners().add(this);
			this.hiValue.getValueListeners().add(this);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

//		private void limitize()
//		{	int min = this.loValue.getIndex();
//			int max = this.hiValue.getIndex();
//			this.loValue.setIndex(Math.min(min, max));
//			this.hiValue.setIndex(Math.max(min, max));
//		}

		private void setCurrent(int x)
		{	if(Math.abs(x - this.loX) < Math.abs(x - this.hiX))
			{	this.curX = this.loX;
				this.currentValue = this.loValue;
				this.otherValue = this.hiValue;
			}
			else
			{	this.curX = this.hiX;
				this.currentValue = this.hiValue;
				this.otherValue = this.loValue;
			}
		}

		@Override public boolean isEnabled()
		{	return super.isEnabled() && this.loValue != null && this.loValue.getParameter() != null && this.hiValue != null && this.hiValue.getParameter() != null;
		}

		@Override protected void paintComponent(Graphics g)
		{
//if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
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
		{	if(e.getButton() != MouseEvent.BUTTON1) return;
			this.setCurrent(e.getX());
			if(e.getX() > this.curX)this.currentValue.addIndex(1);
			else this.currentValue.addIndex(-1);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	int r = e.getWheelRotation();
			if(r < 0)
			{	this.loValue.addIndex(r);
				if(this.loValue.hasChanged())
				{	this.hiValue.addIndex(r);
				}
			}
			else
			{	this.hiValue.addIndex(r);
				if(this.hiValue.hasChanged())
				{	this.loValue.addIndex(r);
				}
			}
			e.consume();
		}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGUI.VARIABLES.dragEvent.getX();
			this.currentValue.addIndex(distance);
			int range = this.hiValue.getIndex() - this.loValue.getIndex();
			if(range < 0) this.otherValue.addIndex(distance); //TODO: suboptimal; bildet zwar das Device-Verhalten ab, sendet aber unnötig Werte
			XGUI.VARIABLES.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e)
		{
		}

		@Override public void contentChanged(XGValue v)
		{	this.repaint();
		}

		@Override public void mousePressed(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
			XGUI.VARIABLES.mousePressed = true;
			this.setCurrent(e.getX());
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
			XGUI.VARIABLES.mousePressed = false;
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
