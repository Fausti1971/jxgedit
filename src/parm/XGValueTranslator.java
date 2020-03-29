package parm;

import adress.InvalidXGAddressException;
import parm.XGTranslationConstants.XGTranslatorTag;
import value.XGValue;

//TODO determiniere ValueType und differenziere Methoden

public interface XGValueTranslator
{	String translate(XGValue v);

	static final String NOVALUE = "no value";

	static XGValueTranslator getTranslator(XGTranslatorTag t)
	{	switch(t)
		{	case translateNot:			return translateNot;
			case translateToText:		return translateToText;
			case translateToTextAdd1:	return translateToTextPlus1;
			case translateDiv10:		return translateDiv10;
			case translateSub128Div10:	return translateSub128Div10;
			case translateMap:			return translateMap;
			case translateXML:			return translateXML;
			default:					return translateToText;
		}
	}

	static XGValueTranslator getTranslator(String name)
	{	try
		{	return getTranslator(XGTranslatorTag.valueOf(name));
		}
		catch(IllegalArgumentException e)
		{	return translateToText;
		}
	}
	
	static XGValueTranslator translateNot = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return "";
		}
	};

	static XGValueTranslator translateToText = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	return v.getContent().toString();
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
				return NOVALUE;
			}
		}
	};

	static XGValueTranslator translateToTextPlus1 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	return String.valueOf(((Integer)v.getContent()) + 1);
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
				return NOVALUE;
			}
		}
	};

	static XGValueTranslator translateDiv10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	return String.valueOf(((float)v.getContent())/10);
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
				return NOVALUE;
			}
		}
	};

	static XGValueTranslator translateSub128Div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	float f;
			try
			{	f = (int)v.getContent();
				return Float.toString((f - 128) / 10);
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
				return NOVALUE;
			}
		}
	};

	static XGValueTranslator translateXML = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return "no XML-value";
		}
	};

	static XGValueTranslator translateMap = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	return v.getSource().getDevice().getTranslations().get(v.getOpcode().getParameter(0).getTranslationMapName()).getValue((int)v.getContent());//TODO:
			}
			catch(NullPointerException | InvalidXGAddressException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};
}
