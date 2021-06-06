package gui;

import javax.swing.*;

public class XGMultipartEditWindow extends XGEditWindow
{	
	public XGMultipartEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		XGFrame midi = new gui.XGFrame("Midi", GRID * 3, GRID * 2);
		XGFrame voice = new gui.XGFrame("Voice", GRID * 3, GRID * 2);
		XGFrame control = new gui.XGFrame("Control", GRID * 3, GRID * 2);

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		midi.add(new XGKnob(values.get("mp_midi_channel")), "A0A1");
		midi.add(new XGKnob(values.get("mp_reserve")), "b0b1");
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
		voice.add(new XGKnob(values.get("mp_volume")), "h0h1");
		voice.add(new XGKnob(values.get("mp_pan")), "i0i1");
		voice.add(new XGKnob(values.get("mp_transpose")), "k0k1");
		voice.add(new XGKnob(values.get("mp_detune")), "l0l1");
		voice.add(new XGKnob(values.get("mp_dry")), "n0n1");
		voice.add(new XGKnob(values.get("mp_rev")), "o0o1");
		voice.add(new XGKnob(values.get("mp_cho")), "p0p1");
		voice.add(new XGKnob(values.get("mp_var")), "q0q1");

		voice.add(new XGKnob(values.get("mp_eg_lpf")), "a2a3");
		voice.add(new XGKnob(values.get("mp_lpf_cut")), "b2b3");
		voice.add(new XGKnob(values.get("mp_lpf_reso")), "c2c3");
		voice.add(new XGKnob(values.get("mp_hpf_cut")), "d2d3");
		voice.add(new XGKnob(values.get("mp_eq_bass_freq")), "f2f3");
		voice.add(new XGKnob(values.get("mp_eq_bass")), "g2g3");
		voice.add(new XGKnob(values.get("mp_eq_treble_freq")), "i2i3");
		voice.add(new XGKnob(values.get("mp_eq_treble")), "j2j3");
		voice.add(new XGKnob(values.get("mp_vib_rate")), "l2l3");
		voice.add(new XGKnob(values.get("mp_vib_depth")), "m2m3");
		voice.add(new XGKnob(values.get("mp_vib_delay")), "n2n3");
		voice.add(new XGCheckbox(values.get("mp_portamento")), "p3q3");
		voice.add(new XGKnob(values.get("mp_portamento_time")), "r2r3");

		voice.add(new XGAEG(values.get("mp_aeg_attack"), values.get("mp_aeg_decay"), values.get("mp_aeg_release")), "a4f7");
		voice.add(new XGScale(values.get("mp_scale_c"), values.get("mp_scale_cis"), values.get("mp_scale_d"), values.get("mp_scale_dis"), values.get("mp_scale_e"), values.get("mp_scale_f"),
			values.get("mp_scale_fis"), values.get("mp_scale_g"), values.get("mp_scale_gis"), values.get("mp_scale_a"), values.get("mp_scale_ais"), values.get("mp_scale_b")), "g4l7");
		voice.add(new XGPEG(values.get("mp_peg_init_level"), values.get("mp_peg_attack_time"), values.get("mp_peg_release_level"), values.get("mp_peg_release_time")), "m4r7");

		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabs.setFont(SMALL_FONT);
		XGFrame tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_mw_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_mw_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_mw_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_mw_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_mw_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_mw_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_mw_offset")), "h0h0");
		tabs.addTab("MW", null, tab, "Modulation Wheel");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_pb_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_pb_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_pb_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_pb_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_pb_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_pb_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_pb_offset")), "h0h0");
		tab.add(new XGKnob(values.get("mp_pb_lo")), "i0i0");
		tabs.addTab("PB", null, tab, "Pitchbend Wheel");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_cat_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_cat_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_cat_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_cat_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_cat_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_cat_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_cat_offset")), "h0h0");
		tabs.addTab("CAT", null, tab, "Channel Aftertouch");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_pat_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_pat_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_pat_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_pat_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_pat_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_pat_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_pat_offset")), "h0h0");
		tabs.addTab("PAT", null, tab, "Polyphonic Aftertouch");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_ac1_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_ac1_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_ac1_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_ac1_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_ac1_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_ac1_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_ac1_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_ac1_offset")), "h0h0");
		tabs.addTab("AC1", null, tab, "Assignable Controller 1");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_ac2_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_ac2_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_ac2_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_ac2_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_ac2_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_ac2_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_ac2_lfo_amp")), "g0g0");
		tab.add(new XGKnob(values.get("mp_ac2_offset")), "h0h0");
		tabs.addTab("AC2", null, tab, "Assignable Controller 2");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_cbc1_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_cbc1_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_cbc1_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_cbc1_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_cbc1_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_cbc1_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_cbc1_lfo_amp")), "g0g0");
		tabs.addTab("CBC1", null, tab, "CBC1");

		tab = new XGFrame(null, 3 * GRID, 4 * GRID);
		tab.add(new XGKnob(values.get("mp_cbc2_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_cbc2_pitch")), "b0b0");
		tab.add(new XGKnob(values.get("mp_cbc2_lpf")), "c0c0");
		tab.add(new XGKnob(values.get("mp_cbc2_amp")), "d0d0");
		tab.add(new XGKnob(values.get("mp_cbc2_lfo_pitch")), "e0e0");
		tab.add(new XGKnob(values.get("mp_cbc2_lfo_lpf")), "f0f0");
		tab.add(new XGKnob(values.get("mp_cbc2_lfo_amp")), "g0g0");
		tabs.addTab("CBC2", null, tab, "CBC2");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_press_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_press")), "b0b0");
		tabs.addTab("Prs", null, tab, "VL Pressure Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_embr_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_embr")), "b0b0");
		tabs.addTab("Emb", null, tab, "VL Embouchure Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_tongue_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_tongue")), "b0b0");
		tabs.addTab("Tng", null, tab, "VL Tonguing Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_scream_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_scream")), "b0b0");
		tabs.addTab("Scr", null, tab, "VL Scream Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_breath_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_breath")), "b0b0");
		tabs.addTab("Brt", null, tab, "VL Breath Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_growl_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_growl")), "b0b0");
		tabs.addTab("Grl", null, tab, "VL Growl Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_throat_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_throat")), "b0b0");
		tabs.addTab("Thr", null, tab, "VL Throat Formant Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_harmonic_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_harmonic")), "b0b0");
		tabs.addTab("Hrm", null, tab, "VL Harmonic Enhancer Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_damping_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_damping")), "b0b0");
		tabs.addTab("Dmp", null, tab, "VL Damping Control");

		tab = new XGFrame(null, GRID * 3, GRID * 4);
		tab.add(new XGKnob(values.get("mp_vl_absorp_nr")), "a0a0");
		tab.add(new XGKnob(values.get("mp_vl_absorp")), "b0b0");
		tabs.addTab("Abs", null, tab, "VL Absorption Control");

		control.add(tabs, "a0k2");

		XGFrame vl = new XGFrame("VL", GRID * 3, GRID * 2);
		vl.add(new XGCheckbox(values.get("mp_vl_noteassign")), "b0c0");
		vl.add(new XGCombo(values.get("mp_vl_notefilter")), "b1c1");
		//vl.add(new XGKnob(values.get("mp_vl_amp_break")), "c0c1");
		//vl.add(new XGKnob(values.get("mp_vl_amp_depth")), "d0d1");
		//vl.add(new XGKnob(values.get("mp_vl_lpf_break")), "e0e1");
		//vl.add(new XGKnob(values.get("mp_vl_lpf_depth")), "f0f1");
		control.add(vl, "l0r2");

		root.add(midi);
		root.add(voice);
		root.add(control);

		return root;
	}
}
