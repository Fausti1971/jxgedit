package parm;

import application.Rest;
import application.XGLoggable;
import value.XGValue;

public interface XGValueTranslator extends XGLoggable
{
	static final String NOVALUE = "no value";

	static enum XGTranslatorTag
	{	empty,
		normal,
		add1,
		div10,
		sub64,
		sub128Div10,
		sub1024Div10,
		table,
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
			case table:			return table;
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
		{	return v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{	return 0;
		}
	};

	static XGValueTranslator normal = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(v.getContent()) + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{	return Rest.parseIntOrDefault(s.trim(), v.getContent());
		}
	};

	static XGValueTranslator add1 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf((v.getContent()) + 1) + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator div10 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(((float)v.getContent())/10) + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static XGValueTranslator sub64 = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return (v.getContent() - 64) + v.getParameter().getUnit();
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
			return Float.toString(f / 10) + v.getParameter().getUnit();
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
			return Float.toString(f / 10) + v.getParameter().getUnit();
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

	static XGValueTranslator table = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	XGParameter p = v.getParameter();
			XGTable t = p.getTranslationTable();
			try
			{	return t.get(v.getContent()).getName() + p.getUnit();
			}
			catch(NullPointerException e)
			{	log.info(e.getMessage() + " param=" + p + " table=" + t);
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
