package opcode;

public interface XGOpcodeConstants
{	static final String
		TAG_MAP = "map",
		TAG_OPCODE = "opcode",
		TAG_NAME = "name",
		TAG_ADRESS = "adress",
		TAG_BYTECOUNT = "byteCount",
		TAG_DATATYPE = "byteType",
		TAG_VALUECLASS = "valueClass";

	static enum ValueDataClass{Integer, String, Image};
	static enum DataType{MIDIBYTE, NIBBLE}

	static final String DEF_OPCODENAME = "unknown opcode: ";
	static final ValueDataClass DEF_VALUECLASS = ValueDataClass.Integer;
	static final int DEF_BYTECOUNT = 1;
	static final DataType DEF_DATATYPE = DataType.MIDIBYTE;
}
