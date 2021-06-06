package gui;

import xml.XGProperty;import static xml.XMLNodeConstants.ATTR_NAME;import javax.swing.*;

public class XGADPartEditWindow extends XGEditWindow
{
	public XGADPartEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.X_AXIS));

		XGFrame midi = new gui.XGFrame("Midi", GRID * 3, GRID * 2);
		XGFrame voice = new gui.XGFrame("Voice", GRID * 3, GRID * 2);

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		midi.add(new XGFlagBox("Receive", values.get("ad_rcv_prg"), values.get("ad_rcv_cc"), values.get("ad_rcv_vol"), values.get("ad_rcv_pan"), values.get("ad_rcv_exp"), values.get("ad_rcv_bank")), "a0e0");
		midi.add(new XGKnob(values.get("ad_midi")), "a1a2");
		midi.add(new XGKnob(values.get("ad_ac1_nr")), "b1b2");
		midi.add(new XGKnob(values.get("ad_ac2_nr")), "c1c2");
		midi.add(new XGKnob(values.get("ad_cbc1_nr")), "d1d2");
		midi.add(new XGKnob(values.get("ad_cbc2_nr")), "e1e2");

		voice.add(new gui.XGCombo(values.get("ad_stereo")), "a0b0");
		voice.add(new gui.XGCombo(values.get("ad_input_cat")), "c0d0");
		voice.add(new gui.XGProgramSelector(values.get("ad_preset")), "e0i0");

		voice.add(new XGKnob(values.get("ad_volume")), "a1a2");
		voice.add(new XGKnob(values.get("ad_pan")), "b1b2");
		voice.add(new XGRadio(values.get("ad_gain")), "c1d2");
		voice.add(new XGKnob(values.get("ad_dry")), "f1f2");
		voice.add(new XGKnob(values.get("ad_rev")), "g1g2");
		voice.add(new XGKnob(values.get("ad_cho")), "h1h2");
		voice.add(new XGKnob(values.get("ad_var")), "i1i2");

		root.add(voice);
		root.add(midi);

		return root;
	}

	public void propertyChanged(XGProperty p)
	{	if(ATTR_NAME.equals(p.getTag())) this.setTitle(this.getTitle());
	}
}
