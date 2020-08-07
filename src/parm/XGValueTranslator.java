package parm;

import application.XGLoggable;
import value.XGValue;

public interface XGValueTranslator extends XGLoggable, XGParameterConstants
{
	static final String NOVALUE = "no value";

//	static XGValueTranslator fx_part = new XGValueTranslator()//TODO: versuche fx-part dynamisch zu machen damit zur Laufzeit die Partnamen angezeigt und ausgewählt werden können
//	{	@Override public int translate(XGValue v, String s)
//		{	return 0;
//		}
//		@Override public String translate(XGValue v)
//		{	Integer i = v.getContent();
//			if(i == 127) return "OFF";
//			if(i < 64) return "MP" + Math.abs(i + 1);
//			else return "AD" + Math.abs(i - 63);
//		}
//	};

/***************************************************************************************************************************/

//	XGTable getTable(XGValue v);
//	String translate(XGValue v);
//	int translate(XGValue v, String s);

}
