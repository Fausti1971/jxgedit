package gui;

import static gui.XGMEQ.*;public class XGEQEditWindow extends XGEditWindow
{


	public XGEQEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	XGFrame root = new XGFrame("MasterEQ", GRID * 3, GRID * 2);
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		root.add(new gui.XGRadio(values.get("eq_program"), javax.swing.BoxLayout.X_AXIS), "a0j0");

		root.add(new XGMEQ(this.module), "a1j6");

		root.add(new gui.XGKnob(values.get(G1)), "a7a8");
		root.add(new gui.XGKnob(values.get(G2)), "c7c8");
		root.add(new gui.XGKnob(values.get(G3)), "e7e8");
		root.add(new gui.XGKnob(values.get(G4)), "g7g8");
		root.add(new gui.XGKnob(values.get(G5)), "i7i8");

		root.add(new gui.XGKnob(values.get(F1)), "a9a10");
		root.add(new gui.XGKnob(values.get(F2)), "c9c10");
		root.add(new gui.XGKnob(values.get(F3)), "e9e10");
		root.add(new gui.XGKnob(values.get(F4)), "g9g10");
		root.add(new gui.XGKnob(values.get(F5)), "i9i10");

		root.add(new gui.XGKnob(values.get(Q1)), "b9b10");
		root.add(new gui.XGKnob(values.get(Q2)), "d9d10");
		root.add(new gui.XGKnob(values.get(Q3)), "f9f10");
		root.add(new gui.XGKnob(values.get(Q4)), "h9h10");
		root.add(new gui.XGKnob(values.get(Q5)), "j9j10");

		root.add(new gui.XGRadio(values.get(S1)), "b7b8");
		root.add(new gui.XGRadio(values.get(S5)), "j7j8");

		return root;
	}
}
