package xml;

public interface XMLNodeConstants
{
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	static final String
		XML_CONFIG = "config.xml",
		XML_STRUCTURE = "structure.xml",
		XML_PARAMETER = "parameter.xml",
		XML_DEFAULTS = "defaults.xml",
		XML_DRUMS = "drums.xml",
		XML_TABLES = "tables.xml";

	static final String
		TAG_WIN = "window",
		ATTR_X = "x",
		ATTR_Y = "y",
		ATTR_W = "w",
		ATTR_H = "h",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",

		TAG_UI = "ui",
		ATTR_LOOKANDFEEL = "lookAndFeel",
		TAG_FILES = "files",

		TAG_DEVICE = "device",
		ATTR_SYSEXID = "sysexID",

		TAG_MIDI = "midi",
		ATTR_MIDIINPUT = "input",
		ATTR_MIDIOUTPUT = "output",
		ATTR_MIDITIMEOUT = "timeout",
		ATTR_COLOR = "color",

		TAG_MODULE = "module",

		ATTR_ADDRESS = "address",

		TAG_INFO = "info",
		ATTR_REF = "ref",
		ATTR_ID = "id",
		ATTR_NAME = "name",

		TAG_BULK = "bulk",

		TAG_OPCODE = "opcode",
		ATTR_DATATYPE = "byteType",

		TAG_PARAMETERTABLES = "parameterTables",
		TAG_PARAMETERTABLE = "parameterTable",
		TAG_PARAMETER = "parameter",

		TAG_DEFAULTSTABLES = "defaultsTables",
		TAG_DEFAULTSTABLE = "defaultsTable",
		TAG_DRUMS = "drums",
		TAG_KEY = "key",
		TAG_ID = "id",

		ATTR_TYPE = "type",
		ATTR_PARAMETERSELECTOR ="parameterSelector",
		ATTR_PARAMETERS = "parameters",
		ATTR_MIN = "min",
		ATTR_MAX = "max",
		ATTR_DEFAULT = "default",
		ATTR_DEFAULTS = "defaults",
		ATTR_DEFAULTSELECTOR = "defaultSelector",
		ATTR_TABLE = "table",
		ATTR_TABLEFILTER = "tableFilter",
		ATTR_TABLECATEGORIES = "categories",
		ATTR_FALLBACKMASK = "fallbackMask",
		ATTR_SHORTNAME = "shortName",
		ATTR_CATEGORY = "category",
		ATTR_UNIT = "unit",
		ATTR_ORIGIN = "origin",
		ATTR_ACTIONS = "actions",

		TAG_TABLE = "table",
		TAG_ITEM = "item",
		ATTR_VALUE = "value",
		ATTR_SELECTORVALUE = "selectorValue",

		ATTR_ORIENTATION = "orientation";
}
