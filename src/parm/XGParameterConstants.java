package parm;

public interface XGParameterConstants
{	enum ParameterTags
	{	FX_RevType, FX_RevReturn, FX_RevPan,
		FX_ChoType, FX_ChoReturn, FX_ChoPan, FX_ChoToRev,
		FX_VarType, FX_VarReturn, FX_VarPan, FX_VarToRev, FX_VarToCho, FX_VarCon, FX_VarPart, FX_VarMWControlDepth, FX_VarPBControlDepth, FX_VarCATControlDepth, FX_VarAC1ControlDepth, FX_VARAC2ControlDepth,
		FX_P01, FX_P02, FX_P03, FX_P04, FX_P05, FX_P06, FX_P07, FX_P08, FX_P09, FX_P10, FX_P11, FX_P12, FX_P13, FX_P14, FX_P15, FX_P16,
/*01*/	FX_RevTime, FX_Diffusion, FX_InitDelay, FX_HPFCutoff, FX_LPFCutoff, FX_Wet, FX_RevDelay, FX_RevDensity, FX_ErBalance, FX_HiDamp,
/*02*/	FX_Width, FX_Height, FX_Depth, FX_WallVary, FX_RevFBLevel,
/*03*/	FX_LDelay, FX_CDelay, FX_RDelay, FX_FBDelay, FX_CLevel,
/*04*/	FX_FB2Delay,
/*05*/	FX_LDelay1, FX_LFBLevel, FX_RDelay1, FX_RFBLevel, FX_LDelay2, FX_RDelay2, FX_Delay2Level,
/*06*/	FX_LRDelay, FX_RLDelay, FX_InputSel,
/*07*/	FX_ErType, FX_RoomSize, FX_Liveness, FX_ErDensity,
/*08*/	FX_GateType,
/*09*/	FX_DelayTime,
/*10*/	FX_LFOFreq, FX_LFOPMDepth, FX_DelayOffset, FX_EQLowFreq, FX_EQLowGain, FX_EQHiFreq, FX_EQHiGain, FX_EQMidFreq, FX_EQMidGain, FX_EQMidWidth, FX_LFOAMDepth,

	{"LFO Depth",0,127,1},				/*	49	*/
	{"LFO Phase Diff",4,124,29},		/*	50	*/
	{"AM Depth",0,127,1},				/*	51	*/
	{"PM Depth",0,127,1},				/*	52	*/
	{"Left/Right Depth",0,127,1},		/*	53	*/
	{"Front/Rear Depth",0,127,1},		/*	54	*/
	{"Pan Direction",0,5,27},			/*	55	*/
	{"Phase Shift Ofst",0,127,1},		/*	56	*/
	{"Stage",4,8,1},					/*	57	*/
	{"Diffusion",0,1,31},				/*	58	*/
	{"Drive",0,127,1},					/*	59	*/
	{"Outp Level 1",0,127,1},			/*	60	*/
	{"Amp Type",0,3,32},				/*	61	*/
	{"Cutoff FreqOfst",0,127,1},		/*	62	*/
	{"Resonance",10,120,3},				/*	63	*/
	{"Pitch",40,88,5},					/*	64	*/
	{"Initial Delay",0,127,107},		/*	65	*/
	{"Fine 1",14,114,5},				/*	66	*/
	{"Fine 2",14,114,5},				/*	67	*/
	{"FB Gain",1,127,5},				/*	68	*/
	{"Pan 1",1,127,2},					/*	69	*/
	{"Pan 2",1,127,2},					/*	70	*/
	{"Outp Level 2",0,127,1},			/*	71	*/
	{"HPF Cutoff",28,58,103},			/*	72	*/
	{"Mix Level",0,127,1},				/*	73	*/
	{"Sensitive",0,127,1},				/*	74	*/
	{"Attack",0,19,108},				/*	75	*/
	{"Release",0,15,109},				/*	76	*/
	{"Treshold",79,121,19},				/*	77	*/
	{"Ratio",0,7,110},					/*	78	*/
	{"Treshold",55,97,19}};				/*	79	*/
	}


	private final XGParameter[] fxParms_reverb01 = new XGParameter[]
	{	new XGParameter(int id, int min, int max, String longName, String shortName)

	}
/*
	{"",0,0,0},							/*	00	*/
	{"Rev Time",0,69,104},				/*	01	*/
	{"Diffusion",0,10,1},				/*	02	*/
	{"InitialDelay",0,63,105},			/*	03	*/
	{"HPF Cutoff",0,52,103},			/*	04	*/
	{"LPF Cutoff",34,60,103},			/*	05	*/
	{"Dry/Wet",1,127,5},				/*	06	*/
	{"Rev Delay",0,63,105},				/*	07	*/
	{"Density",0,4,1},					/*	08	*/
	{"Er/Rev Bal",1,127,5},				/*	09	*/
	{"Hi Damp",1,10,3},					/*	10	*/
	{"Width",0,37,111},					/*	11	*/
	{"Height",0,73,111},				/*	12	*/
	{"Depth",0,104,111},				/*	13	*/
	{"Wall Vary",0,30,1},				/*	14	*/
	{"FB Level",1,127,5},				/*	15	*/
	{"L Delay",1,7150,3},			/*	16	*/
	{"R Delay",1,7150,3},			/*	17	*/
	{"C Delay",1,7150,3},			/*	18	*/
	{"FB Delay 1",1,7150,3},			/*	19	*/
	{"C Level",0,127,1},				/*	20	*/
	{"FB Delay 2",1,7150,3},			/*	21	*/
	{"L Delay1",1,3550,3},			/*	22	*/
	{"L FB Lvl",1,127,5},			/*	23	*/
	{"R Delay1",1,3550,3},			/*	24	*/
	{"R FB Lvl",1,127,5},			/*	25	*/
	{"L Delay2",1,3550,3},			/*	26	*/
	{"R Delay2",1,3550,3},			/*	27	*/
	{"Delay2 Lvl",0,127,1},				/*	28	*/
	{"L->R Delay",1,3550,3},			/*	29	*/
	{"R->L Delay",1,3550,3},			/*	30	*/
	{"Input Sel.",0,2,21},				/*	31	*/
	{"Type",0,5,17},					/*	32	*/
	{"Room Size",0,44,106},				/*	33	*/
	{"Liveness",0,10,1},				/*	34	*/
	{"Density",0,3,1},					/*	35	*/
	{"Type",0,1,24},					/*	36	*/
	{"Delay Time",0,127,107},			/*	37	*/
	{"LFO Freq",0,127,101},				/*	38	*/
	{"LFO PM Dep",0,63,1},				/*	39	*/
	{"Delay Ofst",0,127,102},			/*	40	*/
	{"EQ Lo Freq",4,40,103},			/*	41	*/
	{"EQ Lo Gain",52,76,5},				/*	42	*/
	{"EQ Hi Freq",28,58,103},			/*	43	*/
	{"EQ Hi Gain",52,76,5},				/*	44	*/
	{"EQ Mi Freq",14,54,103},			/*	45	*/
	{"EQ Mi Gain",52,76,5},				/*	46	*/
	{"EQ Mi Widt",10,120,3},			/*	47	*/
	{"LFO AM Dep",0,127,1},				/*	48	*/
	{"LFO Depth",0,127,1},				/*	49	*/
	{"LFO Phase Diff",4,124,29},		/*	50	*/
	{"AM Depth",0,127,1},				/*	51	*/
	{"PM Depth",0,127,1},				/*	52	*/
	{"Left/Right Depth",0,127,1},		/*	53	*/
	{"Front/Rear Depth",0,127,1},		/*	54	*/
	{"Pan Direction",0,5,27},			/*	55	*/
	{"Phase Shift Ofst",0,127,1},		/*	56	*/
	{"Stage",4,8,1},					/*	57	*/
	{"Diffusion",0,1,31},				/*	58	*/
	{"Drive",0,127,1},					/*	59	*/
	{"Outp Level 1",0,127,1},			/*	60	*/
	{"Amp Type",0,3,32},				/*	61	*/
	{"Cutoff FreqOfst",0,127,1},		/*	62	*/
	{"Resonance",10,120,3},				/*	63	*/
	{"Pitch",40,88,5},					/*	64	*/
	{"Initial Delay",0,127,107},		/*	65	*/
	{"Fine 1",14,114,5},				/*	66	*/
	{"Fine 2",14,114,5},				/*	67	*/
	{"FB Gain",1,127,5},				/*	68	*/
	{"Pan 1",1,127,2},					/*	69	*/
	{"Pan 2",1,127,2},					/*	70	*/
	{"Outp Level 2",0,127,1},			/*	71	*/
	{"HPF Cutoff",28,58,103},			/*	72	*/
	{"Mix Level",0,127,1},				/*	73	*/
	{"Sensitive",0,127,1},				/*	74	*/
	{"Attack",0,19,108},				/*	75	*/
	{"Release",0,15,109},				/*	76	*/
	{"Treshold",79,121,19},				/*	77	*/
	{"Ratio",0,7,110},					/*	78	*/
	{"Treshold",55,97,19}};				/*	79	*/
 */
}
