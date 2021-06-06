package gui;
import javax.swing.*;

public class XGVariationEditWindow extends XGEditWindow
{
	public XGVariationEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	JPanel root = new JPanel();
		XGFrame main = new XGFrame(null, GRID * 3, GRID * 2),
			parms = new XGFrame(null, GRID * 3, GRID * 4),
			ctrl = new XGFrame(null, GRID * 3, GRID * 4);
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("var_program")), "a0f0");
		main.add(new gui.XGKnob(values.get("var_return")), "g0g1");
		main.add(new gui.XGKnob(values.get("var_pan")), "h0h1");
		main.add(new gui.XGKnob(values.get("var_to_rev")), "i0i1");
		main.add(new gui.XGKnob(values.get("var_to_cho")), "j0j1");
		main.add(new gui.XGRadio(values.get("var_connection"), BoxLayout.X_AXIS), "a1c1");
		main.add(new gui.XGCombo(values.get("var_part")), "d1f1");
		root.add(main);

		parms.add(new gui.XGKnob(values.get("var_p1")), "a0a0");
		parms.add(new gui.XGKnob(values.get("var_p2")), "b0b0");
		parms.add(new gui.XGKnob(values.get("var_p3")), "c0c0");
		parms.add(new gui.XGKnob(values.get("var_p4")), "d0d0");
		parms.add(new gui.XGKnob(values.get("var_p5")), "e0e0");
		parms.add(new gui.XGKnob(values.get("var_p6")), "f0f0");
		parms.add(new gui.XGKnob(values.get("var_p7")), "g0g0");
		parms.add(new gui.XGKnob(values.get("var_p8")), "h0h0");
		parms.add(new gui.XGKnob(values.get("var_p9")), "i0i0");
		parms.add(new gui.XGKnob(values.get("var_p10")), "j0j0");
		parms.add(new gui.XGKnob(values.get("var_p11")), "k0k0");
		parms.add(new gui.XGKnob(values.get("var_p12")), "l0l0");
		parms.add(new gui.XGKnob(values.get("var_p13")), "m0m0");
		parms.add(new gui.XGKnob(values.get("var_p14")), "n0n0");
		parms.add(new gui.XGKnob(values.get("var_p15")), "o0o0");
		parms.add(new gui.XGKnob(values.get("var_p16")), "p0p0");
		root.add(parms);

		ctrl.add(new gui.XGKnob(values.get("var_mw")), "a0a0");
		ctrl.add(new gui.XGKnob(values.get("var_pb")), "b0b0");
		ctrl.add(new gui.XGKnob(values.get("var_cat")), "c0c0");
		ctrl.add(new gui.XGKnob(values.get("var_ac1")), "d0d0");
		ctrl.add(new gui.XGKnob(values.get("var_ac2")), "e0e0");
		root.add(ctrl);

		return root;
	}
}