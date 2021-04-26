package gui;
public class XGVariationEditWindow extends XGEditWindow
{
	public XGVariationEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	private javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel(), main = new javax.swing.JPanel(), parms = new javax.swing.JPanel(), ctrl = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));
		main.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, GAP, GAP));
		ctrl.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, GAP, GAP));
		parms.setLayout(new java.awt.GridLayout());
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("var_program")));
		main.add(new gui.XGKnob(values.get("var_return")));
		main.add(new gui.XGKnob(values.get("var_pan")));
		main.add(new gui.XGKnob(values.get("var_to_rev")));
		main.add(new gui.XGKnob(values.get("var_to_cho")));
		main.add(new gui.XGRadio(values.get("var_connection")));
		main.add(new gui.XGKnob(values.get("var_part")));
		root.add(main);

		parms.add(new gui.XGKnob(values.get("var_p1")));
		parms.add(new gui.XGKnob(values.get("var_p2")));
		parms.add(new gui.XGKnob(values.get("var_p3")));
		parms.add(new gui.XGKnob(values.get("var_p4")));
		parms.add(new gui.XGKnob(values.get("var_p5")));
		parms.add(new gui.XGKnob(values.get("var_p6")));
		parms.add(new gui.XGKnob(values.get("var_p7")));
		parms.add(new gui.XGKnob(values.get("var_p8")));
		parms.add(new gui.XGKnob(values.get("var_p9")));
		parms.add(new gui.XGKnob(values.get("var_p10")));
		parms.add(new gui.XGKnob(values.get("var_p11")));
		parms.add(new gui.XGKnob(values.get("var_p12")));
		parms.add(new gui.XGKnob(values.get("var_p13")));
		parms.add(new gui.XGKnob(values.get("var_p14")));
		parms.add(new gui.XGKnob(values.get("var_p15")));
		parms.add(new gui.XGKnob(values.get("var_p16")));
		root.add(parms);

		ctrl.add(new gui.XGKnob(values.get("var_mw")));
		ctrl.add(new gui.XGKnob(values.get("var_pb")));
		ctrl.add(new gui.XGKnob(values.get("var_cat")));
		ctrl.add(new gui.XGKnob(values.get("var_ac1")));
		ctrl.add(new gui.XGKnob(values.get("var_ac2")));
		root.add(ctrl);

		return root;
	}
}