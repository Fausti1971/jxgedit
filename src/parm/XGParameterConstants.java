package parm;

import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	public static final XGParameter DUMMY_PARAMETER = new XGParameter("dummy", 0);
	static final String DEF_PARAMETERNAME = "unknown parameter: ";
	static final int  DEF_MIN = 0, DEF_MAX = 127;
	static final XGValueTranslator DEF_TRANSLATOR = XGValueTranslator.normal;

	static enum ValueDataType
	{	MSB, LSB, MSN, LSN
	}

	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;
}
