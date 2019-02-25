package parm;

import java.util.function.Function;

public interface ValueTranslation extends Function<XGParameter, String>
{
	static String translateToText(XGParameter p)
	{	return "" + p.getValue();
	}

	static String translateToTextPlus1(XGParameter p)
	{	return "" + (p.getValue() + 1);
	}

	static String translateMap(XGParameter p)
	{	try
		{	return p.getTranslationMap().get(p.getValue());
		}
		catch(NullPointerException e)
		{	return "no value";
		}
	}
}
