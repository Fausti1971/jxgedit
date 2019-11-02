package value;

//TODO determiniere ValueType und differenziere Methoden

public interface XGValueTranslator
{	String translate(XGValue v);

	static XGValueTranslator getTranslator(String name)
	{	if(name == null) return translateToText;
		switch(name)
		{	case "translateNot":		return translateNot;
			case "translateToText":		return translateToText;
			case "translateToTextPlus1":return translateToTextPlus1;
			case "translateDiv10":		return translateDiv10;
			case "translateSub128Div10":return translateSub128Div10;
			case "translateMap":		return translateMap;
			default:					return translateToText;
		}
	}
	
	static XGValueTranslator translateNot = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	return "";}
	};

	static XGValueTranslator translateToText = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	return "" + v.getContent();}
	};

	static XGValueTranslator translateToTextPlus1 = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	return "" + ((Integer)v.getContent()) + 1;}
	};

	static XGValueTranslator translateDiv10 = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	return "" + ((float)v.getContent())/10;}
	};

	static XGValueTranslator translateSub128Div10 = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	float f = (int)v.getContent();
			return Float.toString((f - 128) / 10);
		}
	};

	static XGValueTranslator translateMap = new XGValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	return v.getParameter().getTranslationMap().getValue((int)v.getContent());}
			catch(NullPointerException e)
			{	e.printStackTrace();
				return "no value";}
		}
	};
}
