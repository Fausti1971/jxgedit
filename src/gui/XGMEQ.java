package gui;

import java.awt.GridBagLayout;
import adress.XGAddress;
import adress.XGAddressableSet;
import gui.XGPoint.PointRelation;
import module.XGModule;
import tag.*;import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGMEQ extends XGFrame
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress
		G1 = new XGAddress("2/64/1"),
		G2 = new XGAddress("2/64/5"),
		G3 = new XGAddress("2/64/9"),
		G4 = new XGAddress("2/64/13"),
		G5 = new XGAddress("2/64/17"),
		F1 = new XGAddress("2/64/2"),
		F2 = new XGAddress("2/64/6"),
		F3 = new XGAddress("2/64/10"),
		F4 = new XGAddress("2/64/14"),
		F5 = new XGAddress("2/64/18"),
		Q1 = new XGAddress("2/64/3"),
		Q2 = new XGAddress("2/64/6"),
		Q3 = new XGAddress("2/64/11"),
		Q4 = new XGAddress("2/64/15"),
		Q5 = new XGAddress("2/64/19"),
		S1 = new XGAddress("2/64/4"),
		S5 = new XGAddress("2/64/20");

/*****************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue g1, g2, g3, g4, g5, f1, f2, f3, f4, f5, q1, q2, q3, q4, q5, s1, s5;
	private final int minX, maxX, minY, maxY, midY;

	public XGMEQ(XMLNode n, XGModule mod)
	{	super(n);
		this.borderize();

		XGTagableAddressableSet<XGValue> set = mod.getValues();
		this.g1 = set.get(G1);
		this.g2 = set.get(G2);
		this.g3 = set.get(G3);
		this.g4 = set.get(G4);
		this.g5 = set.get(G5);

		this.f1 = set.get(F1);
		this.f2 = set.get(F2);
		this.f3 = set.get(F3);
		this.f4 = set.get(F4);
		this.f5 = set.get(F5);

		this.q1 = set.get(Q1);
		this.q2 = set.get(Q2);
		this.q3 = set.get(Q3);
		this.q4 = set.get(Q4);
		this.q5 = set.get(Q5);

		this.s1 = set.get(S1);
		this.s5 = set.get(S5);

		this.minX = this.f1.getParameter().getMinIndex();
		this.maxX = this.f5.getParameter().getMaxIndex();
		this.minY = this.g1.getParameter().getMinIndex();
		this.maxY = this.g1.getParameter().getMaxIndex();
		this.midY = (this.maxY - this.minY)/2 + this.minY;

		this.panel = new XGPointPanel(n, this.minX, this.maxX, this.minY, this.maxY);
		this.panel.setUnits("Hz", "dB");

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);

		this.panel.add(new XGPoint(0, new XGFixedValue("fixed", this.minX), this.g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.f1, this.g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, this.f2, this.g2, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, this.f3, this.g3, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, this.f4, this.g4, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(5, this.f5, this.g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(6, new XGFixedValue("fixed", this.maxX), this.g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.s1.addValueListener((XGValue s)->{this.setShape(s.getValue(), 0, this.g1);});
		this.s5.addValueListener((XGValue s)->{this.setShape(s.getValue(), 6, this.g5);});
	}

	private void setShape(int s, int i, XGValue g)
	{	XGPoint p = this.panel.getPoints().get(i);
		if(s == 0) p.setYValue(g);
		else p.setYValue(new XGFixedValue("fixed", this.midY));
	}
}
