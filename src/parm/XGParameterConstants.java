package parm;

public interface XGParameterConstants
{	
	static final Opcode
		MP_ELRES = new Opcode(0x00),
		MP_BANK_MSB = new Opcode(0x01),
		MP_BANK_LSB = new Opcode(0x02),
		MP_PRG = new Opcode(0x03),
		MP_CH = new Opcode(0x04),

		AD_PRG = new Opcode(0x00);

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
