package gui;

import java.awt.*;
import java.awt.event.*;import java.util.Set;
import javax.swing.*;
import application.XGMath;
import application.XGStrings;
import module.XGModule;import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;import xml.XMLNode;

public class XGRange extends XGFrame implements XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static XGRange newRange(XGModule mod, XMLNode node)
	{	Set<String> tags = XGStrings.splitCSV(node.getStringAttribute(ATTR_VALUE_TAG));
		if(tags.size() != 2) LOG.warning("incorrect count of tags for " + ATTR_RANGE + tags);
		XGValue lo = mod.getValues().get((String)tags.toArray()[0]);
		XGValue hi = mod.getValues().get((String)tags.toArray()[1]);
		return new XGRange(lo, hi); 
	}

/*******************************************************************************************************/
	private final XGRangeBar bar;
	private final XGValueLabel label;
	private final XGValue loValue, hiValue;

	public XGRange(XGValue lo, XGValue hi)
	{	super("");
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

		this.bar = new XGRangeBar(this);
		this.add(this.bar, "0,0,1,2");

		this.label = new XGRangeLabel(this.loValue, this.hiValue);
		this.add(this.label, "0,2,1,1");

		this.addMouseListener(this);
		boolean ena = this.isEnabled();
		this.bar.setEnabled(ena);
		this.label.setEnabled(ena);
	}

	@Override public String getToolTipText(){	return this.loValue.getValue() + "..." + this.hiValue.getValue();}

	@Override public boolean isVisible(){	return this.loValue != null && this.loValue.getParameter().isValid()  && this.hiValue != null && this.hiValue.getParameter().isValid();}

	@Override public void contentChanged(XGValue v)
	{	this.bar.repaint();
		this.label.setText(this.loValue + "..." + this.hiValue);
	}

/***************************************************************************************************/

	private static class XGRangeBar extends JPanel implements MouseMotionListener, MouseWheelListener, XGComponent
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
			float w2 = (float)this.getWidth()/2, h3 = (float)this.getHeight()/3;
// draw background
			GradientPaint gp = new GradientPaint(w2,h3,COL_BAR_BACK_DARK, w2, this.getHeight(), COL_BAR_BACK,true);
			g2.setPaint(gp);
	//		g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
			this.loX = XGMath.linearIO(this.range.loValue.getIndex(), loParameter.getMinIndex(), loParameter.getMaxIndex(), 0, this.getWidth());
			this.hiX = XGMath.linearIO(this.range.hiValue.getIndex(), hiParameter.getMinIndex(), hiParameter.getMaxIndex(), 0, this.getWidth());
			float diff = Math.max(DEF_STROKEWIDTH, this.hiX - this.loX);
			gp = new GradientPaint(w2,h3,COL_BAR_FORE_LIGHT, w2, this.getHeight(), COL_BAR_FORE,true);
			g2.setPaint(gp);
			g2.fillRoundRect(Math.min(this.loX, this.getWidth() - DEF_STROKEWIDTH), 0, (int)diff, this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
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
		{	int r = XGUI.getWheelRotation(e);//e.getWheelRotation();
			if(r < 0)
			{	this.range.loValue.addIndex(r, true);
				if(this.range.loValue.hasChanged())
					this.range.hiValue.addIndex(r, true);
			}
			else
			{	this.range.hiValue.addIndex(r, true);
				if(this.range.hiValue.hasChanged())
					this.range.loValue.addIndex(r, true);
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
		{	XGComponent.super.mousePressed(e);
			this.setCurrent(e.getX());
			e.consume();
		}
	
		@Override public void mouseReleased(MouseEvent e)
		{	XGComponent.super.mouseReleased(e);
			this.setCurrent(e.getX());
		}

		@Override public void mouseEntered(MouseEvent e){}

		@Override public void mouseExited(MouseEvent e){}
	}

/************************************************************************************************************/

	private class XGRangeLabel extends XGValueLabel
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	/*****************************************************************************************************/

		private final XGValue value2;

		public XGRangeLabel(XGValue lo, XGValue hi)
		{	super(lo);
			this.value2 = hi;
			this.value2.getValueListeners().add(this);
			this.setText(this.value + "..." + this.value2);
		}

		@Override public void mouseClicked(MouseEvent e)
		{	super.mouseClicked(e);
			String s = JOptionPane.showInputDialog(e.getComponent(), this.value2.getParameter(), this.value2);
			try
			{	this.value2.setValue(s, true);
			}
			catch(NumberFormatException ignored)
			{	LOG.warning(s);
			}
		}

		@Override public void contentChanged(XGValue v){	this.setText(this.value + "..." + this.value2);}
	}
}
