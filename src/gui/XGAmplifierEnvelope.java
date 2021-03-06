package gui;

import application.XGStrings;import gui.XGPoint.PointRelation;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;import xml.XMLNode;

public class XGAmplifierEnvelope extends XGFrame implements XGComponent
{
	private static final long serialVersionUID = 1L;

	static XGAmplifierEnvelope newAmplifierEnvelope(XGModule mod, XMLNode node)
	{	String[] names = new String[3];
		XGStrings.splitCSV(node.getStringAttribute(ATTR_VALUE_TAG)).toArray(names);
		XGValue a = mod.getValues().get(names[0]), d = mod.getValues().get(names[1]), r = mod.getValues().get(names[2]);
		return new XGAmplifierEnvelope(a, d, r);
	}

/**************************************************************************************/

	public XGAmplifierEnvelope(XGValue atk, XGValue dec, XGValue rel)
	{	super("");
		XGPointPanel panel;
		if(atk == null || dec == null || rel == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			return;
		}

		int maxX = atk.getParameter().getMaxIndex() - atk.getParameter().getMinIndex();
		maxX += dec.getParameter().getMaxIndex() - dec.getParameter().getMinIndex();
		maxX += rel.getParameter().getMaxIndex() - rel.getParameter().getMinIndex();

		panel = new XGPointPanel(1, 5, 0, 0, 0, maxX, 0, 127);
		panel.setUnits("Time", "Gain");

		panel.add(new XGPoint(0, XGFixedValue.VALUE_0, XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(1, atk, XGFixedValue.VALUE_127, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(2, dec, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(3, rel, XGFixedValue.VALUE_0, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));

		this.add(panel, "0,0,1,1");
	}
}
