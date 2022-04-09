package xml;

import static application.JXG.APPNAME;public interface XMLNodeConstants
{
	String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	String
		XML_CONFIG = APPNAME + ".xml",
		XML_STRUCTURE = "structure.xml",
		XML_PARAMETER = "parameter.xml",
		XML_DEFAULTS = "defaults.xml",
		XML_DRUMS = "drums.xml",
		XML_TABLES = "tables.xml";

	String
		TAG_WIN = "window",
		ATTR_X = "x",
		ATTR_Y = "y",
		ATTR_W = "w",
		ATTR_H = "h",
		ATTR_GRID_WIDTH = "grid_width",
		ATTR_GRID_HEIGHT = "grid_height",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",

		TAG_UI = "ui",
		ATTR_LOOKANDFEEL = "lookAndFeel",
		ATTR_FONT_NAME = "fontname",
		ATTR_FONT_STYLE = "fontstyle",
		ATTR_FONT_SIZE = "fontsize",
		ATTR_MOUSEWHEEL_INVERTED = "mousewheel_inverted",
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

		TAG_PARAMETERTABLE = "parameterTable",
		TAG_PARAMETER = "parameter",

		TAG_DEFAULTSTABLE = "defaultsTable",

		TAG_KEY = "key",
		TAG_ID = "id",

		ATTR_TYPE = "type",
		ATTR_IMMUTABLE = "immutable",
		ATTR_MUTABLEPAR = "mutablePar",
		ATTR_MUTABLEDEF = "mutableDef",
		ATTR_MUTABLE = "mutable",
		ATTR_PARAMETERSELECTOR ="parameterSelector",
		ATTR_PARAMETERTABLE = "parameterTable",
		ATTR_MIN = "min",
		ATTR_MAX = "max",
		ATTR_DEFAULT = "default",
		ATTR_DEFAULTSTABLE = "defaultsTable",
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
