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
	static final int POINT_SIZE = POINT_RADIUS * 2;
	public static enum PointRelation{ABSOLUTE, PREVIOUS_VALUE, ADD_PREVIOUS_VALUE, SUB_PREVIOUS_VALUE, ADD_PREVIOUS_COORDINATE};

/******************************************************************************************************/

	private XGValue valueX, valueY;
	private XGPointPanel panel;
	private final PointRelation relationX, relationY;
	private final int index;
	private XGPoint previous = null;
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
		if(this.isMovable())
		{	this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.setToolTipText("");
		}
		else this.setVisible(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private boolean isMovable()
	{	XGParameter xP = this.valueX.getParameter(), yP = this.valueY.getParameter();
		return xP.getMaxIndex() - xP.getMinIndex() != 0 || yP.getMaxIndex() - yP.getMinIndex() != 0;
	}

	void setPanel(XGPointPanel pnl)
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
	{	int x = 0, y = 0;
		int xi = this.valueX.getIndex(), yi = this.valueY.getIndex(), pxi = 0, pyi = 0;
		if(this.previous != null)
		{	pxi = this.previous.getValueX().getIndex();
			pyi = this.previous.getValueY().getIndex();
			switch(this.relationX)
			{	case ABSOLUTE:				break;
				case ADD_PREVIOUS_VALUE:	xi += pxi; break;
				case SUB_PREVIOUS_VALUE:	xi -= pxi; break;
				case PREVIOUS_VALUE:		xi = pxi; break;
				case ADD_PREVIOUS_COORDINATE:	x = this.previous.getX() + POINT_RADIUS; break;
				default:					break;
			}
			switch(this.relationY)
			{	case ABSOLUTE:				break;
				case ADD_PREVIOUS_VALUE:	yi += pyi; break;
				case SUB_PREVIOUS_VALUE:	yi -= pyi; break;
				case PREVIOUS_VALUE:		yi = pyi; break;
				case ADD_PREVIOUS_COORDINATE:	y = this.previous.getY() + POINT_RADIUS; break;
				default:					break;
			}
		}
		x += XGMath.linearIO(xi, this.panel.getMinXIndex(), this.panel.getMaxXIndex(), 0, this.panel.getWidth());
		y += XGMath.linearIO(yi, this.panel.getMinYIndex(), this.panel.getMaxYIndex(), this.panel.getHeight(), 0);

		this.setLocation(x - POINT_RADIUS, y - POINT_RADIUS);
		this.tooltip.setName(this.toString());
		if(this.isShowing())
		{	Point p = this.getLocationOnScreen();
			p.x += POINT_SIZE;
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
	{	XGComponent.mousePressed = true;
		XGComponent.dragEvent = e;
		this.tooltip.setVisible(true);
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	XGComponent.mousePressed = false;
		XGComponent.dragEvent = e;
		this.tooltip.setVisible(false);
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e)
	{	if(!XGComponent.mousePressed)
		{	this.tooltip.setLocation(e.getLocationOnScreen().x + POINT_SIZE, e.getLocationOnScreen().y + POINT_SIZE);
			this.tooltip.setVisible(true);
		}
	}

	@Override public void mouseExited(MouseEvent e)
	{	if(!XGComponent.mousePressed) this.tooltip.setVisible(false);
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
