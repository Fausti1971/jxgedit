package gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import gui.XGPoint.PointRelation;
import value.XGFixedValue;
import value.XGValue;

public class XGVEG extends XGFrame implements MouseMotionListener
{
	private static final long serialVersionUID = 1L;

/*********************************************************************************************************************/

	private final XGValue depth;
	private final XGValue offset;
	private final XGTooltip tooltip = new XGTooltip();

	public XGVEG(XGValue dep, XGValue off, XGValue lo, XGValue hi)
	{	super(true);
		this.depth = dep;
		this.offset = off;

		XGValue low;
		if(lo == null) low = new XGFixedValue("Limit low", 1);
		else low = lo;

		XGValue high;
		if(hi == null) high = new XGFixedValue("Limit High", 127);
		else high = hi;

	/***************************************************************************************/
		//TODO: Mach neu! die Kurvensteilheit stimmt nicht mehr, wenn die Velocity-Limits geändert werden...
		XGPointPanel panel;
		if(dep == null || off == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			return;
		}

		panel = new XGPointPanel(1, 1, 0, 64, 1, 127, 64, 127);
		panel.setUnits("Velocity", "Volume");

		panel.add(new XGPoint(0, low, new XGFixedValue("", 0), PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(1, low, this.offset, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(2, high, this.depth, PointRelation.ABSOLUTE, PointRelation.ADD_TO_PREVIOUS_COORDINATE));
		panel.add(new XGPoint(3, high, new XGFixedValue("", 0), PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		panel.addMouseMotionListener(this);
		panel.addMouseListener(this);
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		this.add(panel, "0,0,1,1");
	}

	private String getInfo(){	return this.depth.getInfo() + "/" + this.offset.getInfo();}

	@Override public void mouseDragged(MouseEvent e)
	{	this.depth.addIndex(e.getXOnScreen() - ENVIRONMENT.dragEvent.getXOnScreen(), true);
		this.offset.addIndex(ENVIRONMENT.dragEvent.getYOnScreen() - e.getYOnScreen(), true);
		this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		this.tooltip.setVisible(true);
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e)
	{	this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		if(!ENVIRONMENT.mousePressed) this.tooltip.setVisible(true);
	}

	@Override public void mouseExited(MouseEvent e)
	{	if(!ENVIRONMENT.mousePressed) this.tooltip.setVisible(false);
	}

	@Override public void mouseMoved(MouseEvent e)
	{	if(ENVIRONMENT.mousePressed) return;
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
	}
}
