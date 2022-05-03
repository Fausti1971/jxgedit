package gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import gui.XGPoint.PointRelation;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;

public class XGVelocityEnvelope extends XGFrame implements MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private static final String LOLIM = "mp_velo_lo", SENSE = "mp_velo_depth", OFFSET = "mp_velo_offset", HILIM = "mp_velo_hi"; 

/*********************************************************************************************************************/

	private final XGValue depth;
	private final XGValue offset;
	private final XGTooltip tooltip = new XGTooltip();

	public XGVelocityEnvelope(XGModule mod)
	{	super(true);
		this.depth = mod.getValues().get(SENSE);
		this.offset = mod.getValues().get(OFFSET);

		XGValue low = mod.getValues().get(LOLIM);
		if(low == null) low = new XGFixedValue("Limit low", 1);

		XGValue high = mod.getValues().get(HILIM);
		if(high == null) high = new XGFixedValue("Limit High", 127);

	/***************************************************************************************/
		//TODO: Mach neu! die Kurvensteilheit stimmt nicht mehr, wenn die Velocity-Limits geändert werden...
		XGPointPanel panel;
		if(this.depth == null || this.offset == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			return;
		}

		panel = new XGPointPanel(1, 1, 0, 64, 1, 127, 64, 127);
		panel.setUnits("Velocity", "Volume");

		panel.add(new XGPoint(0, low, XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(1, low, this.offset, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(2, high, this.depth, PointRelation.ABSOLUTE, PointRelation.ADD_TO_PREVIOUS_COORDINATE));
		panel.add(new XGPoint(3, high, XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

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
