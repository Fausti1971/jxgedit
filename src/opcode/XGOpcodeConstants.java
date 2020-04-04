package opcode;

import xml.XMLNodeConstants;

public interface XGOpcodeConstants extends XMLNodeConstants
{
	static enum ValueDataClass
	{	Integer, String, Image
	};

	static enum ValueDataType
	{	MSB, LSB, MSN, LSN
	}

	static final String DEF_OPCODENAME = "unknown opcode ";
	static final ValueDataClass DEF_VALUECLASS = ValueDataClass.Integer;
	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;
}
