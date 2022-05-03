package gui;

import module.XGModule;import tag.XGTagableAddressableSet;import value.XGValue;import javax.swing.*;

public class XGDrumEditWindow extends XGEditWindow
{
	public XGDrumEditWindow(XGModule mod)
	{	super( mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	@Override public String getTitle()
	{	return XGMainWindow.MAINWINDOW.getTitle() + " - " + this.module.getType() + " - " + this.module.toString();
	}

	@Override JComponent createContent()
	{	XGFrame root = new XGFrame(false);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		XGFrame voice = new XGFrame(3, 2, false);
		voice.add(new XGKnob(values.get("ds_vol")), "0,0,1,2");
		voice.add(new XGKnob(values.get("ds_pan")), "1,0,1,2");
		voice.add(new XGCombo(values.get("ds_audio_output")), "2,0,2,1");
		voice.add(new XGCheckbox(values.get("ds_on")), "4,0,2,1");
		voice.add(new XGCheckbox(values.get("ds_off")), "4,1,2,1");
		voice.add(new XGRadio(values.get("ds_assign")), "6,0,2,2");
		voice.add(new XGKnob(values.get("ds_group")), "8,0,1,2");
		root.add(voice, "0,0,9,2");

		XGFrame pitch = new XGFrame("Pitch");
		pitch.add(new XGKnob(values.get("ds_pitch")), "0,0,1,4");
		pitch.add(new XGKnob(values.get("ds_velo_pitch")), "1,0,1,4");
		root.add(pitch, "9,0,2,2");

		XGFrame fx = new XGFrame("Effect");
		fx.add(new XGKnob(values.get("ds_rev")), "0,0,1,4");
		fx.add(new XGKnob(values.get("ds_cho")), "1,0,1,4");
		fx.add(new XGKnob(values.get("ds_var")), "2,0,1,4");
		root.add(fx, "0,2,3,2");

		XGFrame filter = new XGFrame("Filter");
		filter.add(new XGKnob(values.get("ds_lpf_cut")), "0,0,1,4");
		filter.add(new XGKnob(values.get("ds_lpf_reso")), "1,0,1,4");
		filter.add(new XGKnob(values.get("ds_velo_lpf")), "2,0,1,4");
		filter.add(new XGKnob(values.get("ds_hpf_cut")), "3,0,1,4");
		root.add(filter, "3,2,4,2");

		XGFrame eq = new XGFrame("EQ");
		eq.add(new XGKnob(values.get("ds_eq_bass")), "0,0,1,4");
		eq.add(new XGKnob(values.get("ds_eq_bass_freq")), "1,0,1,4");
		eq.add(new XGKnob(values.get("ds_eq_treble")), "2,0,1,4");
		eq.add(new XGKnob(values.get("ds_eq_treble_freq")), "3,0,1,4");
		root.add(eq, "7,2,4,2");

		XGFrame aeg = new XGFrame("Amplifier Envelope Generator");
		aeg.add(new XGAmplifierEnvelope(values.get("ds_aeg_attack"), values.get("ds_aeg_decay"), values.get("ds_aeg_release")), "0,0,3,4");
		aeg.add(new XGKnob(values.get("ds_aeg_attack")), "0,4,1,4");
		aeg.add(new XGKnob(values.get("ds_aeg_decay")), "1,4,1,4");
		aeg.add(new XGKnob(values.get("ds_aeg_release")), "2,4,1,4");
		root.add(aeg, "11,0,3,4");

		return root;
	}

}
