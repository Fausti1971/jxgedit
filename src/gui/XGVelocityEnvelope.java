package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;import java.awt.geom.AffineTransform;import java.awt.geom.Area;import java.awt.geom.GeneralPath;
import application.XGMath;import gui.XGPoint.PointRelation;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;import value.XGValueChangeListener;

public class XGVelocityEnvelope extends XGFrame implements MouseMotionListener, XGShaper, XGValueChangeListener
{
	private static final String LOLIM = "mp_velo_lo", SENSE = "mp_velo_depth", OFFSET = "mp_velo_offset", HILIM = "mp_velo_hi"; 

/*********************************************************************************************************************/

	private final XGValue depth, offset, low, high;
	private final XGPointPanel panel;

	public XGVelocityEnvelope(XGModule mod)throws XGComponentException
	{	super("");
		this.depth = mod.getValues().get(SENSE);
		this.offset = mod.getValues().get(OFFSET);
		if(this.depth == null) throw new XGComponentException("velo depth value is null");
		if(this.offset == null) throw new XGComponentException("velo offset value is null");

		this.depth.getValueListeners().add(this);
		this.offset.getValueListeners().add(this);

		XGValue lo = mod.getValues().get(LOLIM);
		if(lo != null)
		{	this.low = lo;
			this.low.getValueListeners().add(this);
		}
		else this.low = new XGFixedValue("Limit low", 1);

		XGValue hi = mod.getValues().get(HILIM);
		if(hi != null)
		{	this.high = hi;
			this.high.getValueListeners().add(this);
		}
		else this.high = new XGFixedValue("Limit High", 127);

		this.panel = new XGPointPanel(this, 1, 1, 0, 64, 0, 127, 0, 127);
		this.panel.setUnits("Velocity", "Volume");

		this.panel.addMouseMotionListener(this);
		this.panel.addMouseListener(this);
		this.panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		this.add(this.panel, "0,0,1,1");
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		Rectangle rLim = new Rectangle(r);
		rLim.x = XGMath.linearScale(this.low.getValue(), 1, 127, r.x, r.width);
		rLim.width = XGMath.linearScale(this.high.getValue(), 1, 127, r.x, r.width) - rLim.x;

		int o = XGMath.linearScale(this.offset.getValue(), 0, 127, r.height, -r.height);
		int d = XGMath.linearScale(this.depth.getValue(), 0, 127, r.height, -r.height);

		gp.moveTo(r.x, r.height);
		gp.lineTo(r.x, r.height + o);
		gp.lineTo(r.width, d + o);
		gp.lineTo(r.width, r.height);

		Area r1 = new Area(gp);
		Area r2 = new Area(rLim);
		r1.intersect(r2);

		return new GeneralPath(r1);

//		return gp;
	}

	@Override public void mouseDragged(MouseEvent e)
	{	this.depth.addIndex(e.getXOnScreen() - ENVIRONMENT.dragEvent.getXOnScreen(), true);
		this.offset.addIndex(ENVIRONMENT.dragEvent.getYOnScreen() - e.getYOnScreen(), true);
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override public void mouseEntered(MouseEvent e){}

	@Override public void mouseExited(MouseEvent e){}

	@Override public void mouseMoved(MouseEvent e){}

	@Override public void contentChanged(XGValue v)
	{	this.panel.repaint();
	}
}
