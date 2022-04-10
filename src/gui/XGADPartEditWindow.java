package gui;

import tag.XGTagableAddressableSet;import value.XGValue;import xml.XGProperty;import static xml.XMLNodeConstants.ATTR_NAME;import javax.swing.*;

public class XGADPartEditWindow extends XGEditWindow
{
	public XGADPartEditWindow(module.XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGTagableAddressableSet<XGValue> values = this.module.getValues();

		XGFrame root = new XGFrame(false);
		XGFrame voice = new XGFrame(false);
		XGFrame effect = new XGFrame(false);
		XGFrame midi = new XGFrame(false);

		voice.add(new XGKnob(values.get("ad_volume")), "0,0,1,2");
		voice.add(new XGKnob(values.get("ad_pan")), "1,0,1,2");
		voice.add(new XGRadio(values.get("ad_gain")), "2,0,1,1");
		voice.add(new XGRadio(values.get("ad_stereo")),"2,1,1,1");
		voice.add(new XGCombo(values.get("ad_input_cat")), "3,0,3,1");
		voice.add(new XGProgramSelector(values.get("ad_preset")), "3,1,3,1");

		effect.add(new XGKnob(values.get("ad_dry")), "0,0,1,2");
		effect.add(new XGKnob(values.get("ad_rev")), "1,0,1,2");
		effect.add(new XGKnob(values.get("ad_cho")), "2,0,1,2");
		effect.add(new XGKnob(values.get("ad_var")), "3,0,1,2");

		midi.add(new XGFlagBox("MIDI Filter", values.get("ad_rcv_prg"), values.get("ad_rcv_cc"), values.get("ad_rcv_vol"), values.get("ad_rcv_pan"), values.get("ad_rcv_exp"), values.get("ad_rcv_bank")), "0,0,2,1");
		midi.add(new XGKnob(values.get("ad_midi")), "2,0,1,2");
		midi.add(new XGKnob(values.get("ad_ac1_nr")), "3,0,1,2");
		midi.add(new XGKnob(values.get("ad_ac2_nr")),"4,0,1,2");
		midi.add(new XGKnob(values.get("ad_cbc1_nr")), "5,0,1,2");
		midi.add(new XGKnob(values.get("ad_cbc2_nr")), "6,0,1,2");

		root.add(voice, "0,0,6,1");
		root.add(effect, "6,0,4,1");
		root.add(midi, "10,0,7,1");

		return root;
	}

	public void propertyChanged(XGProperty p){	if(ATTR_NAME.equals(p.getTag())) this.setTitle(this.getTitle());}
}
