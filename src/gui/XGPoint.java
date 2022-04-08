package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;
import application.XGLoggable;
import application.XGMath;
import parm.XGParameter;
import value.XGValue;

public class XGPoint extends JComponent implements XGUI, XGLoggable
{
	private static final long serialVersionUID = 1L;
	private static final int POINT_RADIUS = 5;
	static final int POINT_SIZE = POINT_RADIUS * 2;
	public enum PointRelation
	{	ABSOLUTE,
		ADD_TO_PREVIOUS_COORDINATE,
	}

//TODO: vielleicht sollte man dem XGPoint eine Gradient-Variable zugestehen, die angibt,
//		in welche Richtung (zum origin, zu nächsten XGPoint, zum min oder max) eine Linie strebt und wie schnell (wäre für EQ und Filterhüllkrve hilfreich)

/******************************************************************************************************/

	private final XGValue valueX; private XGValue valueY;
	private XGPointPanel panel;
	private final PointRelation relationX, relationY;
	private final int index;
	private XGPoint previous = null, next = null;
	private final XGTooltip tooltip = new XGTooltip();
	private Graphics2D g2;

	public XGPoint(int index, XGValue valX, XGValue valY, PointRelation relX, PointRelation relY)
	{	this.index = index;
		this.valueX = valX;
		this.valueY = valY;
		this.relationX = relX;
		this.relationY = relY;
		this.setSize(POINT_SIZE + 1, POINT_SIZE + 1);
		this.setPreferredSize(this.getSize());
		if(!(this.isMovable())) this.setVisible(false);
	}

	private boolean isMovable()
	{	XGParameter xP = this.valueX.getParameter(), yP = this.valueY.getParameter();
		return xP.getMaxIndex() - xP.getMinIndex() != 0 || yP.getMaxIndex() - yP.getMinIndex() != 0;
	}

	void setPanel(XGPointPanel pnl)
	{	this.panel = pnl;
		if(this.index != 0)
		{	this.previous = this.panel.getPoints().get(this.index - 1);
			this.previous.next = this;
		}
		this.valueX.getValueListeners().add((XGValue v)->this.panel.setSelectedValue(v));
		this.valueY.getValueListeners().add((XGValue v)->this.panel.setSelectedValue(v));
		this.setLocation();
	}

	void setYValue(XGValue v)
	{	this.valueY = v;
		if(this.isMovable())
		{	this.valueY.getValueListeners().add((XGValue)->this.panel.setSelectedValue(v));
			this.setVisible(true);
		}
		else this.setVisible(false);
		this.panel.repaint();
	}

	void setLocation()
	{	int x = 0, y = 0, xe = this.panel.getWidth(), ye = this.panel.getHeight();
		
		switch(this.relationX)
		{	default:
			case ABSOLUTE:
				x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, xe);
				break;
			case ADD_TO_PREVIOUS_COORDINATE:
				if(this.previous != null) x = this.previous.getX() + POINT_RADIUS;
				x += XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, xe);
				break;
//			case SUB_FROM_NEXT_COORDINATE:
//				if(this.next != null) x = this.next.getX() + POINT_RADIUS;
//				else x = xe;
//				x -= XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, xe);
//				break;
		}
		switch(this.relationY)
		{	default:
			case ABSOLUTE:
				y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMaxYIndex(), this.panel.getMinYIndex(), 0, ye);
				break;
			case ADD_TO_PREVIOUS_COORDINATE:
				if(this.previous != null) y = this.previous.getY() + POINT_RADIUS;
				y -= XGMath.linearIO(this.valueY.getIndex(), this.panel.getMaxYIndex(), this.panel.getMinYIndex(), 0, ye);
				break;
//			case SUB_FROM_NEXT_COORDINATE:
//				if(this.next != null) y = this.next.getY() + POINT_RADIUS;
//				else y = ye;
//				y -= XGMath.linearIO(this.valueY.getIndex(), this.panel.getMaxYIndex(), this.panel.getMinYIndex(), 0, ye);
//				break;
		}
//		x = XGMath.linearIO(this.valueX.getIndex(), this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.getWidth());
//		y = XGMath.linearIO(this.valueY.getIndex(), this.panel.getMaxYIndex(), this.panel.getMinYIndex(), 0, this.getHeight());

		this.setLocation(x - POINT_RADIUS, y - POINT_RADIUS);
		this.tooltip.setName(this.toString());
		if(this.isShowing())
		{	Point p = this.getLocationOnScreen();
			this.tooltip.setLocation(p.x + POINT_SIZE, p.y + POINT_SIZE);
		}
	}

	//XGValue getValueX()
	//{	return this.valueX;
	//}
	//
	//XGValue getValueY()
	//{	return this.valueY;
	//}

	@Override public String toString()
	{	return this.valueX.getInfo() + "/" + this.valueY.getInfo();
	}

	//@Override public void mousePressed(MouseEvent e)
	//{	XGUI.ENVIRONMENT.mousePressed = true;
	//	XGUI.ENVIRONMENT.dragEvent = e;
	//	this.tooltip.setVisible(true);
	//	e.consume();
	//}
	//
	//@Override public void mouseReleased(MouseEvent e)
	//{	XGUI.ENVIRONMENT.mousePressed = false;
	//	XGUI.ENVIRONMENT.dragEvent = e;
	//	this.tooltip.setVisible(false);
	//	e.consume();
	//}
	//
	//@Override public void mouseEntered(MouseEvent e)
	//{	if(!XGUI.ENVIRONMENT.mousePressed)
	//	{	Point p = e.getLocationOnScreen();
	//		this.tooltip.setLocation(p.x + POINT_SIZE, p.y + POINT_SIZE);
	//		this.tooltip.setVisible(true);
	//	}
	//}
	//
	//@Override public void mouseExited(MouseEvent e)
	//{	if(!XGUI.ENVIRONMENT.mousePressed) this.tooltip.setVisible(false);
	//}
	//
	//@Override public void mouseDragged(MouseEvent e)
	//{	this.valueX.addIndex(e.getXOnScreen() - XGUI.ENVIRONMENT.dragEvent.getXOnScreen(), true);
	//	this.valueY.addIndex(XGUI.ENVIRONMENT.dragEvent.getYOnScreen() - e.getYOnScreen(), true);
	//	this.setLocation();
	//	XGUI.ENVIRONMENT.dragEvent = e;
	//	e.consume();
	//}
	//
	//@Override public void mouseClicked(MouseEvent e)
	//{	this.requestFocusInWindow();
	//	this.repaint();
	//}
	//
	//@Override public void mouseMoved(MouseEvent e)
	//{
	//}

	@Override protected void paintComponent(Graphics g)
	{	super.paintComponent(g);
		this.g2 = (Graphics2D)g.create();
		this.g2.addRenderingHints(AALIAS);
		this.g2.setColor(COL_BAR_FORE);
		if(this.valueX.equals(this.panel.getSelectedValue()) || this.valueY.equals(this.panel.getSelectedValue())) this.g2.fillOval(0, 0, POINT_SIZE, POINT_SIZE);
		else this.g2.drawOval(0, 0, POINT_SIZE, POINT_SIZE);
		this.g2.dispose();
	}
}
