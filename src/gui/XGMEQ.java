package gui;

import java.awt.GridBagLayout;
import gui.XGPoint.PointRelation;
import static gui.XGUI.DEF_GBC;import module.XGModule;
import tag.*;import value.XGFixedValue;
import value.XGValue;

public class XGMEQ extends javax.swing.JPanel
{
	public static final String
		G1 = "eq_gain1",
		G2 = "eq_gain2",
		G3 = "eq_gain3",
		G4 = "eq_gain4",
		G5 = "eq_gain5",
		F1 = "eq_freq1",
		F2 = "eq_freq2",
		F3 = "eq_freq3",
		F4 = "eq_freq4",
		F5 = "eq_freq5",
		Q1 = "eq_q1",
		Q2 = "eq_q2",
		Q3 = "eq_q3",
		Q4 = "eq_q4",
		Q5 = "eq_q5",
		S1 = "eq_shape1",
		S5 = "eq_shape5";

/*****************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue g1, g2, g3, g4, g5, f1, f2, f3, f4, f5, q1, q2, q3, q4, q5, s1, s5;
	private final int minX, maxX, minY, maxY, midY;

	public XGMEQ(XGModule mod)
	{
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

		this.panel = new XGPointPanel(1, 5, 0, 0, this.minX, this.maxX, this.minY, this.maxY);
		this.panel.setUnits("Hz", "dB");
		this.panel.setName("");

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);

		this.panel.add(new XGPoint(0, new XGFixedValue("fixed", this.minX), this.g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.f1, this.g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, this.f2, this.g2, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, this.f3, this.g3, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, this.f4, this.g4, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(5, this.f5, this.g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(6, new XGFixedValue("fixed", this.maxX), this.g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.s1.getValueListeners().add((XGValue s)->this.setShape(s.getValue(), 0, this.g1));
		this.s5.getValueListeners().add((XGValue s)->this.setShape(s.getValue(), 6, this.g5));
	}

	private void setShape(int s, int i, XGValue g)
	{	XGPoint p = this.panel.getPoints().get(i);
		if(s == 0) p.setYValue(g);
		else p.setYValue(new XGFixedValue("fixed", this.midY));
	}
}
