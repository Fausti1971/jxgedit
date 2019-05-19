package parm;

import msg.Bytes;

public interface XGParameterConstants
{	static final String XML_FILE = "rsc/XGParameterMaps.xml"; 
	static final String
		TAG_PARAMETER = "parameter",
		TAG_OFFSET = "offset",
		TAG_BYTECOUNT = "byteCount",
		TAG_BYTETYPE = "byteType",
		TAG_VALUETYPE = "valueType",
		TAG_MIN = "valueMin",
		TAG_MAX = "valueMax",
		TAG_TRANSLATOR = "translator",
		TAG_TRANSLATIONMAP = "translationMap",
		TAG_DESCMAPINDEX = "descriptionMapIndex",
		TAG_DEPENDSOF = "descriptionDependsOf",
		TAG_LONGNAME = "longName",
		TAG_SHORTNAME = "shortName";

	static enum ParameterType {UNKNOWN, COMPLETE, OPCODE, DESCRIPTION};
	static enum ValueType {NUMBER, TEXT, BITMAP};

	static final String DEF_LONGNAME = "unknown parameter", DEF_SHORTNAME = "unknow";
	static final int DEF_BYTECOUNT = 1;
	static final Bytes.ByteType DEF_BYTE_TYPE = Bytes.ByteType.MIDIBYTE;
	static final ValueType DEF_VALUE_TYPE = ValueType.NUMBER;
	static final ValueTranslator DEF_TRANSLATOR = ValueTranslator.translateToText;

	public static final int
	SYS_TUNE = 0x00,
	SYS_VOL = 0x04,
	SYS_ATTENUTATOR = 0x05,
	SYS_TRANSPOSE = 0x05,
	SYS_DRUMRESET = 0x7D,
	SYS_XGON = 0x7E,
	SYS_RESETALL = 0x7F,

	INFO_NAME = 0x00,
	INFO_XGLEVEL1 = 0x0E,
	INFO_XGLEVEL = 0x0F,

	FX1_REV_TYPE = 0x00,
	FX1_REV_P01 = 0x02,
	FX1_REV_P02 = 0x03,
	FX1_REV_P03 = 0x04,
	FX1_REV_P04 = 0x05,
	FX1_REV_P05 = 0x06,
	FX1_REV_P06 = 0x07,
	FX1_REV_P07 = 0x08,
	FX1_REV_P08 = 0x09,
	FX1_REV_P09 = 0x0A,
	FX1_REV_P10 = 0x0B,
	FX1_REV_RETURN = 0x0C,
	FX1_REV_PAN = 0x0D,
	FX1_REV_P11 = 0x10,
	FX1_REV_P12 = 0x11,
	FX1_REV_P13 = 0x12,
	FX1_REV_P14 = 0x13,
	FX1_REV_P15 = 0x14,
	FX1_REV_P16 = 0x15,

	FX1_CHO_TYPE = 0x20,
	FX1_CHO_P01 = 0x22,
	FX1_CHO_P02 = 0x23,
	FX1_CHO_P03 = 0x24,
	FX1_CHO_P04 = 0x25,
	FX1_CHO_P05 = 0x26,
	FX1_CHO_P06 = 0x27,
	FX1_CHO_P07 = 0x28,
	FX1_CHO_P08 = 0x29,
	FX1_CHO_P09 = 0x2A,
	FX1_CHO_P10 = 0x2B,
	FX1_CHO_RETURN = 0x2C,
	FX1_CHO_PAN = 0x2D,
	FX1_CHO_TOREV = 0x2E,
	FX1_CHO_P11 = 0x30,
	FX1_CHO_P12 = 0x31,
	FX1_CHO_P13 = 0x32,
	FX1_CHO_P14 = 0x33,
	FX1_CHO_P15 = 0x34,
	FX1_CHO_P16 = 0x35,

	FX1_VAR_TYPE = 0x40,
	FX1_VAR_P01 = 0x42,
	FX1_VAR_P02 = 0x44,
	FX1_VAR_P03 = 0x46,
	FX1_VAR_P04 = 0x48,
	FX1_VAR_P05 = 0x4A,
	FX1_VAR_P06 = 0x4C,
	FX1_VAR_P07 = 0x4E,
	FX1_VAR_P08 = 0x50,
	FX1_VAR_P09 = 0x52,
	FX1_VAR_P10 = 0x54,
	FX1_VAR_RETURN = 0x56,
	FX1_VAR_PAN = 0x57,
	FX1_VAR_TOREV = 0x58,
	FX1_VAR_TOCHO = 0x59,
	FX1_VAR_CONN = 0x5A,
	FX1_VAR_PART = 0x5B,
	FX1_VAR_MWCTRLDEPTH = 0x5C,
	FX1_VAR_PBCTRLDEPTH = 0x5D,
	FX1_VAR_CATCTRLDEPTH = 0x5E,
	FX1_VAR_AC1CTRLDEPTH = 0x5F,
	FX1_VAR_AC2CTRLDEPTH = 0x60,
	FX1_VAR_P11 = 0x70,
	FX1_VAR_P12 = 0x71,
	FX1_VAR_P13 = 0x72,
	FX1_VAR_P14 = 0x73,
	FX1_VAR_P15 = 0x74,
	FX1_VAR_P16 = 0x35,

	EQ_TYPE = 0x00,
	EQ_GAIN1 = 0x01,
	EQ_FREQ1 = 0x02,
	EQ_Q1 = 0x03,
	EQ_SHAPE1 = 0x04,
	EQ_GAIN2 = 0x05,
	EQ_FREQ2 = 0x06,
	EQ_Q2 = 0x07,
	EQ_GAIN3 = 0x09,
	EQ_FREQ3 = 0x0A,
	EQ_Q3 = 0x0B,
	EQ_GAIN4 = 0x0D,
	EQ_FREQ4 = 0x0E,
	EQ_Q4 = 0x0F,
	EQ_GAIN5 = 0x11,
	EQ_FREQ5 = 0x12,
	EQ_Q5 = 0x13,
	EQ_SHAPE5 = 0x14,

	FX2_INS_TYPE = 0x00,
	FX2_INS_P01 = 0x02,
	FX2_INS_P02 = 0x03,
	FX2_INS_P03 = 0x04,
	FX2_INS_P04 = 0x05,
	FX2_INS_P05 = 0x06,
	FX2_INS_P06 = 0x07,
	FX2_INS_P07 = 0x08,
	FX2_INS_P08 = 0x09,
	FX2_INS_P09 = 0x0A,
	FX2_INS_P10 = 0x0B,
	FX2_INS_PART = 0x0C,
	FX2_INS_MWCTRLDEPTH = 0x0D,
	FX2_INS_PBCTRLDEPTH = 0x0E,
	FX2_INS_CATCTRLDEPTH = 0x0F,
	FX2_INS_AC1CTRLDEPTH = 0x10,
	FX2_INS_AC2CTRLDEPTH = 0x11,
	FX2_INS_P11 = 0x20,
	FX2_INS_P12 = 0x21,
	FX2_INS_P13 = 0x22,
	FX2_INS_P14 = 0x23,
	FX2_INS_P15 = 0x24,
	FX2_INS_P16 = 0x25,

	MP_ELRES = 0x00,
	MP_BANK_MSB = 0x01,
	MP_BANK_LSB = 0x02,
	MP_PRG = 0x03,
	MP_CH = 0x04,
	MP_TUNE = 0x09,
	MP_PARTMODE = 0x7,
	MP_POLYMODE = 0x5,
	MP_KEYON = 0x6,
	MP_TRANSPOSE = 0x8,
	MP_VOL= 0xB,
	MP_VELSENSEDEPTH = 0xC,
	MP_VELSENSEOFFSET = 0xD,
	MP_VELLIMITLO = 0x6D,
	MP_VELLIMITHI = 0x6E,
	MP_PAN = 0xE,
	MP_NOTELIMITLO = 0xF,
	MP_NOTELIMITHI = 0x10,
	MP_DRYLVL = 0x11,
	MP_CHO = 0x12,
	MP_REV = 0x13,
	MP_VAR = 0x14,
	MP_VIBRATE = 0x15,
	MP_VIBDEPTH = 0x16,
	MP_VIBDELAY = 0x17,
	MP_CUTOFF = 0x18,
	MP_RESONANCE = 0x19,
	MP_ATACK = 0x1A,
	MP_DECAY = 0x1B,
	MP_RELEASE = 0x1C,
	MP_MW_PITCH = 0x1D,
	MP_MW_FILTER = 0x1E,
	MP_MW_AMPL = 0x1F,
	MP_MW_LFOPITCHDEPTH = 0x20,
	MP_MW_LFOFILTERDEPTH = 0x21,
	MP_MW_LFOAMPLDEPTH = 0x22,
	MP_PB_PITCH = 0x23,
	MP_PB_FILTER = 0x24,
	MP_PB_AMPL = 0x25,
	MP_PB_LFOPITCHDEPTH = 0x26,
	MP_PB_LFOFILTERDEPTH = 0x27,
	MP_PB_LFOAMPLDEPTH = 0x28,

	MP_RCV_PB = 0x30,
	MP_RCV_CAT = 0x31,
	MP_RCV_PRGCH = 0x32,
	MP_RCV_CC = 0x33,
	MP_RCV_PAT = 0x34,
	MP_RCV_NOTE = 0x35,
	MP_RCV_RPN = 0x36,
	MP_RCV_NRPN = 0x37,
	MP_RCV_MW = 0x38,
	MP_RCV_VOL = 0x39,
	MP_RCV_PAN = 0x3A,
	MP_RCV_EXPR = 0x3B,
	MP_RCV_HOLD1 = 0x3C,
	MP_RCV_PORTAMENTO = 0x3D,
	MP_RCV_SOSTENUTO = 0x3E,
	MP_RCV_SOFTPEDAL = 0x3F,
	MP_RCV_BANKCH = 0x40,

	MP_SCT_C = 0x41,
	MP_SCT_CIS = 0x42,
	MP_SCT_D = 0x43,
	MP_SCT_DIS = 0x44,
	MP_SCT_E = 0x45,
	MP_SCT_F = 0x46,
	MP_SCT_FIS = 0x47,
	MP_SCT_G = 0x48,
	MP_SCT_GIS = 0x49,
	MP_SCT_A = 0x4A,
	MP_SCT_AIS = 0x4B,
	MP_SCT_B = 0x4C,
	
	MP_CAT_PITCH = 0x4D,
	MP_CAT_FILTER = 0x4E,
	MP_CAT_AMPL = 0x4F,
	MP_CAT_LFOPITCHDEPTH = 0x50,
	MP_CAT_LFOFILTERDEPTH = 0x51,
	MP_CAT_LFOAMPLDEPTH = 0x52,
	MP_PAT_PITCH = 0x53,
	MP_PAT_FILTER = 0x54,
	MP_PAT_AMPL = 0x55,
	MP_PAT_LFOPITCHDEPTH = 0x56,
	MP_PAT_LFOFILTERDEPTH = 0x57,
	MP_PAT_LFOAMPLDEPTH = 0x58,
	MP_AC1_PITCH = 0x59,
	MP_AC1_FILTER = 0x5A,
	MP_AC1_AMPL = 0x5B,
	MP_AC1_AMPLCONTROL = 0x5C,
	MP_AC1_LFOPITCHDEPTH = 0x5D,
	MP_AC1_LFOFILTERDEPTH = 0x5E,
	MP_AC1_LFOAMPLDEPTH = 0x5F,
	MP_AC2_PITCH = 0x60,
	MP_AC2_FILTER = 0x61,
	MP_AC2_AMPL = 0x62,
	MP_AC2_AMPLCONTROL = 0x63,
	MP_AC2_LFOPITCHDEPTH = 0x64,
	MP_AC2_LFOFILTERDEPTH = 0x65,
	MP_AC2_LFOAMPLDEPTH = 0x66,

	MP_PORTAMENTO = 0x67,
	MP_PORTAMENTOTIME = 0x68,

	MP_PEG_INITLEVEL = 0x69,
	MP_PEG_ATTACKTIME = 0x6A,
	MP_PEG_RELEASELEVEL = 0x6B,
	MP_PEG_RELEASETIME = 0x6C,

	AD_INPUTGAIN = 0x00,
	AD_BANK_MSB = 0x01,
	AD_BANK_LSB = 0x02,
	AD_PRG = 0x03,
	AD_MIDICHANNEL = 0x04,
	AD_VOL = 0x0B,
	AD_PAN = 0x0E,
	AD_DRYLEVEL = 0x11,
	AD_CHO_SEND = 0x12,
	AD_REV_SEND = 0x13,
	AD_VAR_SEND = 0x14,
	AD_RCV_PRGCH = 0x32,
	AD_RCV_CC = 0x33,
	AD_RCV_VOL = 0x39,
	AD_RCV_PAN = 0x3A,
	AD_RCV_EXPR = 0x3B,
	AD_RCV_BANK = 0x40,
	AD_AC1_CTRL = 0x59,
	AD_AC2_CTRL = 0x60,

	DR_COARSE = 0x00,
	DR_FINE = 0x01,
	DR_VOL = 0x02,
	DR_GRP = 0x03,
	DR_PAN = 0x04,
	DR_REV = 0x05,
	DR_CHO = 0x06,
	DR_VAR = 0x07,
	DR_ASSIGN = 0x08,
	DR_RCVOFF = 0x09,
	DR_RCVON = 0x0A,
	DR_CUTPOFF = 0x0B,
	DR_RESO = 0x0C,
	DR_ATTACK = 0x0D,
	DR_DECAY = 0x0E,
	DR_RELEASE = 0x0F;
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