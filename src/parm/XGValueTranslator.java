package parm;

import application.Rest;
import value.XGValue;

public interface XGValueTranslator
{
	static final String SPACE = " ";
	static final String NOVALUE = "no value";

	static enum XGTranslatorTag
	{	empty,
		normal,
		add1,
		div10,
		sub64,
		sub128Div10,
		sub1024Div10,
		map,
		percent,
		xml
	}

	static XGValueTranslator getTranslator(XGTranslatorTag t)
	{	switch(t)
		{	case empty:			return empty;
			case normal:		return normal;
			case add1:			return add1;
			case div10:			return div10;
			case sub64:			return sub64;
			case sub128Div10:	return sub128Div10;
			case sub1024Div10:	return sub1024Div10;
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
		{	return "" + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{	return 0;
		}
	};

	static XGValueTranslator normal = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(v.getContent()) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{	return Rest.parseIntOrDefault(s.trim(), v.getContent());
		}
	};

	static XGValueTranslator add1 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf((v.getContent()) + 1) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(((float)v.getContent())/10) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator sub64 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return (v.getContent() - 64) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator sub1024Div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	float f = v.getContent() - 1024;
			return Float.toString(f / 10) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator sub128Div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	float f = v.getContent() - 128;
			return Float.toString(f / 10) + SPACE + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator xml = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return "no XML-value";
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator map = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	try
			{	XGParameter p = v.getParameter();
				return v.getSource().getDevice().getTables().get(p.getTranslationMapName()).get(v.getContent()) + SPACE + p.getUnit();
			}
			catch(NullPointerException e)
			{	e.printStackTrace();
				return "no value";
			}
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

/***************************************************************************************************************************/

	String translate(XGValue v);
	int translate(XGValue v, String s);

}
