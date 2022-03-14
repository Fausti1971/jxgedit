package gui;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import adress.XGAddress;
import gui.XGPoint.PointRelation;
import value.XGFixedValue;
import value.XGValue;
import javax.swing.*;

public class XGVEG extends JPanel implements MouseMotionListener, XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress
		DEPTH = new XGAddress("8//12"),
		OFFSET = new XGAddress("8//13"),
		LOW = new XGAddress("8//109"),
		HIGH = new XGAddress("8//110");

/***************************************************************************************/
//TODO: Mach neu! die Kurvensteilheit stimmt nicht mehr, wenn die Velocity-Limits geändert werden...
	private final XGPointPanel panel;
	private final XGValue depth, offset, low, high;
	private final XGTooltip tooltip = new XGTooltip();

	public XGVEG(XGValue dep, XGValue off, XGValue lo, XGValue hi)
	{	this.depth = dep;
		this.offset = off;

		if(lo == null) this.low = new XGFixedValue("Limit low", 1);
		else this.low = lo;

		if(hi == null) this.high = new XGFixedValue("Limit High", 127);
		else this.high = hi;

		if(dep == null || off == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.panel = null;
			return;
		}
		this.setName("Velocity");
		this.borderize();

		this.panel = new XGPointPanel(1, 1, 0, 64, 1, 127, 64, 127);
		this.panel.setUnits("Velocity", "Volume");

		this.panel.add(new XGPoint(0, this.low, new XGFixedValue("", 0), PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.low, this.offset, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, this.high, this.depth, PointRelation.ABSOLUTE, PointRelation.ADD_TO_PREVIOUS_COORDINATE));
		this.panel.add(new XGPoint(3, this.high, new XGFixedValue("", 0), PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.panel.addMouseMotionListener(this);
		this.panel.addMouseListener(this);
		this.panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

//x1 = low
//y1 = offset
//x2 = high
//y2 = offset + depth
		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);
	}

	private String getInfo()
	{	return this.depth.getInfo() + "/" + this.offset.getInfo();
	}

	@Override public void mouseDragged(MouseEvent e)
	{	this.depth.addIndex(e.getXOnScreen() - VARIABLES.dragEvent.getXOnScreen(), true);
		this.offset.addIndex(VARIABLES.dragEvent.getYOnScreen() - e.getYOnScreen(), true);
		this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		this.tooltip.setVisible(true);
		VARIABLES.dragEvent = e;
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e)
	{	this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		if(!VARIABLES.mousePressed) this.tooltip.setVisible(true);
	}

	@Override public void mouseExited(MouseEvent e)
	{	if(!VARIABLES.mousePressed) this.tooltip.setVisible(false);
	}

	@Override public void mouseMoved(MouseEvent e)
	{	if(VARIABLES.mousePressed) return;
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
	}
}
