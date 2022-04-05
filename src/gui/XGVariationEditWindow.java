package gui;
import module.XGModule;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.swing.*;

public class XGVariationEditWindow extends XGEditWindow
{
	public XGVariationEditWindow(XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	XGFrame
			root = new XGFrame(false),
			main = new XGFrame(8,2, false),
			parms = new XGFrame(16,1, false),
			ctrl = new XGFrame(7,1, false);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		main.add(new XGProgramSelector(values.get("var_program")), "0,0,4,1");
		main.add(new XGKnob(values.get("var_return")), "4,0,1,2");
		main.add(new XGKnob(values.get("var_pan")), "5,0,1,2");
		main.add(new XGKnob(values.get("var_to_rev")), "6,0,1,2");
		main.add(new XGKnob(values.get("var_to_cho")), "7,0,1,2");
		main.add(new XGRadio(values.get("var_connection"), BoxLayout.X_AXIS), "0,1,2,1");
		main.add(new XGCombo(values.get("var_part")), "2,1,2,1");
		root.add(main, "0,0,8,1");

		parms.add(new XGKnob(values.get("var_p1")), "0,0,1,1");
		parms.add(new XGKnob(values.get("var_p2")), "1,0,1,1");
		parms.add(new XGKnob(values.get("var_p3")), "2,0,1,1");
		parms.add(new XGKnob(values.get("var_p4")), "3,0,1,1");
		parms.add(new XGKnob(values.get("var_p5")), "4,0,1,1");
		parms.add(new XGKnob(values.get("var_p6")), "5,0,1,1");
		parms.add(new XGKnob(values.get("var_p7")), "6,0,1,1");
		parms.add(new XGKnob(values.get("var_p8")), "7,0,1,1");
		parms.add(new XGKnob(values.get("var_p9")), "8,0,1,1");
		parms.add(new XGKnob(values.get("var_p10")), "9,0,1,1");
		parms.add(new XGKnob(values.get("var_p11")), "10,0,1,1");
		parms.add(new XGKnob(values.get("var_p12")), "11,0,1,1");
		parms.add(new XGKnob(values.get("var_p13")), "12,0,1,1");
		parms.add(new XGKnob(values.get("var_p14")), "13,0,1,1");
		parms.add(new XGKnob(values.get("var_p15")), "14,0,1,1");
		parms.add(new XGKnob(values.get("var_p16")), "15,0,1,1");
		root.add(parms, "0,1,16,1");

		ctrl.add(new XGKnob(values.get("var_mw")), "0,0,1,1");
		ctrl.add(new XGKnob(values.get("var_pb")), "1,0,1,1");
		ctrl.add(new XGKnob(values.get("var_cat")), "2,0,1,1");
		ctrl.add(new XGKnob(values.get("var_ac1")), "3,0,1,1");
		ctrl.add(new XGKnob(values.get("var_ac2")), "4,0,1,1");
		root.add(ctrl, "9,0,7,1");

		return root;
	}
}