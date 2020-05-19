package parm;

import application.Rest;
import application.XGLoggable;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public interface XGValueTranslator extends XGLoggable, XGParameterConstants
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
		xml,
		panorama,
		effect_part
	}

	static XGValueTranslator getTranslator(XGDevice dev, XMLNode n)
	{	String name = n.getStringAttribute(ATTR_TRANSLATOR);
		if(name == null) return normal;
		try
		{	XGTranslatorTag t = XGTranslatorTag.valueOf(name);
			{	switch(t)
				{	case empty:			return empty;
					case normal:		return normal;
					case add1:			return add1;
					case div10:			return div10;
					case sub64:			return sub64;
					case sub128Div10:	return sub128Div10;
					case sub1024Div10:	return sub1024Div10;
					case table:			return new XGTableTranslator(dev, n);
					case xml:			return xml;
					case panorama:		return panorama;
					case effect_part:	return fx_part;
					default:			return normal;// vielleicht wahlweise auch percent?
				}
			}
		}
		catch(IllegalArgumentException | XGTableNotFoundException e)
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
		}
	};

	static XGValueTranslator normal = new XGValueTranslator()
	{	@Override public String translate(XGValue v)
		{	return String.valueOf(v.getContent()) + v.getParameter().getUnit();
		}
		@Override public int translate(XGValue v, String s)
		{	return Rest.parseIntOrDefault(s.trim(), v.getContent());
		}
		@Override public XGTable getTable(XGValue v)
		{	XGParameter p = v.getParameter();
			XGTable t = new XGTable(p.getTag());
			for(int i = p.getMinValue(); i <= p.getMaxValue(); i++) t.put(i, new XGTableEntry(i, String.valueOf(i)));
			return t;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
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
		@Override public XGTable getTable(XGValue v)
		{
			// TODO Auto-generated method stub
			return null;
		}
	};

	static XGValueTranslator panorama = new XGValueTranslator()
	{	@Override public int translate(XGValue v, String s)
		{	return 0;
		}
		@Override public String translate(XGValue v)
		{	Integer i = v.getContent();
			if(i == 0) return "Rnd";
			if(i < 64) return "L" + Math.abs(i - 64);
			if(i > 64) return "R" + Math.abs(i - 64);
			else return "C";
		}
		@Override public XGTable getTable(XGValue v)
		{	return null;
		}
	};

	static XGValueTranslator fx_part = new XGValueTranslator()
	{	@Override public int translate(XGValue v, String s)
		{	return 0;
		}
		@Override public String translate(XGValue v)
		{	Integer i = v.getContent();
			if(i == 127) return "OFF";
			if(i < 64) return "MP" + Math.abs(i + 1);
			else return "AD" + Math.abs(i - 63);
		}
		@Override public XGTable getTable(XGValue v)
		{	return null;
		}
	};

/***************************************************************************************************************************/

	XGTable getTable(XGValue v);
	String translate(XGValue v);
	int translate(XGValue v, String s);

}
