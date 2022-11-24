package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import application.XGMath;
import parm.XGParameter;
import parm.XGParameterChangeListener;import value.XGValue;
import value.XGValueChangeListener;

public class XGVerticalSlider extends XGFrame implements XGValueChangeListener, XGParameterChangeListener, MouseListener, XGValueComponent
{
/*****************************************************************************************************************************/

	private final XGValue value;
	private final XGVerticalSliderPanel bar;
	private final XGValueLabel label;
	private XGParameter parameter;

	public XGVerticalSlider(XGValue v)throws XGComponentException
	{	super("");
		this.value = v;
		if(this.value == null) throw new XGComponentException("value is null");

		this.addMouseListener(this);
		this.value.getValueListeners().add(this);

		this.bar = new XGVerticalSliderPanel(this);
		this.bar.addMouseListener(this);
		this.add(this.bar, "0,0,1,5");

		this.label = new XGValueLabel(this.value);
		this.add(this.label, "0,5,1,1");

		this.parameterChanged(value.getParameter());
	}

//	@Override public String getName(){	return this.value.getParameter().getShortName();}

	@Override public void contentChanged(XGValue v){	this.bar.repaint();}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setName(p.getShortName());
			this.setToolTipText(p.getName());
			this.label.setText(this.value.toString());
			this.setVisible(p.isValid());
			this.setEnabled(p.isValid());
		}
		this.parameter = p;
	}

	public XGValue[] getValues(){	return new XGValue[]{this.value};
	}

	/***********************************************************************************************************************/

	private static class XGVerticalSliderPanel extends JComponent implements MouseMotionListener, MouseWheelListener, XGComponent, XGValueComponent
	{
		private final XGVerticalSlider slider;
		private final Rectangle bar = new Rectangle();

		private XGVerticalSliderPanel(XGVerticalSlider s)
		{	this.slider = s;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
		}

		//@Override public boolean isEnabled(){	return this.slider.isEnabled();}

		@Override public XGValue[] getValues(){	return this.slider.getValues();}

		@Override protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			float w3 = (float)this.getWidth()/3, h2 = (float)this.getHeight()/2;
// draw background
			GradientPaint gp = new GradientPaint(w3,h2,COL_BAR_BACK_DARK, this.getWidth(), h2, COL_BAR_BACK,true);
			g2.setPaint(gp);
		//	g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
			this.bar.y = XGMath.linearScale(this.slider.parameter.getOriginIndex(), this.slider.parameter.getMinIndex(), this.slider.parameter.getMaxIndex(), this.getHeight(), 0);
			int endY = XGMath.linearScale(this.slider.value.getIndex(), this.slider.parameter.getMinIndex(), this.slider.parameter.getMaxIndex(), this.getHeight(), 0);
			this.bar.height = endY - this.bar.y;
			gp = new GradientPaint(w3,h2,COL_BAR_FORE_LIGHT, this.getWidth(), h2, COL_BAR_FORE,true);
			g2.setPaint(gp);
			g2.setStroke(DEF_NORMAL_STROKE);
			g2.drawLine(0, endY, this.getWidth(), endY);
			g2.fillRoundRect(0, Math.min(this.bar.y, this.bar.y + this.bar.height), this.getWidth(), Math.abs(this.bar.height), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}

		@Override public void mouseClicked(MouseEvent e)
		{	if(e.getButton() == MouseEvent.BUTTON1)
			{	if(this.bar.y + this.bar.height > e.getY()) this.slider.value.addIndex(1, true);
				else this.slider.value.addIndex(-1, true);
			}
	//		XGComponent.super.mouseClicked(e);
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
