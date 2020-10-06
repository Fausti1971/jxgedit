package gui;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JRadioButton;
import adress.XGAddress;
import application.XGLoggable;
import application.XGMath;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGPoint extends JRadioButton implements GuiConstants, XGLoggable, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
//	private static final int POINT_RADIUS = 20;

/******************************************************************************************************/

	private final XGValue valueX, valueY;
	private final XGArea panel;

	public XGPoint(XGArea panel, XMLNode n, XGModule mod)
	{	this.panel = panel;
		XGAddress ax = null, ay = null;
		XGValue vx, vy;
		if(n.hasAttribute(ATTR_ADDRESS_X))
		{	ax = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_X), mod.getAddress());
			vx = mod.getValues().getOrDefault(ax, new XGFixedValue("value x not found " + ax, 0));
		}
		else if(n.hasAttribute(ATTR_FIXED_X))
		{	vx = new XGFixedValue("fixed value", n.getIntegerAttribute(ATTR_FIXED_X));
		}
		else vx = new XGFixedValue("no value x", 0);

		if(n.hasAttribute(ATTR_ADDRESS_Y))
		{	ay = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_Y), mod.getAddress());
			vy = mod.getValues().getOrDefault(ay, new XGFixedValue("value y not found " + ay, 0));
		}
		else if(n.hasAttribute(ATTR_FIXED_Y))
		{	vy = new XGFixedValue("fixed value", n.getIntegerAttribute(ATTR_FIXED_Y));
		}
		else vy = new XGFixedValue("no value y", 0);

		this.valueX = vx;
		this.valueX.addValueListener((XGValue)->{this.setLocation();});
		this.valueY = vy;
		this.valueY.addValueListener((XGValue)->{this.setLocation();});
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setToolTipText("x=" + this.valueX.getInfo() + "\ny=" + this.valueY.getInfo());
		this.setSize(super.getPreferredSize());//JComponents werden ohne LayoutManager nicht resized
	}

	public XGPoint(XGArea panel, XGValue valX, XGValue valY)
	{	this.panel = panel;
		this.valueX = valX;
		this.valueX.addValueListener((XGValue)->{this.setLocation();});
		this.valueY = valY;
		this.valueY.addValueListener((XGValue)->{this.setLocation();});
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
//		this.setLocation();
	}

//	@Override public Point getLocation()
//	{	int x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.panel.getWidth());
//		int y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMinYIndex(), this.panel.getMaxYIndex(), this.panel.getHeight(), 0);
//		return new Point(x, y);
//	}

	void setLocation()
	{	int x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.panel.getWidth());
		int y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMinYIndex(), this.panel.getMaxYIndex(), this.panel.getHeight(), 0);
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.panel.repaint();
		this.setToolTipText("x=" + this.valueX.getInfo() + "\ny=" + this.valueY.getInfo());
//System.out.println(this + "=" + this.getBounds());
	}

	XGValue getValueX()
	{	return this.valueX;
	}

	XGValue getValueY()
	{	return this.valueY;
	}

	@Override public String toString()
	{	return this.valueX.getInfo() + "/" + this.valueY.getInfo();
	}

	@Override public void mousePressed(MouseEvent e)
	{	XGComponent.dragEvent = e;
		this.setSelected(true);
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	XGComponent.dragEvent = e;
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public void mouseDragged(MouseEvent e)
	{	this.valueX.addIndex(e.getXOnScreen() - XGComponent.dragEvent.getXOnScreen());
		this.valueY.addIndex(XGComponent.dragEvent.getYOnScreen() - e.getYOnScreen());
//System.out.println("x0=" + XGComponent.dragEvent.getX() + " x1=" + e.getX());
		XGComponent.dragEvent = e;
		e.consume();
	}

	@Override public void mouseClicked(MouseEvent e)
	{
	}

	@Override public void mouseMoved(MouseEvent e)
	{
	}
}
