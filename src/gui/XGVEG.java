package gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import gui.XGPoint.PointRelation;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGVEG extends XGComponent implements MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress
		DEPTH = new XGAddress("8//12"),
		OFFSET = new XGAddress("8//13"),
		LOW = new XGAddress("8//109"),
		HIGH = new XGAddress("8//110");

/***************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue depth, offset, low, high;
	private final XGTooltip tooltip = new XGTooltip();

	public XGVEG(XMLNode n, XGModule mod) throws InvalidXGAddressException
	{	super(n, mod);
		this.depth = mod.getValues().get(DEPTH.complement(mod.getAddress()));
		this.offset = mod.getValues().get(OFFSET.complement(mod.getAddress()));
		this.low = mod.getValues().get(LOW.complement(mod.getAddress()));
		this.high = mod.getValues().get(HIGH.complement(mod.getAddress()));

		this.borderize();
		this.setLayout(null);
		this.panel = new XGPointPanel(this, n);
		this.panel.setLimits(0, 127, 64, 127);
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

		this.add(this.panel);
	}

	private String getInfo()
	{	return this.depth.getInfo() + "/" + this.offset.getInfo();
	}

	@Override public void mouseDragged(MouseEvent e)
	{	this.depth.addIndex(e.getXOnScreen() - XGComponent.dragEvent.getXOnScreen());
		this.offset.addIndex(XGComponent.dragEvent.getYOnScreen() - e.getYOnScreen());
		this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		this.tooltip.setVisible(true);
		XGComponent.dragEvent = e;
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e)
	{	super.mouseEntered(e);
		this.tooltip.setName(this.getInfo());
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
		if(!XGComponent.mousePressed) this.tooltip.setVisible(true);
	}

	@Override public void mouseExited(MouseEvent e)
	{	super.mouseExited(e);
		if(!XGComponent.mousePressed) this.tooltip.setVisible(false);
	}

	@Override public void mouseMoved(MouseEvent e)
	{	if(XGComponent.mousePressed) return;
		Point p = e.getLocationOnScreen();
		this.tooltip.setLocation(p.x + XGPoint.POINT_SIZE, p.y + XGPoint.POINT_SIZE);
	}
}
