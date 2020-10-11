package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import application.XGLoggable;
import application.XGMath;
import parm.XGParameter;
import value.XGValue;

public class XGPoint extends JComponent implements GuiConstants, XGLoggable, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private static final int POINT_RADIUS = 5;
	private static final int POINT_SIZE = POINT_RADIUS * 2;

/******************************************************************************************************/

	private XGValue valueX, valueY;
	private XGDrawPanel panel;
	private final boolean isXAbsolute, isYAbsolute;
	private final int index;
	private XGPoint previous = null;
	private final XGTooltip tooltip = new XGTooltip();
	private Graphics2D g2;

	public XGPoint(int index, XGValue valX, XGValue valY, boolean xAbs, boolean yAbs)
	{	this.index = index;
		this.valueX = valX;
		this.valueY = valY;
		this.isXAbsolute = xAbs;
		this.isYAbsolute = yAbs;
		this.setSize(POINT_SIZE + 1, POINT_SIZE + 1);
		this.setPreferredSize(this.getSize());
		if(this.isMovable()) this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else this.setVisible(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private boolean isMovable()
	{	XGParameter xP = this.valueX.getParameter(), yP = this.valueY.getParameter();
		return xP.getMaxIndex() - xP.getMinIndex() != 0 || yP.getMaxIndex() - yP.getMinIndex() != 0;
	}

	void setPanel(XGDrawPanel pnl)
	{	this.panel = pnl;
		if(this.index != 0)
		{	this.previous = this.panel.getPoints().get(this.index - 1);
//			this.previous.next = this;
		}
		this.valueX.addValueListener((XGValue)->{this.panel.setSelectedPoint(this);});
		this.valueY.addValueListener((XGValue)->{this.panel.setSelectedPoint(this);});
		this.setLocation();
	}

	void setYValue(XGValue v)
	{	this.valueY = v;
		if(this.isMovable())
		{	this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.valueY.addValueListener((XGValue)->{this.panel.setSelectedPoint(this);});
			this.setVisible(true);
		}
		else this.setVisible(false);
		this.panel.repaint();
	}

	void setLocation()
	{	int x, y;
		x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.panel.getWidth());
		if(!this.isXAbsolute && this.previous != null)
		{	try
			{	x += this.previous.getX() + POINT_RADIUS;
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
		y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMinYIndex(), this.panel.getMaxYIndex(), this.panel.getHeight(), 0);
		if(!this.isYAbsolute && this.previous != null)
		{	try
			{	y += this.previous.getY() + POINT_RADIUS;
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
		this.setLocation(x - POINT_RADIUS, y - POINT_RADIUS);
		this.tooltip.setName(this.toString());
		if(this.isShowing())
		{	Point p = this.getLocationOnScreen();
			p.x += POINT_SIZE;
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
	{	this.requestFocusInWindow();
		this.repaint();

	}

	@Override public void mouseMoved(MouseEvent e)
	{
	}

	@Override protected void paintComponent(Graphics g)
	{	super.paintComponent(g);
		this.g2 = (Graphics2D)g.create();
		this.g2.addRenderingHints(XGComponent.AALIAS);
		this.g2.setColor(COL_BAR_FORE);
		if(this.equals(this.panel.getSelectedPoint())) this.g2.fillOval(0, 0, POINT_SIZE, POINT_SIZE);
		else this.g2.drawOval(0, 0, POINT_SIZE, POINT_SIZE);
		this.g2.dispose();
	}
}
