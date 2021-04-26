package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import gui.XGPoint.PointRelation;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGScale extends XGFrame
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress
		ADR_C = new XGAddress("8//65"),
		ADR_CIS = new XGAddress("8//66"),
		ADR_D = new XGAddress("8//67"),
		ADR_DIS = new XGAddress("8//68"),
		ADR_E = new XGAddress("8//69"),
		ADR_F = new XGAddress("8//70"),
		ADR_FIS = new XGAddress("8//71"),
		ADR_G = new XGAddress("8//72"),
		ADR_GIS = new XGAddress("8//73"),
		ADR_A = new XGAddress("8//74"),
		ADR_AIS = new XGAddress("8//75"),
		ADR_B = new XGAddress("8//76");

/*****************************************************************************************************/

	private final XGValue val_c, val_cis, val_d, val_dis, val_e, val_f, val_fis, val_g, val_gis, val_a, val_ais, val_b;
	private final XGPointPanel panel;

	public XGScale(XMLNode n, XGModule mod) throws InvalidXGAddressException
	{	super(n);
		this.borderize();
		this.panel = new XGPointPanel(0, 1, 0, 64, 0, 11, 0, 127);
		this.panel.setUnits("Note", "Pitch");
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		this.add(this.panel, gbc);

		this.val_c = mod.getValues().get(ADR_C.complement(mod.getAddress()));
		this.val_cis = mod.getValues().get(ADR_CIS.complement(mod.getAddress()));
		this.val_d = mod.getValues().get(ADR_D.complement(mod.getAddress()));
		this.val_dis = mod.getValues().get(ADR_DIS.complement(mod.getAddress()));
		this.val_e = mod.getValues().get(ADR_E.complement(mod.getAddress()));
		this.val_f = mod.getValues().get(ADR_F.complement(mod.getAddress()));
		this.val_fis = mod.getValues().get(ADR_FIS.complement(mod.getAddress()));
		this.val_g = mod.getValues().get(ADR_G.complement(mod.getAddress()));
		this.val_gis = mod.getValues().get(ADR_GIS.complement(mod.getAddress()));
		this.val_a = mod.getValues().get(ADR_A.complement(mod.getAddress()));
		this.val_ais = mod.getValues().get(ADR_AIS.complement(mod.getAddress()));
		this.val_b = mod.getValues().get(ADR_B.complement(mod.getAddress()));

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
