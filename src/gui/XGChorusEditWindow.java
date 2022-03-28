package gui;

import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;public class XGChorusEditWindow extends XGEditWindow
{
	public XGChorusEditWindow(module.XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame();
		XGFrame main = new XGFrame(15, 2);
		XGFrame parms = new XGFrame(15, 1);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		main.add(new XGProgramSelector(values.get("cho_program")), "0,0,4,1");
		main.add(new XGKnob(values.get("cho_return")), "4,0,1,2");
		main.add(new XGKnob(values.get("cho_pan")), "5,0,1,2");
		main.add(new XGKnob(values.get("cho_to_rev")), "6,0,1,2");
		root.add(main, "0,0,1,1");

		parms.add(new XGKnob(values.get("cho_p1")), "0,0,1,1");
		parms.add(new XGKnob(values.get("cho_p2")), "1,0,1,1");
		parms.add(new XGKnob(values.get("cho_p3")), "2,0,1,1");
		parms.add(new XGKnob(values.get("cho_p4")), "3,0,1,1");
		parms.add(new XGKnob(values.get("cho_p5")), "4,0,1,1");
		parms.add(new XGKnob(values.get("cho_p6")), "5,0,1,1");
		parms.add(new XGKnob(values.get("cho_p7")), "6,0,1,1");
		parms.add(new XGKnob(values.get("cho_p8")), "7,0,1,1");
		parms.add(new XGKnob(values.get("cho_p9")), "8,0,1,1");
		parms.add(new XGKnob(values.get("cho_p10")), "9,0,1,1");
		parms.add(new XGKnob(values.get("cho_p11")), "10,0,1,1");
		parms.add(new XGKnob(values.get("cho_p12")), "11,0,1,1");
		parms.add(new XGKnob(values.get("cho_p13")), "12,0,1,1");
		parms.add(new XGKnob(values.get("cho_p14")), "13,0,1,1");
		parms.add(new XGKnob(values.get("cho_p15")), "14,0,1,1");
		parms.add(new XGKnob(values.get("cho_p16")), "15,0,1,1");
		root.add(parms, "0,1,1,1");

		return root;
	}
}