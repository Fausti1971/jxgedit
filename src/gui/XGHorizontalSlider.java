package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import application.XGMath;
import application.XGStrings;import module.XGModule;import parm.XGParameter;
import value.XGValue;
import value.XGValueChangeListener;import xml.XMLNode;

public class XGHorizontalSlider extends XGFrame implements XGValueChangeListener, MouseListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	static XGFrame newSlider(XGModule mod, XMLNode node)
	{	XGValue v = mod.getValues().get(node.getStringAttribute(ATTR_VALUE_TAG));
		Rectangle r = XGStrings.toRectangle(node.getStringAttribute(ATTR_CONSTRAINT));
		if(node.hasAttribute(ATTR_ORIENTATION))
		{	switch(XGOrientation.valueOf(node.getStringAttribute(ATTR_ORIENTATION)))
			{	case horizontal:	return new XGHorizontalSlider(v);
				case vertical:		return new XGVerticalSlider(v);
				default:			break;
			}
		}
		if(r.width > r.height) return new XGHorizontalSlider(v);
		else return new XGVerticalSlider(v);
	}

/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGHorizontalSliderBar bar;
	private final XGValueLabel label;

	public XGHorizontalSlider(XGValue v)
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

		this.bar = new XGHorizontalSliderBar(this);
		this.add(this.bar, "0,0,1,1");

		this.label = new XGValueLabel(this.value);
		this.add(this.label, "0,1,1,1");
	}

//	@Override public String getName(){	return this.value.getParameter().getShortName();}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
	}


	private static class XGHorizontalSliderBar extends JComponent implements MouseMotionListener, MouseWheelListener, XGComponent
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/**********************************************************************************************/

		private final XGHorizontalSlider slider;
		private int barWidth;

		private XGHorizontalSliderBar(XGHorizontalSlider s)
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
			float w2 = (float)this.getWidth()/2, h3 = (float)this.getHeight()/3;
// draw background
			GradientPaint gp = new GradientPaint(w2,h3,COL_BAR_BACK_DARK, w2, this.getHeight(), COL_BAR_BACK,true);
			g2.setPaint(gp);
		//	g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
			int originX = XGMath.linearIO(parameter.getOriginIndex(), parameter.getMinIndex(), parameter.getMaxIndex(), 0, this.getWidth());
			int endX = XGMath.linearIO(this.slider.value.getIndex(), parameter.getMinIndex(), parameter.getMaxIndex(), 0, this.getWidth());
			this.barWidth = Math.max(DEF_STROKEWIDTH, endX - originX);
			gp = new GradientPaint(w2,h3,COL_BAR_FORE_LIGHT, w2, this.getHeight(), COL_BAR_FORE,true);
			g2.setPaint(gp);
			g2.fillRoundRect(Math.min(originX, originX + this.barWidth), 0, Math.abs(this.barWidth), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	if(e.getButton() != MouseEvent.BUTTON1) return;
			if(this.getX() + this.barWidth < e.getX()) this.slider.value.addIndex(1, true);
			else this.slider.value.addIndex(-1, true);
			e.consume();
		}
	
		@Override public void mouseWheelMoved(MouseWheelEvent e){	this.slider.value.addIndex(XGUI.getWheelRotation(e), true);}
	
		@Override public void mouseDragged(MouseEvent e)
		{	int distance = e.getX() - XGUI.ENVIRONMENT.dragEvent.getX();
			this.slider.value.addIndex(distance, true);
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e){}

		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}
}
