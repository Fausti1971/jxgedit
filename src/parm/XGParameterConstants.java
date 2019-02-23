package parm;

import memory.Bytes;

public interface XGParameterConstants extends Bytes
{	
	static enum Tags{unknown, mp_elRes, mp_bankMsb, mp_bankLsb, mp_prg, mp_ch};//alle XGParameterTags auch als IDs für Controls
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
