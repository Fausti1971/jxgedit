package gui;

import application.XGMath;
import application.XGStrings;
import module.XGModule;
import parm.XGParameter;
import value.XGFixedValue;
import value.XGValue;
import value.XGValueChangeListener;
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
		final XGSliderHandle start, end;

		XGSliderPanel(XGSlider2 slider)
		{	this.slider = slider;

			this.bar = new XGSlider2Bar(this);
			this.add(this.bar);

			this.start = new XGSliderHandle(this, slider.start);
			this.add(this.start);

			this.end = new XGSliderHandle(this, slider.end);
			this.add(this.end);

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

		public void componentResized(ComponentEvent event)
		{	this.start.resized();
			this.end.resized();
			this.bar.resized();
		}

		public void componentMoved(ComponentEvent event)
		{
		}

		public void componentShown(ComponentEvent event)
		{
		}

		public void componentHidden(ComponentEvent event)
		{
		}
	}

/*************************************************************************************************************************/

	private class XGSlider2Bar extends JComponent
	{
		final XGSliderPanel panel;

		XGSlider2Bar(XGSliderPanel panel)
		{	this.panel = panel;
		}

		void resized()
		{	this.setBounds(this.panel.start.getX(), this.panel.getHeight() - DEF_STROKEWIDTH, Math.abs(this.panel.end.getX() + DEF_STROKEWIDTH/2 - this.panel.start.getX() + DEF_STROKEWIDTH/2), DEF_STROKEWIDTH);
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

/************************************************************************************************************************/

	private class XGSliderHandle extends JComponent implements XGValueChangeListener, MouseMotionListener, XGComponent
	{
		final XGSliderPanel panel;
		final XGValue value;

		XGSliderHandle(XGSliderPanel panel, XGValue v)
		{	this.panel = panel;
			this.value = v;
			this.value.getValueListeners().add(this);
			boolean fixed = v instanceof XGFixedValue;
			this.setEnabled(!fixed);
			this.setVisible(!fixed);
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		@Override public void contentChanged(XGValue v)
		{	XGParameter p = this.value.getParameter();
			this.setLocation(XGMath.linearScale(this.value.getIndex(), p.getMinIndex(), p.getMaxIndex(), 0, this.panel.getWidth()) - DEF_STROKEWIDTH/2, 0);
			this.panel.bar.resized();
		}

		@Override protected void paintComponent(Graphics g)
		{	super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.addRenderingHints(AALIAS);
			g2.setColor(COL_BAR_FORE);
			g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
			g2.dispose();
//System.out.println(this.value.getTag() + ", handle=" + this.getBounds());
//System.out.println(this.value.getTag() + ", panel=" + this.panel.getBounds());
		}

		public void resized(){	this.setSize(DEF_STROKEWIDTH, this.panel.getHeight());}
	
		@Override public void mouseDragged(MouseEvent e)
		{	XGParameter p = this.value.getParameter();
			int distance = e.getX() - XGUI.ENVIRONMENT.dragEvent.getX();
	System.out.println(distance);
			int diff = XGMath.linearScale(distance, 0, this.panel.getWidth(), p.getMinIndex(), p.getMaxIndex());
			this.value.addIndex(diff, true);
			XGUI.ENVIRONMENT.dragEvent = e;
			e.consume();
		}

		@Override public void mouseMoved(MouseEvent e){}
	}
}
