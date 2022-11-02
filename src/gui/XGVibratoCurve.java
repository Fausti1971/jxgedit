package gui;

import application.XGMath;import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import value.XGValueChangeListener;import javax.swing.*;import java.awt.*;import java.awt.geom.GeneralPath;import java.awt.geom.Point2D;

public class XGVibratoCurve extends XGFrame implements XGValueChangeListener, XGShaper
{
//maxDelay ca. 4s
//minFreq 0,08Hz (= 12,5s/Welle)
//width = 16,5s (4s + 12,5s) damit bei MaxDelay und minFreq wenistens eine komplette Welle dargestellt wird
//das bedeutet, dass bei minDelay (0s) und maxFreq (39,7Hz) 655,05 Wellen dargestellt werden müssen!
//minX = 0;
//maxX = (4s + 12,5s) 128 + 448 = 576;
//minY = 0;
//maxY = 127;
//oriX = 0;
//oriY = 64; 

	private static final String DELAY = "mp_vib_delay", DEPTH = "mp_vib_depth", RATE = "mp_vib_rate";

/************************************************************************************************************************/

	private final XGValue rate, depth, delay;
	private final XGPointPanel panel;

	public XGVibratoCurve(XGModule module)throws XGComponentException
	{	super("");
		try
		{	XGTagableAddressableSet<XGValue> values = module.getValues();
			this.rate = values.get(RATE);
			this.depth = values.get(DEPTH);
			this.delay = values.get(DELAY);
			this.rate.getValueListeners().add(this);
			this.depth.getValueListeners().add(this);
			this.delay.getValueListeners().add(this);
		}
		catch(NullPointerException e)
		{	throw new XGComponentException(e.getMessage());
		}
		this.panel = new XGPointPanel(this, 1, 0, 0, 64, 0, 576, 0, 127);
		this.panel.setUnits("Time", "Level");

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);
	}

	public void contentChanged(XGValue v)
	{	this.panel.repaint();
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		if(this.rate.getValue() == 0) return gp;
		float midY = r.height/2F;
		float maxDelayPoint = XGMath.linearScale(127, 0, 576, 0, r.width);
		float maxWavelength = r.width - maxDelayPoint;
		float minWavelength = Math.max(2, r.width / 655);//muss mindestens 2 sein, damit halfWavelength nicht 0 wird (Speicherüberlauf);
		float halfWavelength = XGMath.linearScale(this.rate.getValue(), this.rate.getParameter().getMaxValue(), this.rate.getParameter().getMinValue(), minWavelength, maxWavelength)/2;
		float amplitude = XGMath.linearScale(this.depth.getValue(), 0, 127, -(r.height - r.y), r.height - r.y);
		float targetX = r.x, targetY = midY, controlX, controlY;
		gp.moveTo(targetX, targetY);

		targetX = XGMath.linearScale(this.delay.getValue(), this.delay.getParameter().getMinValue(), this.delay.getParameter().getMaxValue(), r.x, maxDelayPoint);
		gp.lineTo(targetX, targetY);

		controlX = targetX - halfWavelength/2;
		while(targetX < r.width)
		{	targetX += halfWavelength;
			controlX += halfWavelength;
			controlY = midY - amplitude;
			gp.quadTo(controlX, controlY, targetX, targetY);

			targetX += halfWavelength;
			controlX += halfWavelength;
			controlY = midY + amplitude;
			gp.quadTo(controlX, controlY, targetX, targetY);
		}
		return gp;
	}
}
