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
		{	try
			{	return "" + v.getNumberValue();
			}
			catch(WrongXGValueTypeException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};

	static ValueTranslator translateToTextPlus1 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	return "" + (v.getNumberValue() + 1);
			}
			catch(WrongXGValueTypeException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};

	static ValueTranslator translateDiv10 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	return "" + ((float)v.getNumberValue())/10;
			}
			catch(WrongXGValueTypeException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};

	static ValueTranslator translateSub128Div10 = new ValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	float f = (int)v.getNumberValue();
				return Float.toString((f - 128) / 10);
			}
			catch(WrongXGValueTypeException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};

	static ValueTranslator translateMap = new ValueTranslator()
	{	public String translate(XGValue v)
		{	try
			{	return v.getParameter().getTranslationMap().get((int)v.getNumberValue());}
			catch(NullPointerException | WrongXGValueTypeException e)
			{	e.printStackTrace();
				return "no value";}
		}
	};
}
