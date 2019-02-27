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

		AD_PRG = 0x00;

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
