package xml;

public interface XMLNodeConstants
{
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	static final String
		XML_CONFIG = "config.xml",
		XML_OPCODE = "opcode.xml",
		XML_TABLES = "tables.xml",
//		XML_TYPE = "types.xml",
		XML_PARAMETER = "parameter.xml",
//		XML_TRANSLATION = "translation.xml",
		XML_TEMPLATES = "templates.xml";

	static final String
		TAG_WIN = "window",
		ATTR_X = "x",
		ATTR_Y = "y",
		ATTR_W = "w",
		ATTR_H = "h",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",

		TAG_DEVICE = "device",
		ATTR_SYSEXID = "sysexID",
		ATTR_LASTDUMPFILE = "lastDumpFile",
		ATTR_DEFAULTDUMPFOLDER = "defaultDumpFolder",

		TAG_MIDI = "midi",
		ATTR_MIDIINPUT = "input",
		ATTR_MIDIOUTPUT = "output",
		ATTR_MIDITIMEOUT = "timeout",
		ATTR_COLOR = "color",
//		TAG_BASECOLOR = "base",
//		TAG_FOCUSCOLOR = "focus",

		TAG_MODULES = "modules",

		TAG_MODULE = "module",
		ATTR_ADDRESS = "address",
		ATTR_ID = "id",
		ATTR_NAME = "name",

		TAG_BULK = "bulk",
		ATTR_HI = "hi",
		ATTR_MID = "mid",
		ATTR_LO = "lo",
//		ATTR_SIZE = "size",

		TAG_OPCODE = "opcode",
		ATTR_DATATYPE = "byteType",
//		ATTR_VALUECLASS = "valueClass",
//		ATTR_DEP_VALUES = "dependencyValues",
//		ATTR_DEP_TYPE = "dependencyType",
//		ATTR_DEPENDING = "dependingOf",

		ATTR_PARAMETER_ID = "parameterID",
//		TAG_MUT_PARAMETER = "mutable_parameter",
		ATTR_MASTER ="master",
		ATTR_INDEX = "index",
		ATTR_MIN = "min",
		ATTR_MAX = "max",
		ATTR_TRANSLATOR = "translator",
		ATTR_TRANSLATIONMAP = "translationMap",
		ATTR_TRANSLATIONMAPFILTER = "translationMapFilter",
		ATTR_LONGNAME = "longName",
		ATTR_SHORTNAME = "shortName",
		ATTR_UNIT = "unit",

		TAG_TEMPLATES = "templates",
		TAG_TEMPLATE = "template",
		TAG_TABLES = "tables",
		TAG_TABLE = "table",
		TAG_ITEM = "item",
//		ATTR_TYPE = "type",
		ATTR_VALUE = "value",

		TAG_FRAME = "frame",
		TAG_KNOB = "knob",
		TAG_SLIDER = "slider",
		TAG_COMBO = "combo",
		TAG_RADIO = "radio",
		TAG_ENVELOPE = "envelope",
		TAG_ENVPOINT = "env_point",
		TAG_AUTO = "auto",
		ATTR_AXIS = "axis",
		ATTR_ORIGIN = "origin",
//		TAG_MAPS = "maps",
//		TAG_MAP = "map",
		TAG_ENTRY = "entry",
//		TAG_KEY = "key",

//		TAG_SETS = "sets",
		TAG_SET = "set";
}
