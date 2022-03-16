package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import gui.XGPoint.PointRelation;
import value.XGFixedValue;
import value.XGValue;
import javax.swing.*;

public class XGScale extends JPanel implements XGComponent
{
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************/

	private final XGValue val_c, val_cis, val_d, val_dis, val_e, val_f, val_fis, val_g, val_gis, val_a, val_ais, val_b;
	private final XGPointPanel panel;

	public XGScale(XGValue c, XGValue cis, XGValue d, XGValue dis, XGValue e, XGValue f, XGValue fis, XGValue g, XGValue gis, XGValue a, XGValue ais, XGValue b)
	{
		this.val_c = c;
		this.val_cis = cis;
		this.val_d = d;
		this.val_dis = dis;
		this.val_e = e;
		this.val_f = f;
		this.val_fis = fis;
		this.val_g = g;
		this.val_gis = gis;
		this.val_a = a;
		this.val_ais = ais;
		this.val_b = b;

		if(c == null || cis == null || d == null || dis == null || e == null || f == null || fis == null || g == null || gis == null || a == null || ais == null || b == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.panel = null;
			return;
		}
		this.setName("Scaling");
		this.borderize();
		this.panel = new XGPointPanel(1, 0, 0, 64, 0, 11, 0, 127);
		this.panel.setUnits("Note", "Pitch");
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		this.add(this.panel, gbc);

		this.panel.add(new XGPoint(0, new XGFixedValue("fix", 0), val_c, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, new XGFixedValue("fix", 1), val_cis, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, new XGFixedValue("fix", 2), val_d, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, new XGFixedValue("fix", 3), val_dis, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, new XGFixedValue("fix", 4), val_e, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(5, new XGFixedValue("fix", 5), val_f, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(6, new XGFixedValue("fix", 6), val_fis, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(7, new XGFixedValue("fix", 7), val_g, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(8, new XGFixedValue("fix", 8), val_gis, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(9, new XGFixedValue("fix", 9), val_a, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(10, new XGFixedValue("fix", 10), val_ais, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(11, new XGFixedValue("fix", 11), val_b, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
	}
}
