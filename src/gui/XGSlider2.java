package gui;

import application.XGMath;
import application.XGStrings;
import module.XGModule;
import parm.XGParameter;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class XGSlider2 extends XGFrame
{
	static XGSlider2 newSlider(XGModule mod, XMLNode node)throws XGComponentException
	{	Set<String> tags = XGStrings.splitCSV(node.getStringAttribute(ATTR_VALUE_TAG));
		XGValue start, end;
		switch(tags.size())
		{	case 2:
				start = mod.getValues().get((String)tags.toArray()[0]);
				end = mod.getValues().get((String)tags.toArray()[1]);
				break;
			case 1:
				start = null;
				end = mod.getValues().get((String)tags.toArray()[0]);
				break;
			default:	throw new RuntimeException("sliders requires one or two values!");
		}
		return new XGSlider2(start, end);
	}

/***********************************************************************************************************************/

	private final XGValue start, end;

	public XGSlider2(XGValue start, XGValue end)throws XGComponentException
	{	super("");
		XGSliderPanel panel;
		XGValueLabel label;
		if(end == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.start = null;
			this.end = null;
			return;
		}
		this.end = end;

		XGParameter p = this.end.getParameter();
		String name = p.getName();
		if(start == null) this.start = new XGFixedValue(p, p.getOriginValue());
		else
		{	this.start = start;
			name = XGStrings.commonString(this.start.getParameter().getName(), name);
		}
		this.setName(name);

		panel = new XGSliderPanel(this);
		this.add(panel, "0,0,1,1");

		label = new XGValueLabel(this.end);
		this.add(label, "0,1,1,1");
	}

/***********************************************************************************************************************/

	private class XGSliderPanel extends JPanel implements ComponentListener
	{
		final XGSlider2 slider;
		final XGSlider2Bar bar;

		XGSliderPanel(XGSlider2 slider)
		{	this.slider = slider;

			this.bar = new XGSlider2Bar(this);
			this.add(this.bar);

			this.addComponentListener(this);
			this.setLayout(null);
		}

		@Override protected void paintComponent(Graphics g)
		{	super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			g2.setColor(COL_BAR_BACK);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}

		public void componentResized(ComponentEvent event){	this.bar.resized();}

		public void componentMoved(ComponentEvent event){}

		public void componentShown(ComponentEvent event){}

		public void componentHidden(ComponentEvent event){}
	}

/*************************************************************************************************************************/

	private class XGSlider2Bar extends JComponent
	{
		final XGSliderPanel panel;

		XGSlider2Bar(XGSliderPanel panel)
		{	this.panel = panel;
		}

		void resized()
		{	XGValue start = this.panel.slider.start;
			XGValue end = this.panel.slider.end;
			int xe = XGMath.linearScale(end.getIndex(), end.getParameter().getMinIndex(), end.getParameter().getMaxIndex(),this.panel.getX(), this.panel.getWidth()); 
			int xs = XGMath.linearScale(start.getIndex(), start.getParameter().getMinIndex(), start.getParameter().getMaxIndex(), this.panel.getX(), this.panel.getWidth());
			this.setBounds(xs, this.panel.getHeight(), xe - xs, this.panel.getHeight());
			this.repaint();
		}

		@Override protected void paintComponent(Graphics g)
		{	super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			g2.setColor(COL_BAR_FORE);
//			g2.setStroke(DEF_STROKE);
//			g2.drawLine(0, 2, this.getWidth(), 2);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
		}
	}
}
