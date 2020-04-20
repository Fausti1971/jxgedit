package parm;

import value.XGValue;

//TODO determiniere ValueType und differenziere Methoden

public interface XGValueTranslator
{
	static enum XGTranslatorTag
	{	empty,
		normal,
		add1,
		div10,
		sub128Div10,
		map,
		percent,
		xml
	}

	static final String NOVALUE = "no value";

	static XGValueTranslator getTranslator(XGTranslatorTag t)
	{	switch(t)
		{	case empty:			return empty;
			case normal:		return normal;
			case add1:			return add1;
			case div10:			return div10;
			case sub128Div10:	return sub128Div10;
			case map:			return map;
			case xml:			return xml;
			default:			return normal;
		}
	}

	static XGValueTranslator getTranslator(String name)
	{	if(name == null) return normal;
		try
		{	return getTranslator(XGTranslatorTag.valueOf(name));
		}
		catch(IllegalArgumentException e)
		{	return normal;
		}
	}
	
	static XGValueTranslator empty = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return "";
		}
	};

	static XGValueTranslator normal = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(v.getContent()) + v.getParameter().getUnit();
		}
	};

	static XGValueTranslator add1 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(((Integer)v.getContent()) + 1) + v.getParameter().getUnit();
		}
	};

	static XGValueTranslator div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(((float)v.getContent())/10) + v.getParameter().getUnit();
		}
	};

	static XGValueTranslator sub128Div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	float f;
			f = (int)v.getContent();
				return Float.toString((f - 128) / 10) + v.getParameter().getUnit();
		}
	};

	static XGValueTranslator xml = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return "no XML-value";
		}
	};

	static XGValueTranslator map = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	XGParameter p = v.getParameter();
				return v.getSource().getDevice().getTables().get(p.getTranslationMapName()).get(v.getContent()) + p.getUnit();
			}
			catch(NullPointerException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
	};

/***************************************************************************************************************************/

	String translate(XGValue v);

}
