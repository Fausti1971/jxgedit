package parm;

import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	static final String DEF_OBJTYPE = "unknown object";
	static final String DEF_PARAMETERNAME = "unknown parameter: ";
	static final int  DEF_MIN = 0, DEF_MAX = 127;
	static final XGValueTranslator DEF_TRANSLATOR = XGValueTranslator.translateToText;
/*
	static XGParameter[] fx1_parameters = 
	{	new XGParameter(FX1_REV_TYPE, "translateMap", "fx_programs", "reverb", 0, 0, "Reverb Program", "RevPrg"),
		new XGParameter(FX1_REV_P01, FX1_REV_TYPE, 1),
		new XGParameter(FX1_REV_P02, FX1_REV_TYPE, 2),
		new XGParameter(FX1_REV_P03, FX1_REV_TYPE, 3),
		new XGParameter(FX1_REV_P04, FX1_REV_TYPE, 4),
		new XGParameter(FX1_REV_P05, FX1_REV_TYPE, 5),
		new XGParameter(FX1_REV_P06, FX1_REV_TYPE, 6),
		new XGParameter(FX1_REV_P07, FX1_REV_TYPE, 7),
		new XGParameter(FX1_REV_P08, FX1_REV_TYPE, 8),
		new XGParameter(FX1_REV_P09, FX1_REV_TYPE, 9),
		new XGParameter(FX1_REV_P10, FX1_REV_TYPE, 10),
		new XGParameter(FX1_REV_P11, FX1_REV_TYPE, 11),
		new XGParameter(FX1_REV_P12, FX1_REV_TYPE, 12),
		new XGParameter(FX1_REV_P13, FX1_REV_TYPE, 13),
		new XGParameter(FX1_REV_P14, FX1_REV_TYPE, 14),
		new XGParameter(FX1_REV_P15, FX1_REV_TYPE, 15),
		new XGParameter(FX1_REV_P16, FX1_REV_TYPE, 16),
		new XGParameter(FX1_REV_RETURN, 0, 127, "Reverb Return", "RevRet"),
		new XGParameter(FX1_REV_PAN, 0, 127, "Reverb Panorama", "RevPan"),
	
		new XGParameter(FX1_CHO_TYPE, "translateMap", "fx_programs", "chorus", 0, 0, "Chorus Program", "ChoPrg"),
		new XGParameter(FX1_CHO_P01, FX1_CHO_TYPE, 1),
		new XGParameter(FX1_CHO_P02, FX1_CHO_TYPE, 2),
		new XGParameter(FX1_CHO_P03, FX1_CHO_TYPE, 3),
		new XGParameter(FX1_CHO_P04, FX1_CHO_TYPE, 4),
		new XGParameter(FX1_CHO_P05, FX1_CHO_TYPE, 5),
		new XGParameter(FX1_CHO_P06, FX1_CHO_TYPE, 6),
		new XGParameter(FX1_CHO_P07, FX1_CHO_TYPE, 7),
		new XGParameter(FX1_CHO_P08, FX1_CHO_TYPE, 8),
		new XGParameter(FX1_CHO_P09, FX1_CHO_TYPE, 9),
		new XGParameter(FX1_CHO_P10, FX1_CHO_TYPE, 10),
		new XGParameter(FX1_CHO_P11, FX1_CHO_TYPE, 11),
		new XGParameter(FX1_CHO_P12, FX1_CHO_TYPE, 12),
		new XGParameter(FX1_CHO_P13, FX1_CHO_TYPE, 13),
		new XGParameter(FX1_CHO_P14, FX1_CHO_TYPE, 14),
		new XGParameter(FX1_CHO_P15, FX1_CHO_TYPE, 15),
		new XGParameter(FX1_CHO_P16, FX1_CHO_TYPE, 16),
		new XGParameter(FX1_CHO_RETURN, 0, 127, "Chorus Return","ChoRet"),
		new XGParameter(FX1_CHO_PAN, 0, 127, "Chorus Panorama", "ChoPan"),
		new XGParameter(FX1_CHO_TOREV, 0, 127, "Chorus To Reverb", "ChoRev"),
	
		new XGParameter(FX1_VAR_TYPE, "translateMap", "fx_programs", "variation", 0, 0, "Variation Program", "VarPrg"),
		new XGParameter(FX1_VAR_P01, FX1_VAR_TYPE, 1),
		new XGParameter(FX1_VAR_P02, FX1_VAR_TYPE, 2),
		new XGParameter(FX1_VAR_P03, FX1_VAR_TYPE, 3),
		new XGParameter(FX1_VAR_P04, FX1_VAR_TYPE, 4),
		new XGParameter(FX1_VAR_P05, FX1_VAR_TYPE, 5),
		new XGParameter(FX1_VAR_P06, FX1_VAR_TYPE, 6),
		new XGParameter(FX1_VAR_P07, FX1_VAR_TYPE, 7),
		new XGParameter(FX1_VAR_P08, FX1_VAR_TYPE, 8),
		new XGParameter(FX1_VAR_P09, FX1_VAR_TYPE, 9),
		new XGParameter(FX1_VAR_P10, FX1_VAR_TYPE, 10),
		new XGParameter(FX1_VAR_P11, FX1_VAR_TYPE, 11),
		new XGParameter(FX1_VAR_P12, FX1_VAR_TYPE, 12),
		new XGParameter(FX1_VAR_P13, FX1_VAR_TYPE, 13),
		new XGParameter(FX1_VAR_P14, FX1_VAR_TYPE, 14),
		new XGParameter(FX1_VAR_P15, FX1_VAR_TYPE, 15),
		new XGParameter(FX1_VAR_P16, FX1_VAR_TYPE, 16),
		new XGParameter(FX1_VAR_RETURN, 0, 127, "Variation Return","VarRet"),
		new XGParameter(FX1_VAR_PAN, 0, 127, "Variation Panorama", "VarPan"),
		new XGParameter(FX1_VAR_TOREV, 0, 127, "Variation To Reverb", "VarRev"),
		new XGParameter(FX1_VAR_TOCHO, 0, 127, "Variation To Chorus", "VarCho"),
		new XGParameter(FX1_VAR_CONN, 0, 127, "Variation Connection", "VarCon"),
		new XGParameter(FX1_VAR_PART, 0, 31, "Variation Part", "VarPart"),
		new XGParameter(FX1_VAR_MWCTRLDEPTH, 0, 127, "Variation Modwheel Control Depth", "VarMW"),
		new XGParameter(FX1_VAR_PBCTRLDEPTH, 0, 127, "Variation Pitchbend Control Depth", "VarPB"),
		new XGParameter(FX1_VAR_CATCTRLDEPTH, 0, 127, "Variation Channel Aftertouch Control Depth", "VarCAT"),
		new XGParameter(FX1_VAR_AC1CTRLDEPTH, 0, 127, "Variation Alternate Controller 1 Control Depth", "VarAC1"),
		new XGParameter(FX1_VAR_AC2CTRLDEPTH, 0, 127, "Variation Alternate Controller 2 Control Depth", "VarAC2")
	};

	XGParameter[] fx2_parameters =
	{	new XGParameter(FX2_INS_TYPE, "translateMap", "fx_programs", "variation", 0, 0, "Variation Program", "VarPrg"),
		new XGParameter(FX2_INS_P01, FX2_INS_TYPE, 1),
		new XGParameter(FX2_INS_P02, FX2_INS_TYPE, 2),
		new XGParameter(FX2_INS_P03, FX2_INS_TYPE, 3),
		new XGParameter(FX2_INS_P04, FX2_INS_TYPE, 4),
		new XGParameter(FX2_INS_P05, FX2_INS_TYPE, 5),
		new XGParameter(FX2_INS_P06, FX2_INS_TYPE, 6),
		new XGParameter(FX2_INS_P07, FX2_INS_TYPE, 7),
		new XGParameter(FX2_INS_P08, FX2_INS_TYPE, 8),
		new XGParameter(FX2_INS_P09, FX2_INS_TYPE, 9),
		new XGParameter(FX2_INS_P10, FX2_INS_TYPE, 10),
		new XGParameter(FX2_INS_P11, FX2_INS_TYPE, 11),
		new XGParameter(FX2_INS_P12, FX2_INS_TYPE, 12),
		new XGParameter(FX2_INS_P13, FX2_INS_TYPE, 13),
		new XGParameter(FX2_INS_P14, FX2_INS_TYPE, 14),
		new XGParameter(FX2_INS_P15, FX2_INS_TYPE, 15),
		new XGParameter(FX2_INS_P16, FX2_INS_TYPE, 16),
		new XGParameter(FX2_INS_PART, 0, 31, "Insertion Part", "VarPart"),
		new XGParameter(FX2_INS_MWCTRLDEPTH, 0, 127, "Insertion Modwheel Control Depth", "VarMW"),
		new XGParameter(FX2_INS_PBCTRLDEPTH, 0, 127, "Insertion Pitchbend Control Depth", "VarPB"),
		new XGParameter(FX2_INS_CATCTRLDEPTH, 0, 127, "Insertion Channel Aftertouch Control Depth", "VarCAT"),
		new XGParameter(FX2_INS_AC1CTRLDEPTH, 0, 127, "Insertion Alternate Controller 1 Control Depth", "VarAC1"),
		new XGParameter(FX2_INS_AC2CTRLDEPTH, 0, 127, "Insertion Alternate Controller 2 Control Depth", "VarAC2")
	};

	static final XGParameter
		FX_REVTIME = new XGParameter(0, "translateMap", "reverb_time", "", 0, 69, "Reverb Time", "RevTm"),
		FX_DIFFUSION = new XGParameter(0, 0, 10, "Diffusion", "Diff"),
		FX_INITDELAY = new XGParameter(0, "translateMap", "delay_time200", "", 0, 63, "Initial Delay", "InitDly"),
		FX_HPFCUTOFF = new XGParameter(0, "translateMap", "eq_frequency", "", 0, 52, "HPF Cuttoff", "HPF"),
		FX_LPFCUTOFF = new XGParameter(0, "translateMap", "eq-frequency", "", 34, 60, "LPF Cutoff", "LPF"),
		FX_DRYWET = new XGParameter(0, "translateMap", "dry_wet", "", 1, 127, "Dry/Wet", "Dry/Wet"),
		FX_REVDELAY = new XGParameter(0, "translateMap", "delay_time200", "", 0, 63, "Reverb Delay", "RevDly"),
		FX_DENSITY = new XGParameter(0, 0, 4, "Density", "Dens"),
		FX_REVEARLY = new XGParameter(0, "translateMap", "early_rev", "", 1, 127, "Early Reflection/Reverb-Balance", "ER/Rev"),
		FX_HIDAMP = new XGParameter(0, "translateDiv10", null, null, 1, 10, "Hi Damp", "HiDmp"),
		FX_WIDTH = new XGParameter(0, "translateMap", "rev_dimensions", "", 0, 37, "Width", "Width"),
		FX_HEIGHT = new XGParameter(0, "translateMap", "rev_dimensions", "", 0, 73, "Height", "Height"),
		FX_DEPTH = new XGParameter(0, "translateMap", "rev_dimensions", "", 0, 104, "Depth", "Depth");
		
};


//	{"Depth",0,104,111},				/*	13	*/
//	{"Wall Vary",0,30,1},				/*	14	*/
//	{"FB Level",1,127,5},				/*	15	*/
//	{"L Delay",1,7150,3},			/*	16	*/
//	{"R Delay",1,7150,3},			/*	17	*/
//	{"C Delay",1,7150,3},			/*	18	*/
//	{"FB Delay 1",1,7150,3},			/*	19	*/
//	{"C Level",0,127,1},				/*	20	*/
//	{"FB Delay 2",1,7150,3},			/*	21	*/
//	{"L Delay1",1,3550,3},			/*	22	*/
//	{"L FB Lvl",1,127,5},			/*	23	*/
//	{"R Delay1",1,3550,3},			/*	24	*/
//	{"R FB Lvl",1,127,5},			/*	25	*/
//	{"L Delay2",1,3550,3},			/*	26	*/
//	{"R Delay2",1,3550,3},			/*	27	*/
//	{"Delay2 Lvl",0,127,1},				/*	28	*/
//	{"L->R Delay",1,3550,3},			/*	29	*/
//	{"R->L Delay",1,3550,3},			/*	30	*/
//	{"Input Sel.",0,2,21},				/*	31	*/
//	{"Type",0,5,17},					/*	32	*/
//	{"Room Size",0,44,106},				/*	33	*/
//	{"Liveness",0,10,1},				/*	34	*/
//	{"Density",0,3,1},					/*	35	*/
//	{"Type",0,1,24},					/*	36	*/
//	{"Delay Time",0,127,107},			/*	37	*/
//	{"LFO Freq",0,127,101},				/*	38	*/
//	{"LFO PM Dep",0,63,1},				/*	39	*/
//	{"Delay Ofst",0,127,102},			/*	40	*/
//	{"EQ Lo Freq",4,40,103},			/*	41	*/
//	{"EQ Lo Gain",52,76,5},				/*	42	*/
//	{"EQ Hi Freq",28,58,103},			/*	43	*/
//	{"EQ Hi Gain",52,76,5},				/*	44	*/
//	{"EQ Mi Freq",14,54,103},			/*	45	*/
//	{"EQ Mi Gain",52,76,5},				/*	46	*/
//	{"EQ Mi Widt",10,120,3},			/*	47	*/
//	{"LFO AM Dep",0,127,1},				/*	48	*/
//	{"LFO Depth",0,127,1},				/*	49	*/
//	{"LFO Phase Diff",4,124,29},		/*	50	*/
//	{"AM Depth",0,127,1},				/*	51	*/
//	{"PM Depth",0,127,1},				/*	52	*/
//	{"Left/Right Depth",0,127,1},		/*	53	*/
//	{"Front/Rear Depth",0,127,1},		/*	54	*/
//	{"Pan Direction",0,5,27},			/*	55	*/
//	{"Phase Shift Ofst",0,127,1},		/*	56	*/
//	{"Stage",4,8,1},					/*	57	*/
//	{"Diffusion",0,1,31},				/*	58	*/
//	{"Drive",0,127,1},					/*	59	*/
//	{"Outp Level 1",0,127,1},			/*	60	*/
//	{"Amp Type",0,3,32},				/*	61	*/
//	{"Cutoff FreqOfst",0,127,1},		/*	62	*/
//	{"Resonance",10,120,3},				/*	63	*/
//	{"Pitch",40,88,5},					/*	64	*/
//	{"Initial Delay",0,127,107},		/*	65	*/
//	{"Fine 1",14,114,5},				/*	66	*/
//	{"Fine 2",14,114,5},				/*	67	*/
//	{"FB Gain",1,127,5},				/*	68	*/
//	{"Pan 1",1,127,2},					/*	69	*/
//	{"Pan 2",1,127,2},					/*	70	*/
//	{"Outp Level 2",0,127,1},			/*	71	*/
//	{"HPF Cutoff",28,58,103},			/*	72	*/
//	{"Mix Level",0,127,1},				/*	73	*/
//	{"Sensitive",0,127,1},				/*	74	*/
//	{"Attack",0,19,108},				/*	75	*/
//	{"Release",0,15,109},				/*	76	*/
//	{"Treshold",79,121,19},				/*	77	*/
//	{"Ratio",0,7,110},					/*	78	*/
//	{"Treshold",55,97,19}};				/*	79	*/

//m.put(DR_COARSE, new XGParameter(new XGOpcode(DR_COARSE), 0, 127, "pitch coarse", "coarse"));
//m.put(DR_FINE, new XGParameter(new XGOpcode(DR_FINE), 0, 127, "pitch fine", "fine"));
//m.put(DR_VOL, new XGParameter(new XGOpcode(DR_VOL), 0, 127, "level", "vol"));
//m.put(DR_GRP, new XGParameter(new XGOpcode(DR_GRP), 0, 127, "alternate group", "alt"));
//m.put(DR_PAN, new XGParameter(new XGOpcode(DR_PAN), 0, 127, "panorama", "pan"));
//m.put(DR_REV, new XGParameter(new XGOpcode(DR_REV), 0, 127, "reverb send", "rev"));
//m.put(DR_CHO, new XGParameter(new XGOpcode(DR_CHO), 0, 127, "chorus send", "cho"));
//m.put(DR_VAR, new XGParameter(new XGOpcode(DR_VAR), 0, 127, "variation send", "var"));
//m.put(DR_ASSIGN, new XGParameter(new XGOpcode(DR_ASSIGN), 0, 1, "key assign", "key"));
//m.put(DR_RCVOFF, new XGParameter(new XGOpcode(DR_RCVOFF), 0, 1, "receive note off", "rcvoff"));
//m.put(DR_RCVON, new XGParameter(new XGOpcode(DR_RCVON), 0, 1, "receive note on", "rcvon"));
//m.put(DR_CUTPOFF, new XGParameter(new XGOpcode(DR_CUTPOFF), 0, 127, "filter cutoff", "cut"));
//m.put(DR_RESO, new XGParameter(new XGOpcode(DR_RESO), 0, 127, "filter resonance", "res"));
//m.put(DR_ATTACK, new XGParameter(new XGOpcode(DR_ATTACK), 0, 127, "eg attack time", "atck"));
//m.put(DR_DECAY, new XGParameter(new XGOpcode(DR_DECAY), 0, 127, "eg decay time", "decy"));
//m.put(DR_RELEASE, new XGParameter(new XGOpcode(DR_RELEASE), 0, 127, "eg release time", "rels"));
}