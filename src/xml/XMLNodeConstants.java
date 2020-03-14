package xml;

public interface XMLNodeConstants
{
	static final String
		XML_CONFIG = "config.xml",
		XML_OPCODE = "opcode.xml",
		XML_TYPE = "type.xml",
		XML_PARAMETER = "parameter.xml",
		XML_TRANSLATION = "translation.xml",
		XML_TEMPLATE = "template.xml";

	static String
		TAG_WIN = "window",
		TAG_WINX = "window_x",
		TAG_WINY = "window_y",
		TAG_WINW = "window_w",
		TAG_WINH = "window_h",

		TAG_COLOR = "device_color",
//		TAG_BASECOLOR = "base",
//		TAG_FOCUSCOLOR = "focus",

		TAG_DEVICE_NAME = "device_name",
		TAG_SYSEXID = "device_sysexID",
		TAG_LASTDUMPFILE = "device_lastDumpFile",
		TAG_DEFAULTDUMPFOLDER = "device_defaultDumpFolder",

		ATTR_SELECTED = "selected",
		ATTR_NAME = "name",
		ATTR_ID = "ID",

		TAG_MAPS = "maps",
		TAG_MAP = "map",
		TAG_ENTRY = "entry",
		TAG_KEY = "key",
		TAG_VALUE = "value",

		TAG_SETS = "sets",
		TAG_SET = "set",
		TAG_ITEM = "item",
	
		TAG_CONFIG = "config",
		TAG_VERSION = "version",
		TAG_DEVICE = "device";

}
