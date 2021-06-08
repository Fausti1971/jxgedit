package gui;

import module.XGModule;import javax.swing.*;

public class XGDrumEditWindow extends XGEditWindow
{
	public XGDrumEditWindow(XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	@Override public String getTitle()
	{	return XGMainWindow.window.getTitle() + " - " + this.module.getType() + " - " + this.module.toString();
	}

	@Override JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.X_AXIS));

//		XGFrame midi = new gui.XGFrame("Midi", GRID * 3, GRID * 2);
		XGFrame voice = new gui.XGFrame(null, GRID * 3, GRID * 2);

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		//midi.add(new XGFlagBox("Receive", values.get("ad_rcv_prg"), values.get("ad_rcv_cc"), values.get("ad_rcv_vol"), values.get("ad_rcv_pan"), values.get("ad_rcv_exp"), values.get("ad_rcv_bank")), "a0e0");
		//midi.add(new XGKnob(values.get("ad_midi")), "a1a2");
		//midi.add(new XGKnob(values.get("ad_ac1_nr")), "b1b2");
		//midi.add(new XGKnob(values.get("ad_ac2_nr")), "c1c2");
		//midi.add(new XGKnob(values.get("ad_cbc1_nr")), "d1d2");
		//midi.add(new XGKnob(values.get("ad_cbc2_nr")), "e1e2");

		voice.add(new XGCombo(values.get("ds_assign")), "a0b0");
		voice.add(new XGCombo(values.get("ds_audio_output")), "c0d0");
		voice.add(new XGCombo(values.get("ds_group")), "e0f0");
		voice.add(new XGCheckbox(values.get("ds_on")), "g0i0");
		voice.add(new XGCheckbox(values.get("ds_off")), "j0l0");

		voice.add(new gui.XGKnob(values.get("ds_vol")), "a1a2");
		voice.add(new gui.XGKnob(values.get("ds_pan")), "b1b2");
		voice.add(new gui.XGKnob(values.get("ds_rev")), "d1d2");
		voice.add(new gui.XGKnob(values.get("ds_cho")), "e1e2");
		voice.add(new gui.XGKnob(values.get("ds_var")), "f1f2");

		voice.add(new gui.XGKnob(values.get("ds_pitch")), "a3a4");
		voice.add(new gui.XGKnob(values.get("ds_velo_pitch")), "b3b4");
		voice.add(new gui.XGKnob(values.get("ds_lpf_cut")), "c3c4");
		voice.add(new gui.XGKnob(values.get("ds_lpf_reso")), "d3d4");
		voice.add(new gui.XGKnob(values.get("ds_velo_lpf")), "e3e4");
		voice.add(new gui.XGKnob(values.get("ds_hpf_cut")), "f3f4");

		voice.add(new gui.XGKnob(values.get("ds_eq_bass")), "a5a6");
		voice.add(new gui.XGKnob(values.get("ds_eq_bass_freq")), "b5b6");
		voice.add(new gui.XGKnob(values.get("ds_eq_treble")), "d5d6");
		voice.add(new gui.XGKnob(values.get("ds_eq_treble_freq")), "e5e6");

		voice.add(new XGAEG(values.get("ds_aeg_attack"), values.get("ds_aeg_decay"), values.get("ds_aeg_release")), "g1l4");
		voice.add(new gui.XGKnob(values.get("ds_aeg_attack")), "g5g6");
		voice.add(new gui.XGKnob(values.get("ds_aeg_decay")), "h5h6");
		voice.add(new gui.XGKnob(values.get("ds_aeg_release")), "i5i6");

		root.add(voice);
		//root.add(midi);

		return root;
	}

}
