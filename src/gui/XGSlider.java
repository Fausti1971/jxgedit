package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import application.XGMath;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;

public class XGSlider extends XGFrame implements XGValueChangeListener, MouseListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGSliderBar bar;
	private final XGValueLabel label;

	public XGSlider(XGValue v)
	{	super("");
		this.value = v;
		if(v == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.setToolTipText(null);
			this.bar = null;
			this.label = null;
			return;
		}
		if(this.value.getParameter() != null)
		{	this.setEnabled(true);
			this.setVisible(true);
		}
		this.setName(this.value.getParameter().getShortName());
		this.addMouseListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,2,0), 0, 0);
		this.bar = new XGSliderBar(this);
		this.add(this.bar, gbc);

		this.label = new XGValueLabel(this.value);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(this.label, gbc);
	}

	@Override public String getName(){	return this.value.getParameter().getShortName();}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
	}


	private class XGSliderBar extends JComponent implements MouseMotionListener, MouseWheelListener, MouseListener
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

		private XGSliderBar(XGSlider s)
		{	this.value = s.value;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public boolean isEnabled()
		{	return super.isEnabled();
		}

		@Override protected void paintComponent(Graphics g)
		{
			this.g2 = (Graphics2D)g.create();
			this.g2.addRenderingHints(AALIAS);
			this.parameter = this.value.getParameter();
	// draw background
			this.g2.setColor(this.getBackground().brighter());
			this.g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
	// draw foreground
			this.originWidth = XGMath.linearIO(this.parameter.getOriginIndex(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, this.getWidth());
			this.barWidth = XGMath.linearIO(this.value.getIndex(), this.parameter.getMinIndex(), this.parameter.getMaxIndex(), 0, this.getWidth()) - this.originWidth;
			this.g2.setColor(COL_BAR_FORE);
			this.g2.fillRoundRect(Math.min(this.originWidth, this.originWidth + this.barWidth), 0, Math.abs(this.barWidth), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			this.g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	if(e.getButton() != MouseEvent.BUTTON1) return;
			if(this.getX() + this.barWidth < e.getX()) this.value.addIndex(1, true);
			else this.value.addIndex(-1, true);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	this.value.addIndex(e.getWheelRotation(), true);
			e.consume();
		}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGUI.ENVIRONMENT.dragEvent.getX();
			this.value.addIndex(distance, true);
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e){}

		@Override public void mousePressed(MouseEvent e)
		{	XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseReleased(MouseEvent e){	XGUI.ENVIRONMENT.dragEvent = e;}


		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}
}
