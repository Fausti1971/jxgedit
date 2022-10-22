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
	private final int midY;

	public XGMEQ(XGModule mod)throws XGComponentException
	{
		if(mod == null) throw new XGComponentException("module is null");

		XGTagableAddressableSet<XGValue> set = mod.getValues();
		XGValue g1 = set.get(G1);
		XGValue g2 = set.get(G2);
		XGValue g3 = set.get(G3);
		XGValue g4 = set.get(G4);
		XGValue g5 = set.get(G5);

		XGValue f1 = set.get(F1);
		XGValue f2 = set.get(F2);
		XGValue f3 = set.get(F3);
		XGValue f4 = set.get(F4);
		XGValue f5 = set.get(F5);

		XGValue q1 = set.get(Q1);
		XGValue q2 = set.get(Q2);
		XGValue q3 = set.get(Q3);
		XGValue q4 = set.get(Q4);
		XGValue q5 = set.get(Q5);

		XGValue s1 = set.get(S1);
		XGValue s5 = set.get(S5);

		int minX = f1.getParameter().getMinIndex();
		int maxX = f5.getParameter().getMaxIndex();
		int minY = g1.getParameter().getMinIndex();
		int maxY = g1.getParameter().getMaxIndex();
		this.midY = (maxY - minY)/2 + minY;

		this.panel = new XGPointPanel(null, 1, 5, 0, 0, minX, maxX, minY, maxY);
		this.panel.setUnits("Hz", "dB");
		this.panel.setName("");

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);

		this.panel.add(new XGPoint(0, new XGFixedValue("fixed", minX), g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, f1, g1, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, f2, g2, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, f3, g3, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, f4, g4, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(5, f5, g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(6, new XGFixedValue("fixed", maxX), g5, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		s1.getValueListeners().add((XGValue s)->this.setShape(s.getValue(), 0, g1));
		s5.getValueListeners().add((XGValue s)->this.setShape(s.getValue(), 6, g5));
	}

	private void setShape(int s, int i, XGValue g)
	{	XGPoint p = this.panel.getPoints().get(i);
		if(s == 0) p.setYValue(g);
		else p.setYValue(new XGFixedValue("fixed", this.midY));
	}
}
