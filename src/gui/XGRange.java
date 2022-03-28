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

public class XGRange extends XGFrame implements XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*******************************************************************************************************/

	private final XGRangeBar bar;
	private final XGRangeLabel label;
	private final XGValue loValue, hiValue;

	public XGRange(XGValue lo, XGValue hi)
	{	super();
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
		this.addMouseListener(this);
		this.addFocusListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);
		this.bar = new XGRangeBar(this);
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

/***************************************************************************************************/

	private class XGRangeBar extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener
	{
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private XGValue currentValue, otherValue;
		private int loX, hiX, curX;
		private final XGRange range;

		XGRangeBar(XGRange range)
		{	this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.range = range;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		private void setCurrent(int x)
		{	if(Math.abs(x - this.loX) < Math.abs(x - this.hiX))
			{	this.curX = this.loX;
				this.currentValue = this.range.loValue;
				this.otherValue = this.range.hiValue;
			}
			else
			{	this.curX = this.hiX;
				this.currentValue = this.range.hiValue;
				this.otherValue = this.range.loValue;
			}
		}

		@Override protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			XGParameter loParameter = this.range.loValue.getParameter();
			XGParameter hiParameter = this.range.hiValue.getParameter();
// draw background
			g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
			this.loX = XGMath.linearIO(this.range.loValue.getIndex(), loParameter.getMinIndex(), loParameter.getMaxIndex(), 0, this.getWidth());
			this.hiX = XGMath.linearIO(this.range.hiValue.getIndex(), hiParameter.getMinIndex(), hiParameter.getMaxIndex(), 0, this.getWidth());
//			this.originWidth = XGMath.linearIO(this.loParameter.getOrigin(), this.loParameter.getMinIndex(), this.loParameter.getMaxIndex(), 0, this.getWidth());
			g2.setColor(COL_BAR_FORE);
			g2.fillRoundRect(this.loX, 0, this.hiX - this.loX, this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	if(e.getButton() != MouseEvent.BUTTON1) return;
			this.setCurrent(e.getX());
			if(e.getX() > this.curX)this.currentValue.addIndex(1, true);
			else this.currentValue.addIndex(-1, true);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	int r = e.getWheelRotation();
			if(r < 0)
			{	this.range.loValue.addIndex(r, true);
				if(this.range.loValue.hasChanged())
				{	this.range.hiValue.addIndex(r, true);
				}
			}
			else
			{	this.range.hiValue.addIndex(r, true);
				if(this.range.hiValue.hasChanged())
				{	this.range.loValue.addIndex(r, true);
				}
			}
			e.consume();
		}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGUI.ENVIRONMENT.dragEvent.getX();
			this.currentValue.addIndex(distance, true);
			int range = this.range.hiValue.getIndex() - this.range.loValue.getIndex();
			if(range < 0) this.otherValue.addIndex(distance, true); //TODO: suboptimal; bildet zwar das Device-Verhalten ab, sendet aber unnötig Werte
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e){}

		@Override public void mousePressed(MouseEvent e)
		{	XGUI.ENVIRONMENT.dragEvent = e;
			XGUI.ENVIRONMENT.mousePressed = true;
			this.setCurrent(e.getX());
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGUI.ENVIRONMENT.dragEvent = e;
			XGUI.ENVIRONMENT.mousePressed = false;
			this.setCurrent(e.getX());
		}

		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}
}
