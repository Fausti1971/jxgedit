package gui;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import gui.XGPoint.PointRelation;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGPEG extends XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress A_TIME = new XGAddress("8//106"), A_LEVEL = new XGAddress("8//105"), R_TIME = new XGAddress("8//108"), R_LEVEL = new XGAddress("8//107");
	private static final XGFixedValue VALUE_255 = new XGFixedValue("fix", 255), VALUE_381 = new XGFixedValue("fix", 381);

/**************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue a_time, a_level, r_time, r_level;

	public XGPEG(XMLNode n, XGModule mod) throws XGMemberNotFoundException, InvalidXGAddressException
	{	super(n, mod);
		this.borderize();
		this.setLayout(null);

		this.a_time = mod.getValues().get(A_TIME.complement(mod.getAddress()));
		this.a_level = mod.getValues().get(A_LEVEL.complement(mod.getAddress()));
		this.r_time = mod.getValues().get(R_TIME.complement(mod.getAddress()));
		this.r_level = mod.getValues().get(R_LEVEL.complement(mod.getAddress()));

		this.panel = new XGPointPanel(this, n);
		this.panel.setLimits(0, 381, 0, 127);
		this.panel.setUnits("Time", "Pitch");

		this.panel.add(new XGPoint(0, new XGFixedValue("fixed",  0), this.a_level, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.a_time, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, VALUE_255, XGFixedValue.VALUE_64, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, this.r_time, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, VALUE_381, this.r_level, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.add(this.panel);
	}
}
