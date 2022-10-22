package gui;

import application.XGMath;import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import value.XGValueChangeListener;import javax.swing.*;import java.awt.*;import java.awt.geom.GeneralPath;

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

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);
	}

	public void contentChanged(XGValue v)
	{	this.panel.repaint();
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		if(this.rate.getValue() == 0) return gp;
		int midY = r.height/2;
		int maxDelayPoint = XGMath.linearScale(127, 0, 576, 0, r.width);
		int maxWavelength = r.width - maxDelayPoint;
		int minWavelength = Math.max(2, r.width / 655);//muss mindestens 2 sein, damit halfWavelength nicht 0 wird (Speicherüberlauf);
		int halfWavelength = XGMath.linearScale(this.rate.getValue(), this.rate.getParameter().getMaxValue(), this.rate.getParameter().getMinValue(), minWavelength, maxWavelength)/2;
		int amplitude = XGMath.linearScale(this.depth.getValue(), 0, 127, -r.height, r.height);
		Point target = new Point(r.x, midY), control = new Point();
		gp.moveTo(target.x, target.y);

		target.x = XGMath.linearScale(this.delay.getValue(), this.delay.getParameter().getMinValue(), this.delay.getParameter().getMaxValue(), r.x, maxDelayPoint);
		gp.lineTo(target.x, target.y);

		control.x = target.x - halfWavelength/2;
		while(target.x < r.width)
		{	target.x += halfWavelength;
			control.x += halfWavelength;
			control.y = midY - amplitude;
			gp.quadTo(control.x, control.y, target.x, target.y);

			target.x += halfWavelength;
			control.x += halfWavelength;
			control.y = midY + amplitude;
			gp.quadTo(control.x, control.y, target.x, target.y);
		}
		return gp;
	}
}
