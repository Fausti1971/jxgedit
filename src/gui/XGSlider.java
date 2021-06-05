package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import application.XGMath;
import static gui.XGUI.*;import module.XGModule;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;
import static value.XGValueStore.STORE;import xml.XMLNode;

public class XGSlider extends JPanel implements XGParameterConstants, XGValueChangeListener, MouseListener, FocusListener, XGComponent
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGSliderBar bar;
	private final XGValueLabel label;

	public XGSlider(XGValue v)
	{	this.value = v;
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
			this.borderize();
		}
//		this.setName(this.value.getParameter().getShortName());
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
	}

	@Override public String getName()
	{	return this.value.getParameter().getShortName();
	}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(v.toString());
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
		{	this.value = v;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.value.getValueListeners().add(this);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public boolean isEnabled()
		{	return super.isEnabled() && this.value != null && this.value.getParameter() != null;
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
			if(this.getX() + this.barWidth < e.getX()) this.value.addIndex(1);
			else this.value.addIndex(-1);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e)
		{	this.value.addIndex(e.getWheelRotation());
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

		@Override public void contentChanged(XGValue v)
		{	this.repaint();
		}

		@Override public void mousePressed(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGUI.VARIABLES.dragEvent = e;
		}


		@Override public void mouseEntered(MouseEvent e)
		{
		}

		@Override public void mouseExited(MouseEvent e)
		{	
		}
	}
public void focusGained(FocusEvent event)
	{
	}public void focusLost(FocusEvent event)
	{
	}public void mouseClicked(MouseEvent event)
	{
	}public void mousePressed(MouseEvent event)
	{
	}public void mouseReleased(MouseEvent event)
	{
	}public void mouseEntered(MouseEvent event)
	{
	}public void mouseExited(MouseEvent event)
	{
	}}
