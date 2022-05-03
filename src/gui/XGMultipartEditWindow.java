package gui;

import module.XGModule;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.swing.*;
import java.awt.*;

public class XGMultipartEditWindow extends XGEditWindow
{
	public XGMultipartEditWindow(XGModule mod)
	{	super( mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(false);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		//XGFrame main = new XGFrame(false);
		//	main.add(new XGKnob(values.get("mp_volume")), "0,0,1,4");
		//	main.add(new XGKnob(values.get("mp_pan")), "1,0,1,4");
		//	main.add(new XGCombo(values.get("mp_partmode")), "0,4,2,2");
		//	main.add(new XGCombo(values.get("mp_audio_output")), "2,4,2,2");
		//	main.add(new XGProgramSelector(values.get("mp_program")), "0,6,4,2");
		//	main.add(new XGRadio(values.get("mp_key_on_assign"), XGComponent.XGOrientation.horizontal), "2,0,2,2");
		//	main.add(new XGRadio(values.get("mp_poly"), XGComponent.XGOrientation.horizontal), "2,2,2,2");
		//root.add(main, "0,0,6,2");

		XGFrame midi = new XGFrame(false);
			//midi.add(new XGKnob(values.get("mp_midi_channel")), "0,0,1,2");
			//midi.add(new XGKnob(values.get("mp_reserve")), "1,0,1,2");
			//midi.add(new XGFlagBox("MIDI Filter", values.get("mp_rcv_pb"), values.get("mp_rcv_cat"), values.get("mp_rcv_prg"), values.get("mp_rcv_cc"), values.get("mp_rcv_pat"), values.get("mp_rcv_note"),
			//	values.get("mp_rcv_rpn"), values.get("mp_rcv_nrpn"), values.get("mp_rcv_mw"), values.get("mp_rcv_vol"), values.get("mp_rcv_pan"), values.get("mp_rcv_exp"), values.get("mp_rcv_hold"), values.get("mp_rcv_portamento"),
			//	values.get("mp_rcv_sostenuto"), values.get("mp_rcv_softpedal"), values.get("mp_rcv_bank")), "0,2,2,1");
			//midi.add(new XGRange(values.get("mp_note_lo"), values.get("mp_note_hi")), "0,3,2,1");
			//XGFrame velo = new XGFrame("Velocity");
			//	velo.add(new XGVelocityEnvelope(this.module), "0,0,4,5");
			//	velo.add(new XGKnob(values.get("mp_velo_lo")), "0,5,1,4");
			//	velo.add(new XGKnob(values.get("mp_velo_offset")), "1,5,1,4");
			//	velo.add(new XGKnob(values.get("mp_velo_depth")), "2,5,1,4");
			//	velo.add(new XGKnob(values.get("mp_velo_hi")), "3,5,1,4");
			//midi.add(velo, "2,0,4,4");
		root.add(midi, "6,0,10,2");

		XGFrame tune = new XGFrame("Pitch");
			//tune.add(new XGKnob(values.get("mp_transpose")), "0,0,1,4");
			//tune.add(new XGKnob(values.get("mp_detune")), "1,0,1,4");
			//
			//XGFrame port = new XGFrame("Portamento");
			//	port.add(new XGCheckbox(values.get("mp_portamento")), "0,0,2,1");
			//	port.add(new XGSlider(values.get("mp_portamento_time")), "0,1,2,2");
			//tune.add(port, "2,0,2,4");
			//
			//XGFrame vib = new XGFrame("Vibrato");
			//	vib.add(new XGKnob(values.get("mp_vib_rate")), "0,0,1,4");
			//	vib.add(new XGKnob(values.get("mp_vib_depth")), "1,0,1,4");
			//	vib.add(new XGKnob(values.get("mp_vib_delay")), "2,0,1,4");
			//tune.add(vib, "0,4,4,4");

			//XGFrame peg = new XGFrame("Pitch Envelope");
			//	peg.add(new XGPitchEnvelope(values.get("mp_peg_init_level"), values.get("mp_peg_attack_time"), values.get("mp_peg_release_level"), values.get("mp_peg_release_time")), "0,0,4,5");
			//	peg.add(new XGKnob(values.get("mp_peg_init_level")), "0,5,1,4");
			//	peg.add(new XGKnob(values.get("mp_peg_attack_time")), "1,5,1,4");
			//	peg.add(new XGKnob(values.get("mp_peg_release_time")), "2,5,1,4");
			//	peg.add(new XGKnob(values.get("mp_peg_release_level")), "3,5,1,4");
			//tune.add(peg, "4,0,5,8");

			//XGFrame scale = new XGFrame("Scale");
			//	scale.add(new XGKnob(values.get("mp_scale_c")), "0,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_cis")), "1,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_d")), "2,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_dis")), "3,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_e")), "4,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_f")), "5,0,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_fis")), "0,4,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_g")), "1,4,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_gis")), "2,4,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_a")), "3,4,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_ais")), "4,4,1,4");
			//	scale.add(new XGKnob(values.get("mp_scale_b")), "5,4,1,4");
			//tune.add(scale, "9,0,6,8");
		root.add(tune, "0,2,16,2");

		//XGFrame fx = new XGFrame("Effect");
		//	fx.add(new XGKnob(values.get("mp_dry")), "0,0,1,4");
		//	fx.add(new XGKnob(values.get("mp_rev")), "1,0,1,4");
		//	fx.add(new XGKnob(values.get("mp_cho")), "2,0,1,4");
		//	fx.add(new XGKnob(values.get("mp_var")), "3,0,1,4");
		//root.add(fx, "0,4,5,1");

		//XGFrame filter = new XGFrame("Filter");
		//	filter.add(new XGKnob(values.get("mp_eg_lpf")), "0,0,1,4");
		//	filter.add(new XGKnob(values.get("mp_lpf_cut")), "1,0,1,4");
		//	filter.add(new XGKnob(values.get("mp_lpf_reso")), "2,0,1,4");
		//	filter.add(new XGKnob(values.get("mp_hpf_cut")), "3,0,1,4");
		//root.add(filter, "5,4,5,1");

		//XGFrame eq = new XGFrame("EQ");
		//	eq.add(new XGKnob(values.get("mp_eq_bass_freq")), "0,0,1,4");
		//	eq.add(new XGKnob(values.get("mp_eq_bass")), "1,0,1,4");
		//	eq.add(new XGKnob(values.get("mp_eq_treble_freq")), "2,0,1,4");
		//	eq.add(new XGKnob(values.get("mp_eq_treble")), "3,0,1,4");
		//root.add(eq, "0,5,5,1");

		//XGFrame aeg = new XGFrame("Amplifier Envelope");
		//	aeg.add(new XGAEG(values.get("mp_aeg_attack"), values.get("mp_aeg_decay"), values.get("mp_aeg_release")), "0,0,3,5");
		//	aeg.add(new XGKnob(values.get("mp_aeg_attack")), "0,5,1,4");
		//	aeg.add(new XGKnob(values.get("mp_aeg_decay")), "1,5,1,4");
		//	aeg.add(new XGKnob(values.get("mp_aeg_release")), "2,5,1,4");
		//root.add(aeg, "10,4,6,2");

		Dimension tabDim = new Dimension(9,1);
		XGFrame control = new XGFrame(false);
			JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
			XGFrame tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_mw_pitch")), "1,0,1,1");			//1
				tab.add(new XGKnob(values.get("mp_mw_lpf")), "2,0,1,1");				//2
				tab.add(new XGKnob(values.get("mp_mw_amp")), "3,0,1,1");				//3
				tab.add(new XGKnob(values.get("mp_mw_lfo_pitch")), "4,0,1,1");		//4
				tab.add(new XGKnob(values.get("mp_mw_lfo_lpf")), "5,0,1,1");			//5
				tab.add(new XGKnob(values.get("mp_mw_lfo_amp")), "6,0,1,1");			//6
				tab.add(new XGKnob(values.get("mp_mw_offset")), "7,0,1,1");			//7
			tabs.addTab("MW", null, tab, "Modulation Wheel");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_pb_pitch")), "1,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_lfo_amp")), "6,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_offset")), "7,0,1,1");
				tab.add(new XGKnob(values.get("mp_pb_lo")), "8,0,1,1");				//8
			tabs.addTab("PB", null, tab, "Pitchbend Wheel");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_cat_pitch")), "1,0,1,1");			//1
				tab.add(new XGKnob(values.get("mp_cat_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_cat_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_cat_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_cat_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_cat_lfo_amp")), "6,0,1,1");
				tab.add(new XGKnob(values.get("mp_cat_offset")), "7,0,1,1");
			tabs.addTab("CAT", null, tab, "Channel Aftertouch");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_pat_pitch")), "1,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_lfo_amp")), "6,0,1,1");
				tab.add(new XGKnob(values.get("mp_pat_offset")), "7,0,1,1");
			tabs.addTab("PAT", null, tab, "Polyphonic Aftertouch");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_ac1_nr")), "0,0,1,1");				//0
				tab.add(new XGKnob(values.get("mp_ac1_pitch")), "1,0,1,1");			//1
				tab.add(new XGKnob(values.get("mp_ac1_lpf")), "2,0,1,1");			//2
				tab.add(new XGKnob(values.get("mp_ac1_amp")), "3,0,1,1");			//3
				tab.add(new XGKnob(values.get("mp_ac1_lfo_pitch")), "4,0,1,1");		//4
				tab.add(new XGKnob(values.get("mp_ac1_lfo_lpf")), "5,0,1,1");		//5
				tab.add(new XGKnob(values.get("mp_ac1_lfo_amp")), "6,0,1,1");		//6
				tab.add(new XGKnob(values.get("mp_ac1_offset")), "7,0,1,1");			//7
			tabs.addTab("AC1", null, tab, "Assignable Controller 1");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_ac2_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_pitch")), "1,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_lfo_amp")), "6,0,1,1");
				tab.add(new XGKnob(values.get("mp_ac2_offset")), "7,0,1,1");
			tabs.addTab("AC2", null, tab, "Assignable Controller 2");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_cbc1_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_pitch")), "1,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc1_lfo_amp")), "6,0,1,1");
			tabs.addTab("CBC1", null, tab, "CBC1");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_cbc2_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_pitch")), "1,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_lpf")), "2,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_amp")), "3,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_lfo_pitch")), "4,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_lfo_lpf")), "5,0,1,1");
				tab.add(new XGKnob(values.get("mp_cbc2_lfo_amp")), "6,0,1,1");
			tabs.addTab("CBC2", null, tab, "CBC2");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_press_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_press")), "1,0,1,1");
			tabs.addTab("Prs", null, tab, "VL Pressure Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_embr_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_embr")), "1,0,1,1");
			tabs.addTab("Emb", null, tab, "VL Embouchure Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_tongue_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_tongue")), "1,0,1,1");
			tabs.addTab("Tng", null, tab, "VL Tonguing Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_scream_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_scream")), "1,0,1,1");
			tabs.addTab("Scr", null, tab, "VL Scream Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_breath_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_breath")), "1,0,1,1");
			tabs.addTab("Brt", null, tab, "VL Breath Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_growl_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_growl")), "1,0,1,1");
			tabs.addTab("Grl", null, tab, "VL Growl Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_throat_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_throat")), "1,0,1,1");
			tabs.addTab("Thr", null, tab, "VL Throat Formant Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_harmonic_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_harmonic")), "1,0,1,1");
			tabs.addTab("Hrm", null, tab, "VL Harmonic Enhancer Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_damping_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_damping")), "1,0,1,1");
			tabs.addTab("Dmp", null, tab, "VL Damping Control");

			tab = new XGFrame(tabDim);
				tab.add(new XGKnob(values.get("mp_vl_absorp_nr")), "0,0,1,1");
				tab.add(new XGKnob(values.get("mp_vl_absorp")), "1,0,1,1");
			tabs.addTab("Abs", null, tab, "VL Absorption Control");
		control.add(tabs, "0,0,8,2");

		XGFrame vl = new XGFrame(false);
			vl.add(new XGCheckbox(values.get("mp_vl_noteassign")), "0,0,1,1");
			vl.add(new XGCombo(values.get("mp_vl_notefilter")), "1,0,1,1");
		//vl.add(new XGKnob(values.get("mp_vl_amp_break")), "c0c1");
		//vl.add(new XGKnob(values.get("mp_vl_amp_depth")), "d0d1");
		//vl.add(new XGKnob(values.get("mp_vl_lpf_break")), "e0e1");
		//vl.add(new XGKnob(values.get("mp_vl_lpf_depth")), "f0f1");
			control.add(vl, "8,1,3,1");
		root.add(control, "0,6,16,1");

		return root;
	}
}
