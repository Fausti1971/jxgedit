package parm;

public interface XGParameterConstants
{	
	static final int
		MP_ELRES = 0x00,
		MP_BANK_MSB = 0x01,
		MP_BANK_LSB = 0x02,
		MP_PRG = 0x03,
		MP_CH = 0x04,
		MP_TUNE = 0x09,

		AD_PRG = 0x00,
	
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

//	static enum Tags{unknown, mp_elRes, mp_bankMsb, mp_bankLsb, mp_prg, mp_ch};//alle XGParameterTags auch als IDs für Controls
/*
	Callback<Integer, String> translateToText = new Callback<Integer, String>()
	{	@Override public String call(Integer v)
		{	return "" + v;
		}
	};

	Callback<Integer, String> translateToTextPlus1 = new Callback<Integer, String>()
	{	@Override public String call(Integer v)
		{	return "" + (v + 1);
		}
	};

	public static Map<Integer, XGParameter> initAllParameters()
	{	Map<Integer, XGParameter> map = new HashMap<>();
		return map;
	}
*/
}
