package obj;

public interface XGObjectConstants
{	public static String XML_FILE = "rsc/XGObjects.xml";

	public static String
		TAG_OBJECT = "object",
		TAG_NAME = "name",
		TAG_ADRESS = "adress",
		TAG_HI = "hi",
		TAG_MID = "mid",
		TAG_LO = "lo",
		TAG_DUMPSEQ = "dumpSequence",
		TAG_PARAMETERMAP = "parameterMap";

	public static final int
		MASTER = 0x00,
		INFO = 0x01,
		SYSFX = 0x02,
		INSFX = 0x03,
		INSFXVL = 0x04,
		DTEXT = 0x06,
		DBITMAP = 0x07,
		MULTI = 0x08,
		MULTIADD = 0x0A,
		MULTIVL = 0x09,
		ADPART = 0x10,
		DRUM1 = 0x30,
		DRUM2 = 0x31,
		DRUM3 = 0x32,
		DRUM4 = 0x33;
}
