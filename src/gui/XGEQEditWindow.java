package gui;

import static gui.XGMEQ.*;import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;public class XGEQEditWindow extends XGEditWindow
{
	public XGEQEditWindow(module.XGModule mod)
	{	super( mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	XGFrame root = new XGFrame(false);
		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		root.add(new XGRadio(values.get("eq_program"), BoxLayout.X_AXIS), "0,0,5,1");

		root.add(new XGMEQ(this.module), "0,1,5,3");

		root.add(new XGKnob(values.get(G1)), "0,4,1,2");
		root.add(new XGKnob(values.get(G2)), "1,4,1,2");
		root.add(new XGKnob(values.get(G3)), "2,4,1,2");
		root.add(new XGKnob(values.get(G4)), "3,4,1,2");
		root.add(new XGKnob(values.get(G5)), "4,4,1,2");

		root.add(new XGKnob(values.get(F1)), "0,6,1,2");
		root.add(new XGKnob(values.get(F2)), "1,6,1,2");
		root.add(new XGKnob(values.get(F3)), "2,6,1,2");
		root.add(new XGKnob(values.get(F4)), "3,6,1,2");
		root.add(new XGKnob(values.get(F5)), "4,6,1,2");

		root.add(new XGKnob(values.get(Q1)), "0,8,1,2");
		root.add(new XGKnob(values.get(Q2)), "1,8,1,2");
		root.add(new XGKnob(values.get(Q3)), "2,8,1,2");
		root.add(new XGKnob(values.get(Q4)), "3,8,1,2");
		root.add(new XGKnob(values.get(Q5)), "4,8,1,2");

		root.add(new XGRadio(values.get(S1)), "0,10,1,1");
		root.add(new XGRadio(values.get(S5)), "4,10,1,1");

		return root;
	}
}
