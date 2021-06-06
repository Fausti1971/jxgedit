package gui;

import static gui.XGMEQ.*;public class XGEQEditWindow extends XGEditWindow
{


	public XGEQEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setResizable(true);
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		root.setLayout(new java.awt.GridBagLayout());
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints(1, 1, 5, 1, 0.5, 0.0, java.awt.GridBagConstraints.WEST, java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(2,2,2,2), 2, 2);

		root.add(new gui.XGRadio(values.get("eq_program"), javax.swing.BoxLayout.X_AXIS), gbc);

		gbc.gridy = 2;
		gbc.gridheight = 5;
		gbc.weighty = 1.0;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		root.add(new XGMEQ(this.module), gbc);

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weighty = 0.0;

		gbc.gridy = 7;
		root.add(new gui.XGKnob(values.get(G1)), gbc);

		gbc.gridx = 2;
		root.add(new gui.XGKnob(values.get(G2)), gbc);

		gbc.gridx = 3;
		root.add(new gui.XGKnob(values.get(G3)), gbc);

		gbc.gridx = 4;
		root.add(new gui.XGKnob(values.get(G4)), gbc);

		gbc.gridx = 5;
		root.add(new gui.XGKnob(values.get(G5)), gbc);

		gbc.gridy = 8;

		gbc.gridx = 1;
		root.add(new gui.XGKnob(values.get(F1)), gbc);

		gbc.gridx = 2;
		root.add(new gui.XGKnob(values.get(F2)), gbc);

		gbc.gridx = 3;
		root.add(new gui.XGKnob(values.get(F3)), gbc);

		gbc.gridx = 4;
		root.add(new gui.XGKnob(values.get(F4)), gbc);

		gbc.gridx = 5;
		root.add(new gui.XGKnob(values.get(F5)), gbc);

		gbc.gridy = 9;

		gbc.gridx = 1;
		root.add(new gui.XGKnob(values.get(Q1)), gbc);

		gbc.gridx = 2;
		root.add(new gui.XGKnob(values.get(Q2)), gbc);

		gbc.gridx = 3;
		root.add(new gui.XGKnob(values.get(Q3)), gbc);

		gbc.gridx = 4;
		root.add(new gui.XGKnob(values.get(Q4)), gbc);

		gbc.gridx = 5;
		root.add(new gui.XGKnob(values.get(Q5)), gbc);

		gbc.gridy = 10;

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		root.add(new gui.XGRadio(values.get(S1)), gbc);

		gbc.gridx = 4;
		root.add(new gui.XGRadio(values.get(S5)), gbc);

		return root;
	}

}
