package gui;

import javax.swing.*;
import java.awt.*;

public class XGMultipartEditWindow extends XGEditWindow
{	
	public XGMultipartEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	private javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		XGFrame midi = new gui.XGFrame("Midi", GRID * 3, GRID * 2);
		XGFrame voice = new gui.XGFrame("Voice", GRID * 3, GRID * 2);
		XGFrame control = new gui.XGFrame("Control", GRID * 3, GRID * 2);

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		midi.add(new XGKnob(values.get("mp_midi_channel")), "A0A1");
		midi.add(new XGFlagBox("Receive", values.get("mp_rcv_pb"), values.get("mp_rcv_cat"), values.get("mp_rcv_prg"), values.get("mp_rcv_cc"), values.get("mp_rcv_pat"), values.get("mp_rcv_note"),
			values.get("mp_rcv_rpn"), values.get("mp_rcv_nrpn"), values.get("mp_rcv_mw"), values.get("mp_rcv_vol"), values.get("mp_rcv_pan"), values.get("mp_rcv_exp"), values.get("mp_rcv_hold"), values.get("mp_rcv_portamento"),
			values.get("mp_rcv_sostenuto"), values.get("mp_rcv_softpedal"), values.get("mp_rcv_bank")), "A2E2");
		midi.add(new XGRange(values.get("mp_note_lo"), values.get("mp_note_hi")), "A3E3");
		midi.add(new XGKnob(values.get("mp_velo_offset")), "F0F1");
		midi.add(new XGKnob(values.get("mp_velo_lo")), "F2F3");
		midi.add(new XGVEG(values.get("mp_velo_depth"), values.get("mp_velo_offset"), values.get("mp_velo_lo"), values.get("mp_velo_hi")), "G0L3");
		midi.add(new XGKnob(values.get("mp_velo_depth")), "M0M1");
		midi.add(new XGKnob(values.get("mp_velo_hi")), "M2M3");

		voice.add(new gui.XGCombo(values.get("mp_key_on_assign")), "A0B0");
		voice.add(new gui.XGCombo(values.get("mp_partmode")), "A1B1");
		voice.add(new gui.XGProgramSelector(values.get("mp_program")), "C1G1");
		voice.add(new gui.XGCombo(values.get("mp_poly")), "C0D0");
		voice.add(new gui.XGCombo(values.get("mp_audio_output")), "E0G0");
		voice.add(new XGCheckbox(values.get("mp_portamento")), "I0J0");
		voice.add(new XGKnob(values.get("mp_portamento_time")), "H0H1");
		voice.add(new XGKnob(values.get("mp_reserve")), "N0N1");

		voice.add(new JPanel(), "I0I1");

		voice.add(new XGKnob(values.get("mp_volume")), "A2A3");
		voice.add(new XGKnob(values.get("mp_pan")), "B2B3");
		voice.add(new JPanel(), "C2C3");
		voice.add(new XGKnob(values.get("mp_transpose")), "D2D3");
		voice.add(new XGKnob(values.get("mp_detune")), "E2E3");
		voice.add(new JPanel(), "F2F3");
		voice.add(new XGKnob(values.get("mp_dry")), "G2g3");
		voice.add(new XGKnob(values.get("mp_rev")), "h2h3");
		voice.add(new XGKnob(values.get("mp_cho")), "i2i3");
		voice.add(new XGKnob(values.get("mp_var")), "j2j3");
		voice.add(new JPanel(), "k2k3");
		voice.add(new XGKnob(values.get("mp_vib_rate")), "l2l3");
		voice.add(new XGKnob(values.get("mp_vib_depth")), "m2m3");
		voice.add(new XGKnob(values.get("mp_vib_delay")), "n2n3");

		voice.add(new XGAEG(values.get("mp_aeg_attack"), values.get("mp_aeg_decay"), values.get("mp_aeg_release")), "a4f7");
		voice.add(new XGScale(values.get("mp_scale_c"), values.get("mp_scale_cis"), values.get("mp_scale_d"), values.get("mp_scale_dis"), values.get("mp_scale_e"), values.get("mp_scale_f"),
			values.get("mp_scale_fis"), values.get("mp_scale_g"), values.get("mp_scale_gis"), values.get("mp_scale_a"), values.get("mp_scale_ais"), values.get("mp_scale_b")), "g4l7");
		voice.add(new XGPEG(values.get("mp_peg_init_level"), values.get("mp_peg_attack_time"), values.get("mp_peg_release_level"), values.get("mp_peg_release_time")), "m4r7");

	//	control.add(new XGTabbedFrame());

		root.add(midi);
		root.add(voice);
		root.add(control);

		return root;
	}
}
