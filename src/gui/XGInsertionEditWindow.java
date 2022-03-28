package gui;
import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;

public class XGInsertionEditWindow extends XGEditWindow
{
	public XGInsertionEditWindow(XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	JComponent createContent()
	{	JPanel root = new JPanel();
		XGFrame main = new XGFrame(3, 2),
			parms = new XGFrame(3, 4),
			ctrl = new XGFrame(3, 4);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		main.add(new XGProgramSelector(values.get("ins_program")), "a0f0");
		main.add(new XGCombo(values.get("ins_part")), "a1f1");
		root.add(main);

		parms.add(new XGKnob(values.get("ins_p1")), "a0a0");
		parms.add(new XGKnob(values.get("ins_p2")), "b0b0");
		parms.add(new XGKnob(values.get("ins_p3")), "c0c0");
		parms.add(new XGKnob(values.get("ins_p4")), "d0d0");
		parms.add(new XGKnob(values.get("ins_p5")), "e0e0");
		parms.add(new XGKnob(values.get("ins_p6")), "f0f0");
		parms.add(new XGKnob(values.get("ins_p7")), "g0g0");
		parms.add(new XGKnob(values.get("ins_p8")), "h0h0");
		parms.add(new XGKnob(values.get("ins_p9")), "i0i0");
		parms.add(new XGKnob(values.get("ins_p10")), "j0j0");
		parms.add(new XGKnob(values.get("ins_p11")), "k0k0");
		parms.add(new XGKnob(values.get("ins_p12")), "l0l0");
		parms.add(new XGKnob(values.get("ins_p13")), "m0m0");
		parms.add(new XGKnob(values.get("ins_p14")), "n0n0");
		parms.add(new XGKnob(values.get("ins_p15")), "o0o0");
		parms.add(new XGKnob(values.get("ins_p16")), "p0p0");
		root.add(parms);

		ctrl.add(new XGKnob(values.get("ins_mw")), "a0a0");
		ctrl.add(new XGKnob(values.get("ins_pb")), "b0b0");
		ctrl.add(new XGKnob(values.get("ins_cat")), "c0c0");
		ctrl.add(new XGKnob(values.get("ins_ac1")), "d0d0");
		ctrl.add(new XGKnob(values.get("ins_ac2")), "e0e0");
		ctrl.add(new XGKnob(values.get("ins_cbc1")), "f0f0");
		ctrl.add(new XGKnob(values.get("ins_cbc2")), "g0g0");

		root.add(ctrl);

		return root;
	}
}