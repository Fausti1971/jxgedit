package gui;

import javax.swing.*;public class XGChorusEditWindow extends XGEditWindow
{
	public XGChorusEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		XGFrame main = new XGFrame(null, GRID * 3, GRID * 2),
			parms = new XGFrame(null, GRID * 3, GRID * 4);
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("cho_program")), "a0f0");
		main.add(new gui.XGKnob(values.get("cho_return")), "g0g1");
		main.add(new gui.XGKnob(values.get("cho_pan")), "h0h1");
		main.add(new gui.XGKnob(values.get("cho_to_rev")), "i0i1");
		root.add(main);

		parms.add(new gui.XGKnob(values.get("cho_p1")), "a0a0");
		parms.add(new gui.XGKnob(values.get("cho_p2")), "b0b0");
		parms.add(new gui.XGKnob(values.get("cho_p3")), "c0c0");
		parms.add(new gui.XGKnob(values.get("cho_p4")), "d0d0");
		parms.add(new gui.XGKnob(values.get("cho_p5")), "e0e0");
		parms.add(new gui.XGKnob(values.get("cho_p6")), "f0f0");
		parms.add(new gui.XGKnob(values.get("cho_p7")), "g0g0");
		parms.add(new gui.XGKnob(values.get("cho_p8")), "h0h0");
		parms.add(new gui.XGKnob(values.get("cho_p9")), "i0i0");
		parms.add(new gui.XGKnob(values.get("cho_p10")), "j0j0");
		parms.add(new gui.XGKnob(values.get("cho_p11")), "k0k0");
		parms.add(new gui.XGKnob(values.get("cho_p12")), "l0l0");
		parms.add(new gui.XGKnob(values.get("cho_p13")), "m0m0");
		parms.add(new gui.XGKnob(values.get("cho_p14")), "n0n0");
		parms.add(new gui.XGKnob(values.get("cho_p15")), "o0o0");
		parms.add(new gui.XGKnob(values.get("cho_p16")), "p0p0");
		root.add(parms);

		return root;
	}
}