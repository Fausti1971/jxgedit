package gui;

import java.awt.GridBagLayout;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import gui.XGPoint.PointRelation;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;import javax.swing.*;

public class XGAEG extends JPanel implements XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress ATTACK = new XGAddress("8//26"), DECAY = new XGAddress("8//27"), RELEASE = new XGAddress("8//28");

/**************************************************************************************/

	private final XGPointPanel panel;
	private final XGValue attack, decay, release;

	public XGAEG(XGValue atk, XGValue dec, XGValue rel)
	{	this.attack = atk;
		this.decay = dec;
		this.release = rel;
		if(atk == null || dec == null || rel == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			this.panel = null;
			return;
		}
		this.setName("Amplifier Envelope Generator");
		this.borderize();

		int maxX = this.attack.getParameter().getMaxIndex() - this.attack.getParameter().getMinIndex();
		maxX += this.decay.getParameter().getMaxIndex() - this.decay.getParameter().getMinIndex();
		maxX += this.release.getParameter().getMaxIndex() - this.release.getParameter().getMinIndex();

		this.panel = new XGPointPanel(0, 0, 0, 0, 0, maxX, 0, 127);
		this.panel.setUnits("Time", "Gain");

		this.panel.add(new XGPoint(0, XGFixedValue.VALUE_0, XGFixedValue.VALUE_0, PointRelation.ABSOLUTE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(1, this.attack, XGFixedValue.VALUE_127, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(2, this.decay, XGFixedValue.VALUE_64, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));
		this.panel.add(new XGPoint(3, this.release, XGFixedValue.VALUE_0, PointRelation.ADD_TO_PREVIOUS_COORDINATE, PointRelation.ABSOLUTE));

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);
	}
}
