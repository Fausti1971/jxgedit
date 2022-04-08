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
		TABLE_SUB8k = "sub8k",
		TABLE_PANORAMA = "panorama",
		TABLE_DEGREES = "degrees",
		TABLE_PERCENT = "percent",
		TABLE_KEYS = "keys",
		TABLE_FX_PARTS = "fx_parts",
		TABLE_PARTMODE = "mp_partmode",
		TABLE_PROGRAM = "mp_program",
		TABLE_GAIN = "eq_gain",
		TABLE_NONE = "none",
		DEF_TABLENAME = TABLE_NORMAL;

	enum XACTION{change,dump,none,change_program,change_partmode}

	String NO_PARAMETERNAME = "no parameter";
	int  UNLIMITED = -1;
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
