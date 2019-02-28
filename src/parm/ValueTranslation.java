package parm;

import obj.XGObject;

public interface ValueTranslation// extends BiFunction<XGObject, XGParameter, String>
{	static String translateNot(XGObject o, XGParameter p)
	{	return "";}

	static String translateToText(XGObject o, XGParameter p)
	{	return "" + o.getValue(p.getOpcode().getOffset());}

	static String translateToTextPlus1(XGObject o, XGParameter p)
	{	return "" + (o.getValue(p.getOpcode().getOffset()) + 1);}

	static String translateSub128Div10(XGObject o, XGParameter p)
	{	return Float.toString(((float)o.getValue(p.getOpcode().getOffset()) - 128) / 10);}

	static String translateMap(XGObject o, XGParameter p)
	{	try
		{	return p.getTranslationMap().get(o.getValue(p.getOpcode().getOffset()));
		}
		catch(NullPointerException e)
		{	return "no value";
		}
	}
}
