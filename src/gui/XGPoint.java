package gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JRadioButton;
import application.XGLoggable;
import application.XGMath;
import value.XGValue;

public class XGPoint extends JRadioButton implements GuiConstants, XGLoggable, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
//	private static final int POINT_RADIUS = 20;

/******************************************************************************************************/

	private final XGValue valueX, valueY;
	private XGDrawPanel panel;
	private final boolean isXAbsolute, isYAbsolute;
	private final int index;
	private XGPoint previous = null, next = null;
	private final XGTooltip tooltip = new XGTooltip();

	public XGPoint(int index, XGValue valX, XGValue valY, boolean xAbs, boolean yAbs)
	{	this.index = index;
		this.valueX = valX;
		this.valueY = valY;
		this.isXAbsolute = xAbs;
		this.isYAbsolute = yAbs;
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	void setPanel(XGDrawPanel pnl)
	{	this.panel = pnl;
		if(this.index != 0)
		{	this.previous = this.panel.getPoints().get(this.index - 1);
			this.previous.next = this;
		}
		this.valueX.addValueListener((XGValue)->{this.panel.repaint();});
		this.valueY.addValueListener((XGValue)->{this.panel.repaint();});
		this.setLocation();
	}

	void setLocation()
	{	int x, y;
		x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.panel.getWidth());
		if(!this.isXAbsolute && this.previous != null)
		{	try
			{	x += this.previous.getX() + this.previous.getWidth()/2;
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
		y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMinYIndex(), this.panel.getMaxYIndex(), this.panel.getHeight(), 0);
		if(!this.isYAbsolute && this.previous != null)
		{	try
			{	y += this.previous.getY() + this.previous.getWidth()/2;
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.tooltip.setName(this.toString());
		if(this.isShowing())
		{	Point p = this.getLocationOnScreen();
			p.x += this.getWidth();
//			p.y -= this.tooltip.getHeight();
			this.tooltip.setLocation(p);
		}
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
//		this.tooltip.setLocation(e.getXOnScreen(), e.getYOnScreen());
		this.tooltip.setVisible(true);
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	XGComponent.dragEvent = e;
		this.tooltip.setVisible(false);
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
		this.setLocation();
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
