package xml;

public interface XMLNodeConstants
{
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	static final String
		XML_CONFIG = "config.xml",
		XML_STRUCTURE = "structure.xml",
//		XML_MODULE = "module.xml",
//		XML_BULK = "bulk.xml",
//		XML_OPCODE = "opcode.xml",
		XML_PARAMETER = "parameter.xml",
		XML_DEFAULT = "default.xml",
		XML_TABLES = "tables.xml",
		XML_TEMPLATE = "template.xml";

	static final String
		TAG_WIN = "window",
		ATTR_X = "x",
		ATTR_Y = "y",
		ATTR_W = "w",
		ATTR_H = "h",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",

		TAG_FILES = "files",

		TAG_DEVICE = "device",
		ATTR_SYSEXID = "sysexID",
		ATTR_DEFAULTDUMPFILE = "defaultDumpFile",
		ATTR_FILE = "file",

		TAG_MIDI = "midi",
		ATTR_MIDIINPUT = "input",
		ATTR_MIDIOUTPUT = "output",
		ATTR_MIDITIMEOUT = "timeout",
		ATTR_COLOR = "color",

		TAG_MODULES = "modules",

		TAG_MODULE = "module",

		ATTR_ADDRESS = "address",
		ATTR_ADDRESS_LO = "address_lo",
		ATTR_ADDRESS_HI = "address_hi",

		ATTR_START_X = "start_x",
		ATTR_END_X = "end_x",
		ATTR_DEPTH = "depth",
		ATTR_OFFSET = "offset",

		ATTR_INFO1 = "info1",
		ATTR_INFO2 = "info2",
		ATTR_INFO3 = "info3",
//		ATTR_ID = "id",
		ATTR_NAME = "name",

		TAG_BULK = "bulk",
//		ATTR_HI = "hi",
//		ATTR_MID = "mid",
//		ATTR_LO = "lo",

		TAG_OPCODE = "opcode",
		ATTR_DATATYPE = "byteType",

		TAG_PARAMETERTABLES = "parameterTables",
		TAG_PARAMETERTABLE = "parameterTable",
		TAG_PARAMETER = "parameter",

		TAG_DEFAULTTABLES = "defaultTables",
		TAG_DEFAULTTABLE = "defaultTable",
		TAG_DEFAULT = "default",

		ATTR_TYPE = "type",
		ATTR_PARAMETERSELECTOR ="parameterSelector",
		ATTR_PARAMETERS = "parameters",
//		ATTR_PARAMETER = "parameter",
		ATTR_MIN = "min",
		ATTR_MAX = "max",
		ATTR_DEFAULT = "default",
		ATTR_DEFAULTS = "defaults",
		ATTR_DEFAULTSELECTOR = "defaultSelector",
		ATTR_TRANSLATOR = "translator",
		ATTR_TABLEFILTER = "tableFilter",
		ATTR_TABLECATEGORIES = "categories",
		ATTR_FALLBACKMASK = "fallbackMask",
		ATTR_SHORTNAME = "shortName",
		ATTR_CATEGORY = "category",
		ATTR_UNIT = "unit",
		ATTR_ORIGIN = "origin",

		TAG_TEMPLATES = "templates",
		TAG_TEMPLATE = "template",
		TAG_TABLES = "tables",
		TAG_TABLE = "table",
		TAG_ITEM = "item",
		ATTR_VALUE = "value",
		ATTR_SELECTORVALUE = "selectorValue",
//		ATTR_MSB = "msb",
//		ATTR_LSB = "lsb",

		TAG_FRAME = "frame",
		TAG_KNOB = "knob",
		TAG_SLIDER = "slider",
		TAG_RANGE = "range",
		TAG_COMBO = "combo",
		TAG_RADIO = "radio",
		TAG_AEG = "aeg",
		TAG_PEG = "peg",
		TAG_MEQ = "meq",
		TAG_VEG = "veg",
		TAG_FILTER = "filter",
		TAG_BUTTON = "button",
		TAG_SELECTOR = "selector",
		TAG_FLAGBOX = "flagbox",
		TAG_FLAG = "flag",
		TAG_SCALE = "scale",
		TAG_AUTO = "auto",

		ATTR_BOUNDS = "bounds",
		ATTR_GRID_X = "grid_x",
		ATTR_GRID_Y = "grid_y",
		ATTR_ORIGIN_X = "origin_x",
		ATTR_ORIGIN_Y = "origin_y",
		ATTR_ORIENTATION = "orientation";
//		ATTR_GB_FILL = "grid_fill",
//		ATTR_GB_WEIGHT_X = "grid_weight_x",
//		ATTR_GB_WEIGHT_Y = "grid_weight_y",
//		ATTR_GB_ANCHOR = "grid_anchor",
//		ATTR_GB_PAD_X = "grid_pad_x",
//		ATTR_GB_PAD_Y = "grid_pad_y",

//		TAG_ENTRY = "entry",
//
//		TAG_SET = "set";
}
