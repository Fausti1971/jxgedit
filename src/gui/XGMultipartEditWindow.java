package gui;

public class XGMultipartEditWindow extends XGEditWindow
{	
	public XGMultipartEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	private javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		XGFrame midi = new gui.XGFrame("Midi");
		XGFrame voice = new gui.XGFrame("Voice");
		XGFrame control = new gui.XGFrame("Control");

		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));
		voice.setLayout(new java.awt.GridBagLayout());
		midi.setLayout(new java.awt.GridBagLayout());
		control.setLayout(new java.awt.GridBagLayout());

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, java.awt.GridBagConstraints.WEST, java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(2,2,2,2), 2, 2);
		voice.add(new gui.XGCombo(values.get("mp_key_on_assign")), gbc);

		gbc.gridx = 2;
		voice.add(new gui.XGCombo(values.get("mp_partmode")), gbc);

		gbc.gridx = 3;
		voice.add(new gui.XGProgramSelector(values.get("mp_program")), gbc);

		gbc.gridx = 4;
		voice.add(new gui.XGCombo(values.get("mp_poly")), gbc);


		root.add(midi);
		root.add(voice);
		root.add(control);

		return root;
	}
}
