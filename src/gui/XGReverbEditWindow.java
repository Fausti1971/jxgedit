package gui;
import static java.awt.BorderLayout.CENTER;import static java.awt.BorderLayout.EAST;import javax.swing.*;

public class XGReverbEditWindow extends XGEditWindow
{
	public XGReverbEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	JComponent createContent()
	{	JPanel root = new javax.swing.JPanel();
		XGFrame main = new XGFrame(null, GRID * 3, GRID * 2),
			parms = new XGFrame(null, GRID * 3, GRID * 4);
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("rev_program")), "a0f0");
		main.add(new gui.XGKnob(values.get("rev_return")), "g0g1");
		main.add(new gui.XGKnob(values.get("rev_pan")), "h0h1");
		root.add(main);

		parms.add(new gui.XGKnob(values.get("rev_p1")), "a0a0");
		parms.add(new gui.XGKnob(values.get("rev_p2")), "b0b0");
		parms.add(new gui.XGKnob(values.get("rev_p3")), "c0c0");
		parms.add(new gui.XGKnob(values.get("rev_p4")), "d0d0");
		parms.add(new gui.XGKnob(values.get("rev_p5")), "e0e0");
		parms.add(new gui.XGKnob(values.get("rev_p6")), "f0f0");
		parms.add(new gui.XGKnob(values.get("rev_p7")), "g0g0");
		parms.add(new gui.XGKnob(values.get("rev_p8")), "h0h0");
		parms.add(new gui.XGKnob(values.get("rev_p9")), "i0i0");
		parms.add(new gui.XGKnob(values.get("rev_p10")), "j0j0");
		parms.add(new gui.XGKnob(values.get("rev_p11")), "k0k0");
		parms.add(new gui.XGKnob(values.get("rev_p12")), "l0l0");
		parms.add(new gui.XGKnob(values.get("rev_p13")), "m0m0");
		parms.add(new gui.XGKnob(values.get("rev_p14")), "n0n0");
		parms.add(new gui.XGKnob(values.get("rev_p15")), "o0o0");
		parms.add(new gui.XGKnob(values.get("rev_p16")), "p0p0");
		root.add(parms);

		return root;
	}
}