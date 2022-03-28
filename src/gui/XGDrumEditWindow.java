package gui;

import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;

public class XGDrumEditWindow extends XGEditWindow
{
	public XGDrumEditWindow(XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	@Override public String getTitle()
	{	return XGMainWindow.MAINWINDOW.getTitle() + " - " + this.module.getType() + " - " + this.module.toString();
	}

	@Override JComponent createContent()
	{	JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));

		XGFrame voice = new XGFrame(3, 2);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		voice.add(new XGCombo(values.get("ds_assign")), "a0b0");
		voice.add(new XGCombo(values.get("ds_audio_output")), "c0d0");
		voice.add(new XGCombo(values.get("ds_group")), "e0f0");
		voice.add(new XGCheckbox(values.get("ds_on")), "g0i0");
		voice.add(new XGCheckbox(values.get("ds_off")), "j0l0");

		voice.add(new XGKnob(values.get("ds_vol")), "a1a2");
		voice.add(new XGKnob(values.get("ds_pan")), "b1b2");
		voice.add(new XGKnob(values.get("ds_rev")), "d1d2");
		voice.add(new XGKnob(values.get("ds_cho")), "e1e2");
		voice.add(new XGKnob(values.get("ds_var")), "f1f2");

		voice.add(new XGKnob(values.get("ds_pitch")), "a3a4");
		voice.add(new XGKnob(values.get("ds_velo_pitch")), "b3b4");
		voice.add(new XGKnob(values.get("ds_lpf_cut")), "c3c4");
		voice.add(new XGKnob(values.get("ds_lpf_reso")), "d3d4");
		voice.add(new XGKnob(values.get("ds_velo_lpf")), "e3e4");
		voice.add(new XGKnob(values.get("ds_hpf_cut")), "f3f4");

		voice.add(new XGKnob(values.get("ds_eq_bass")), "a5a6");
		voice.add(new XGKnob(values.get("ds_eq_bass_freq")), "b5b6");
		voice.add(new XGKnob(values.get("ds_eq_treble")), "d5d6");
		voice.add(new XGKnob(values.get("ds_eq_treble_freq")), "e5e6");

		voice.add(new XGAEG(values.get("ds_aeg_attack"), values.get("ds_aeg_decay"), values.get("ds_aeg_release")), "g1l4");
		voice.add(new XGKnob(values.get("ds_aeg_attack")), "g5g6");
		voice.add(new XGKnob(values.get("ds_aeg_decay")), "h5h6");
		voice.add(new XGKnob(values.get("ds_aeg_release")), "i5i6");

		root.add(voice);

		return root;
	}

}
