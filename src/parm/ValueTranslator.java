package parm;

import java.util.HashMap;
import java.util.Map;

//TODO determiniere ValueType und differenziere Methoden

public interface ValueTranslator
{	String translate(XGValue v);
	void addToMap();

	static ValueTranslator getTranslator(String name)
	{	return translators.getOrDefault(name, translateToText);}

	static Map<String, ValueTranslator> translators = new HashMap<>();

	static ValueTranslator translateNot = new ValueTranslator()
	{	public String translate(XGValue v)
		{	return "";}
		public void addToMap()
		{	translators.put("translateNot", this);}
	};

	static ValueTranslator translateToText = new ValueTranslator()
	{	public void addToMap()
		{	translators.put("translateToText", this);}
		public String translate(XGValue v)
		{	return v.getValue().toString();}
	};

	static ValueTranslator translateToTextPlus1 = new ValueTranslator()
	{	public void addToMap()
		{	translators.put("translateToTextPlus1", this);}
		public String translate(XGValue v)
		{	return "" + ((int)v.getValue() + 1);}
	};

	static ValueTranslator translateDiv10 = new ValueTranslator()
	{	public void addToMap()
		{	translators.put("translateDiv10", this);}
		public String translate(XGValue v)
		{	return "" + ((float)v.getValue())/10;}
	};

	static ValueTranslator translateSub128Div10 = new ValueTranslator()
	{	public void addToMap()
		{	translators.put("translateSub128Div10", this);}
		public String translate(XGValue v)
		{	return Float.toString(((float)v.getValue() - 128) / 10);}
	};

	static ValueTranslator translateMap = new ValueTranslator()
	{	public void addToMap()
		{	translators.put("translateMap", this);}
		public String translate(XGValue v)
		{	try
			{	return v.getParameter().getTranslationMap().get((int)v.getValue());}
			catch(NullPointerException e)
			{	return "no value";}
		}
	};
}
