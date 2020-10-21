package gui;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import gui.XGPoint.PointRelation;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGFilterCurve extends XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress HPF_FREQ = new XGAddress("10//32"), HPF_RES = new XGAddress("10//33"), LPF_FREQ = new XGAddress("8//24"), LPF_RES = new XGAddress("8//25");
	private static final int Q_VALUE = 10;
	private static final XGFixedValue FILTER_Q = new XGFixedValue("fix", Q_VALUE);

/***********************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue hpf_freq, hpf_res, lpf_freq, lpf_res;
	private final XGTooltip tooltip = new XGTooltip();

	public XGFilterCurve(XMLNode n, XGModule mod) throws InvalidXGAddressException
	{	super(n, mod);
		this.hpf_freq = mod.getValues().get(HPF_FREQ.complement(mod.getAddress()));
		this.hpf_res = mod.getValues().get(HPF_RES.complement(mod.getAddress()));
		this.lpf_freq = mod.getValues().get(LPF_FREQ.complement(mod.getAddress()));
		this.lpf_res = mod.getValues().get(LPF_RES.complement(mod.getAddress()));

		this.borderize();
		this.setLayout(null);

		this.panel = new XGPointPanel(this, n);
		this.panel.setLimits(0, 255, 0, 191);
		this.panel.setUnits("Frequency", "Resonance");

		this.panel.add(new XGPoint(0, this.hpf_freq, XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, FILTER_Q, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, FILTER_Q, this.hpf_res, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ADD_TO_PREVIOUS_VALUE));
		this.panel.add(new XGPoint(3, FILTER_Q, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));

		this.panel.add(new XGPoint(4, FILTER_Q, XGFixedValue.VALUE_64, PointRelation.SUB_FROM_NEXT_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(5, FILTER_Q, this.lpf_res, PointRelation.SUB_FROM_NEXT_COORDINATE, PointRelation.ADD_TO_PREVIOUS_VALUE));
		this.panel.add(new XGPoint(6, this.lpf_freq, XGFixedValue.VALUE_0, PointRelation.SUB_FROM_NEXT_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(7, new XGFixedValue("fix", 255), XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));


		this.add(this.panel);
	}
}
