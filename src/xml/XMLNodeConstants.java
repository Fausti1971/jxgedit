package xml;

public interface XMLNodeConstants
{
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	static final String
		XML_CONFIG = "config.xml",
		XML_OPCODE = "opcodes.xml",
		XML_TYPE = "types.xml",
		XML_PARAMETER = "parameter.xml",
		XML_TRANSLATION = "translations.xml",
		XML_TEMPLATE = "templates.xml";

	static final String
		TAG_WIN = "window",
		TAG_WINX = "window_x",
		TAG_WINY = "window_y",
		TAG_WINW = "window_w",
		TAG_WINH = "window_h",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",
		TAG_DEVICE = "device",

		TAG_COLOR = "device_color",
//		TAG_BASECOLOR = "base",
//		TAG_FOCUSCOLOR = "focus",

		TAG_DEVICE_NAME = "device_name",
		TAG_SYSEXID = "device_sysexID",
		TAG_LASTDUMPFILE = "device_lastDumpFile",
		TAG_DEFAULTDUMPFOLDER = "device_defaultDumpFolder",

		TAG_MODULES = "modules",

		TAG_MODULE = "module",
		ATTR_ID = "id",
		ATTR_NAME = "name",

		TAG_BULK = "bulk",
		ATTR_HI = "hi",
		ATTR_MID = "mid",
		ATTR_LO = "lo",
//		ATTR_SIZE = "size",

		TAG_OPCODE = "opcode",
		ATTR_DATATYPE = "byteType",
		ATTR_VALUECLASS = "valueClass",
		ATTR_DEP_VALUES = "dependencyValues",
		ATTR_DEP_TYPE = "dependencyType",
		ATTR_DEPENDING = "dependingOf",

		TAG_PARAMETER = "parameter",
		ATTR_MIN = "min",
		ATTR_MAX = "max",
		ATTR_TRANSLATOR = "translator",
		ATTR_TRANSLATIONMAP = "translationMap",
		ATTR_LONGNAME = "longName",
		ATTR_SHORTNAME = "shortName",

		TAG_MAPS = "maps",
		TAG_MAP = "map",
		TAG_ENTRY = "entry",
		TAG_KEY = "key",
		TAG_VALUE = "value",

		TAG_SETS = "sets",
		TAG_SET = "set",
		TAG_ITEM = "item";
}
