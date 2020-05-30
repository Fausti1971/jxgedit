package parm;

import application.XGLoggable;
import value.XGValue;

public interface XGValueTranslator extends XGLoggable, XGParameterConstants
{
	static final String NOVALUE = "no value";



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

//	XGTable getTable(XGValue v);
//	String translate(XGValue v);
//	int translate(XGValue v, String s);

}
