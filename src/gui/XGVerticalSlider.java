package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import application.XGMath;
import parm.XGParameter;
import value.XGValue;
import value.XGValueChangeListener;

public class XGVerticalSlider extends XGFrame implements XGValueChangeListener, MouseListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGVerticalSliderBar bar;
	private final XGValueLabel label;

	public XGVerticalSlider(XGValue v)
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
			this.setName(this.value.getParameter().getShortName());
		}
		this.addMouseListener(this);
		this.value.getValueListeners().add(this);

		this.bar = new XGVerticalSliderBar(this);
		this.add(this.bar, "0,0,1,5");

		this.label = new XGValueLabel(this.value);
		this.add(this.label, "0,5,1,1");
	}

//	@Override public String getName(){	return this.value.getParameter().getShortName();}

	@Override public void contentChanged(XGValue v){	this.bar.repaint();}


	private static class XGVerticalSliderBar extends JComponent implements MouseMotionListener, MouseWheelListener, XGComponent
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private final XGVerticalSlider slider;
		private int barHeight;

		private XGVerticalSliderBar(XGVerticalSlider s)
		{	this.slider = s;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		@Override public boolean isEnabled(){	return this.slider.isEnabled();}

		@Override protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			XGParameter parameter = this.slider.value.getParameter();
			float w3 = (float)this.getWidth()/3, h2 = (float)this.getHeight()/2;
// draw background
			GradientPaint gp = new GradientPaint(w3,h2,COL_BAR_BACK_DARK, this.getWidth(), h2, COL_BAR_BACK,true);
			g2.setPaint(gp);
		//	g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
			int originY = XGMath.linearIO(parameter.getOriginIndex(), parameter.getMinIndex(), parameter.getMaxIndex(), this.getHeight(), 0);
			int endY = XGMath.linearIO(this.slider.value.getIndex(), parameter.getMinIndex(), parameter.getMaxIndex(), this.getHeight(), 0);
			this.barHeight = endY - originY;
			gp = new GradientPaint(w3,h2,COL_BAR_FORE_LIGHT, this.getWidth(), h2, COL_BAR_FORE,true);
			g2.setPaint(gp);
			g2.setStroke(DEF_STROKE);
			g2.drawLine(0, endY, this.getWidth(), endY);
			g2.fillRoundRect(0, Math.min(originY, originY + this.barHeight), this.getWidth(), Math.abs(this.barHeight), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	if(e.getButton() != MouseEvent.BUTTON1) return;
			if(this.getY() + this.barHeight > e.getY()) this.slider.value.addIndex(1, true);
			else this.slider.value.addIndex(-1, true);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e){	this.slider.value.addIndex(XGUI.getWheelRotation(e), true);}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = XGUI.ENVIRONMENT.dragEvent.getY() - e.getY();
			this.slider.value.addIndex(distance, true);
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e){}

		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}
}
