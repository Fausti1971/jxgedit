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

	private JComponent createContent()
	{	JPanel root = new javax.swing.JPanel(), main = new javax.swing.JPanel(), parms = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));
		main.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, GAP, GAP));
		parms.setLayout(new java.awt.GridLayout());
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("rev_program")));
		main.add(new gui.XGKnob(values.get("rev_return")));
		main.add(new gui.XGKnob(values.get("rev_pan")));
		root.add(main);

		parms.add(new gui.XGKnob(values.get("rev_p1")));
		parms.add(new gui.XGKnob(values.get("rev_p2")));
		parms.add(new gui.XGKnob(values.get("rev_p3")));
		parms.add(new gui.XGKnob(values.get("rev_p4")));
		parms.add(new gui.XGKnob(values.get("rev_p5")));
		parms.add(new gui.XGKnob(values.get("rev_p6")));
		parms.add(new gui.XGKnob(values.get("rev_p7")));
		parms.add(new gui.XGKnob(values.get("rev_p8")));
		parms.add(new gui.XGKnob(values.get("rev_p9")));
		parms.add(new gui.XGKnob(values.get("rev_p10")));
		parms.add(new gui.XGKnob(values.get("rev_p11")));
		parms.add(new gui.XGKnob(values.get("rev_p12")));
		parms.add(new gui.XGKnob(values.get("rev_p13")));
		parms.add(new gui.XGKnob(values.get("rev_p14")));
		parms.add(new gui.XGKnob(values.get("rev_p15")));
		parms.add(new gui.XGKnob(values.get("rev_p16")));
		root.add(parms);

		return root;
	}
}