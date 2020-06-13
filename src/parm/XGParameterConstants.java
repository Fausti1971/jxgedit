package parm;

import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	static final String
		TABLE_NORMAL = "normal",
		TABLE_ADD1 = "add1",
		TABLE_DIV10 = "div10",
		TABLE_SUB64 = "sub64",
		TABLE_SUB128DIV10 = "sub128div10",
		TABLE_SUB1024DIV10 = "sub1024div10",
		TABLE_PANORAMA = "panorama",
		TABLE_DEGREES = "degrees",
		DEF_TABLENAME = TABLE_NORMAL;

//	public static final XGParameter DUMMY_PARAMETER = new XGParameter("n/a", 0);
	static final String DEF_PARAMETERNAME = "unknown parameter: ";
	static final int  DEF_MIN = 0, DEF_MAX = 127;
	static final int DEF_SELECTORVALUE = -1;

	static enum ValueDataType
	{	MSB, LSB, MSN, LSN
	}

	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;
}
