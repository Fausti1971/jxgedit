package gui;

public class XGChorusEditWindow extends XGEditWindow
{
	public XGChorusEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	private javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel(), main = new javax.swing.JPanel(), parms = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));
		main.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, GAP, GAP));
		parms.setLayout(new java.awt.GridLayout());
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("cho_program")));
		main.add(new gui.XGKnob(values.get("cho_return")));
		main.add(new gui.XGKnob(values.get("cho_pan")));
		main.add(new gui.XGKnob(values.get("cho_to_rev")));
		root.add(main);

		parms.add(new gui.XGKnob(values.get("cho_p1")));
		parms.add(new gui.XGKnob(values.get("cho_p2")));
		parms.add(new gui.XGKnob(values.get("cho_p3")));
		parms.add(new gui.XGKnob(values.get("cho_p4")));
		parms.add(new gui.XGKnob(values.get("cho_p5")));
		parms.add(new gui.XGKnob(values.get("cho_p6")));
		parms.add(new gui.XGKnob(values.get("cho_p7")));
		parms.add(new gui.XGKnob(values.get("cho_p8")));
		parms.add(new gui.XGKnob(values.get("cho_p9")));
		parms.add(new gui.XGKnob(values.get("cho_p10")));
		parms.add(new gui.XGKnob(values.get("cho_p11")));
		parms.add(new gui.XGKnob(values.get("cho_p12")));
		parms.add(new gui.XGKnob(values.get("cho_p13")));
		parms.add(new gui.XGKnob(values.get("cho_p14")));
		parms.add(new gui.XGKnob(values.get("cho_p15")));
		parms.add(new gui.XGKnob(values.get("cho_p16")));
		root.add(parms);

		return root;
	}
}