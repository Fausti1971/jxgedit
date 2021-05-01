package parm;

import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	String
		TABLE_NORMAL = "normal",
		TABLE_ADD1 = "add1",
		TABLE_DIV10 = "div10",
		TABLE_SUB64 = "sub64",
		TABLE_SUB128DIV10 = "sub128div10",
		TABLE_SUB1024DIV10 = "sub1024div10",
		TABLE_PANORAMA = "panorama",
		TABLE_DEGREES = "degrees",
		TABLE_NONE = "none",
		DEF_TABLENAME = TABLE_NORMAL;

	enum XACTION{change,dump,none,switch_program};

	String MUTABLE = "mutable", IMMUTABLE = "immutable";
	String DEF_PARAMETERNAME = "unknown parameter: ";
	String NO_PARAMETERNAME = "no parameter";
	int  DEF_MIN = 0, DEF_MAX = 127, DEF_ORIGIN = 0;
/**
 * ein Pseudo Selector-Value, damit auch der Parameter eines immutable-Opcodes im Set gespeichert werden kann (ebenso defaults)
 */
	int DEF_SELECTORVALUE = -1;
	int NO_PARAMETERVALUE = -1;

	XGParameter NO_PARAMETER = new XGParameter(NO_PARAMETERNAME, NO_PARAMETERVALUE);

	enum ValueDataType
	{	MSB, LSB, MSN, LSN
	}

	ValueDataType DEF_DATATYPE = ValueDataType.LSB;
}
