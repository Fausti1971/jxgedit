package parm;

public interface XGTranslationConstants
{
	static enum XGTranslator
	{	translateNot,
		translateToText,
		translateToTextPlus1,
		translateDiv10,
		translateSub128Div10,
		translateMap,
		translateXML
	}

	static final String
		TAG_MAPS = "maps",
		TAG_MAP = "map",
		TAG_ENTRY = "entry",
		TAG_KEY = "key",
		TAG_VALUE = "value",
		ATTR_NAME = "name";
}
