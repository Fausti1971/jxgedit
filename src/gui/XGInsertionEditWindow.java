package gui;
import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;

public class XGInsertionEditWindow extends XGEditWindow
{
	public XGInsertionEditWindow(XGModule mod)
	{	super( mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(false);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		XGFrame main = new XGFrame(5, 1, false);
		main.add(new XGProgramSelector(values.get("ins_program")), "0,0,4,1");
		main.add(new XGCombo(values.get("ins_part")), "2,1,2,1");
		root.add(main, "0,0,5,1");

		XGFrame ctrl = new XGFrame(7, 1, false);
		ctrl.add(new XGKnob(values.get("ins_mw")), "0,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_pb")), "1,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_cat")), "2,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_ac1")), "3,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_ac2")), "4,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_cbc1")), "5,0,1,1");
		ctrl.add(new XGKnob(values.get("ins_cbc2")), "6,0,1,1");
		root.add(ctrl, "9,0,7,1");

		XGFrame parms = new XGFrame(16, 1, false);
		parms.add(new XGKnob(values.get("ins_p1")), "0,0,1,1");
		parms.add(new XGKnob(values.get("ins_p2")), "1,0,1,1");
		parms.add(new XGKnob(values.get("ins_p3")), "2,0,1,1");
		parms.add(new XGKnob(values.get("ins_p4")), "3,0,1,1");
		parms.add(new XGKnob(values.get("ins_p5")), "4,0,1,1");
		parms.add(new XGKnob(values.get("ins_p6")), "5,0,1,1");
		parms.add(new XGKnob(values.get("ins_p7")), "6,0,1,1");
		parms.add(new XGKnob(values.get("ins_p8")), "7,0,1,1");
		parms.add(new XGKnob(values.get("ins_p9")), "8,0,1,1");
		parms.add(new XGKnob(values.get("ins_p10")), "9,0,1,1");
		parms.add(new XGKnob(values.get("ins_p11")), "10,0,1,1");
		parms.add(new XGKnob(values.get("ins_p12")), "11,0,1,1");
		parms.add(new XGKnob(values.get("ins_p13")), "12,0,1,1");
		parms.add(new XGKnob(values.get("ins_p14")), "13,0,1,1");
		parms.add(new XGKnob(values.get("ins_p15")), "14,0,1,1");
		parms.add(new XGKnob(values.get("ins_p16")), "15,0,1,1");
		root.add(parms, "0,1,16,1");

		XGFrame parms2 = new XGFrame(16, 1, false);
		parms2.add(new XGKnob(values.get("ins_p1w")), "0,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p2w")), "1,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p3w")), "2,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p4w")), "3,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p5w")), "4,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p6w")), "5,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p7w")), "6,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p8w")), "7,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p9w")), "8,0,1,1");
		parms2.add(new XGKnob(values.get("ins_p10w")), "9,0,1,1");
		root.add(parms2, "0,2,16,1");

		return root;
	}
}