package gui;
import java.awt.GridBagLayout;
import adress.XGAddressRange;
import gui.XGPoint.PointRelation;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;import xml.XMLNode;
import javax.swing.*;

public class XGPitchEnvelope extends JPanel implements XGComponent
{
	private static final XGFixedValue VALUE_255 = new XGFixedValue("fix", 255), VALUE_381 = new XGFixedValue("fix", 381);

	static XGPitchEnvelope newPitchEnvelope(XGModule mod, XMLNode node)
	{	return new XGPitchEnvelope(mod.getValues().get("mp_peg_init_level"), mod.getValues().get("mp_peg_attack_time"), mod.getValues().get("mp_peg_release_level"), mod.getValues().get("mp_peg_release_time"));
	}

	public XGPitchEnvelope(XGValue al, XGValue at, XGValue rl, XGValue rt)
	{
/**************************************************************************************/

		XGPointPanel panel;
		if(al == null || at == null || rl == null || rt == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			return;
		}
		this.setName("Pitch Envelope Generator");

		panel = new XGPointPanel(1, 2, 0, 64, 0, 381, 0, 127);
		panel.setUnits("Time", "Pitch");

		panel.add(new XGPoint(0, new XGFixedValue("fixed",  0), al, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(1, at, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(2, VALUE_255, XGFixedValue.VALUE_64, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(3, rt, rl, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		panel.add(new XGPoint(4, VALUE_381, rl, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));

		this.setLayout(new GridBagLayout());
		this.add(panel, DEF_GBC);
	}
}
