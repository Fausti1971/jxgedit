package xml;

public interface XMLNodeConstants
{
	String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	String
		XML_DEVICE = "device.xml",
		XML_PARAMETER = "parameter.xml",
		XML_DEFAULTS = "defaults.xml",
		XML_DRUMS = "drums.xml",
		XML_TABLES = "tables.xml",
		XML_TEMPLATES = "templates.xml";

	String
		TAG_WIN = "window",
		ATTR_X = "x",
		ATTR_Y = "y",
		ATTR_W = "w",
		ATTR_H = "h",

		TAG_CONFIG = "config",
		TAG_VERSION = "version",
		TAG_INIT_MESSAGE = "init_message",

		TAG_UI = "ui",
		ATTR_LOOKANDFEEL = "lookAndFeel",
		ATTR_FONT_NAME = "fontname",
		ATTR_FONT_STYLE = "fontstyle",
		ATTR_FONT_SIZE = "fontsize",
		ATTR_MOUSEWHEEL_INVERTED = "mousewheel_inverted",
		ATTR_KNOB_BEHAVIOR = "knob_behavior",
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

		TAG_TEMPLATE="template",
		ATTR_VALUE_TAG = "value_tag",
		ATTR_FRAME = "frame",
		ATTR_KNOB = "knob",
		ATTR_RESET_BUTTONS = "reset_buttons",
		ATTR_PROG_SELECT = "program_selector",
		ATTR_COMBO = "combo",
		ATTR_RADIO = "radio",
		ATTR_FLAGBOX = "flag_box",
		ATTR_CHECKBOX = "checkbox",
		ATTR_SLIDER = "slider",
		ATTR_RANGE = "range_slider",
		ATTR_VELO_ENV = "velocity_envelope",
		ATTR_EQ_ENV = "eq_envelope",
		ATTR_PITCH_ENV = "pitch_envelope",
		ATTR_AMP_ENV = "amplifier_envelope",
		ATTR_TABBED = "tab_frame",
		ATTR_TAB = "tab",
		ATTR_CONSTRAINT = "constraint",
		ATTR_LABEL = "label",
		ATTR_TITLE = "title",
		ATTR_ORIENTATION = "orientation";
}
