package gui;

import application.XGMath;import application.XGStrings;import gui.XGPoint.PointRelation;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;import xml.XMLNode;import java.awt.*;import java.awt.geom.GeneralPath;

public class XGAmplifierEnvelope extends XGFrame implements XGComponent, XGShaper
{
	private static final int MAX_X = 4 * 128;//attack + decay + sustain + release;

	static XGAmplifierEnvelope newAmplifierEnvelope(XGModule mod, XMLNode node)throws XGComponentException
	{	String[] tags = new String[3];
		XGStrings.splitCSV(node.getStringAttribute(ATTR_VALUE_TAG)).toArray(tags);
		XGValue a = mod.getValues().get(tags[0]), d = mod.getValues().get(tags[1]), r = mod.getValues().get(tags[2]);
		return new XGAmplifierEnvelope(a, d, r);
	}

/**************************************************************************************/

	private final XGValue attack, decay, release;

	public XGAmplifierEnvelope(XGValue atk, XGValue dec, XGValue rel)throws XGComponentException
	{	super("");
		XGPointPanel panel;
		if(atk == null || dec == null || rel == null) throw new XGComponentException("value is null");
		this.attack = atk;
		this.decay = dec;
		this.release = rel;

		panel = new XGPointPanel(this, 1, 5, 0, 0, 0, MAX_X, 0, 127);
		panel.setUnits("Time", "Gain");

		this.attack.getValueListeners().add((XGValue v)->{panel.repaint();});
		this.decay.getValueListeners().add((XGValue v)->{panel.repaint();});
		this.release.getValueListeners().add((XGValue v)->{panel.repaint();});

		this.add(panel, "0,0,1,1");
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		float maxWidth = r.width / 4F;
		float minWidth = maxWidth / 10;
		float midY = r.height/2F;

		float atk = XGMath.linearScale(this.attack.getValue(), 0, 127, r.x + minWidth, r.x + maxWidth);
		float dec = XGMath.linearScale(this.decay.getValue(), 0, 127, atk + minWidth, atk + maxWidth);
		float sus = dec + maxWidth;
		float rel = XGMath.linearScale(this.release.getValue(), 0, 127, sus + minWidth, sus + maxWidth);

		gp.moveTo(r.x, r.height);
		gp.lineTo(atk, r.y);
		gp.lineTo(dec, midY);
		gp.lineTo(sus, midY);
		gp.lineTo(rel, r.height);

		return gp;
	}
}
