package gui;
import java.awt.GridBagLayout;
import adress.XGAddressRange;
import gui.XGPoint.PointRelation;
import value.XGFixedValue;
import value.XGValue;
import javax.swing.*;

public class XGPEG extends JPanel implements XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final XGAddressRange A_TIME = new XGAddressRange("8//106"), A_LEVEL = new XGAddressRange("8//105"), R_TIME = new XGAddressRange("8//108"), R_LEVEL = new XGAddressRange("8//107");
	private static final XGFixedValue VALUE_255 = new XGFixedValue("fix", 255), VALUE_381 = new XGFixedValue("fix", 381);

/**************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue a_time, a_level, r_time, r_level;

	public XGPEG(XGValue al, XGValue at, XGValue rl, XGValue rt)
	{	this.a_level = al;
		this.a_time = at;
		this.r_level = rl;
		this.r_time = rt;
		if(al == null || at == null || rl == null || rt == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.panel = null;
			return;
		}
		this.setName("Pitch Envelope Generator");

		this.panel = new XGPointPanel(1, 2, 0, 64, 0, 381, 0, 127);
		this.panel.setUnits("Time", "Pitch");

		this.panel.add(new XGPoint(0, new XGFixedValue("fixed",  0), this.a_level, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.a_time, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, VALUE_255, XGFixedValue.VALUE_64, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, this.r_time, this.r_level, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(4, VALUE_381, this.r_level, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);
	}
}
