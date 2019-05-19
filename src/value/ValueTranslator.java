package value;

//TODO determiniere ValueType und differenziere Methoden

public interface ValueTranslator
{	String translate(XGValue v);

	static ValueTranslator getTranslator(String name)
	{	switch(name)
		{	case "translateNot":		return translateNot;
			case "translateToText":		return translateToText;
			case "translateToTextPlus1":return translateToTextPlus1;
			case "translateDiv10":		return translateDiv10;
			case "translateSub128Div10":return translateSub128Div10;
			case "translateMap":		return translateMap;
			default:					return translateToText;
		}
	}
	
	static ValueTranslator translateNot = new ValueTranslator()
	{	public String translate(XGValue v)
		{	return "";}
	};

	static ValueTranslator translateToText = new ValueTranslator()
	{	public String translate(XGValue v)
		{	return v.getValue().toString();}
	};

	static ValueTranslator translateToTextPlus1 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	return "" + ((int)v.getValue() + 1);}
	};

	static ValueTranslator translateDiv10 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	return "" + ((float)v.getValue())/10;}
	};

	static ValueTranslator translateSub128Div10 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	float f = (int)v.getValue();
			return Float.toString((f - 128) / 10);
		}
	};

	static ValueTranslator translateMap = new ValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	return v.getParameter().getTranslationMap().get((int)v.getValue());}
			catch(NullPointerException e)
			{	return "no value";}
		}
	};
}
