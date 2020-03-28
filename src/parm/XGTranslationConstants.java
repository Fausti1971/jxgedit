package parm;

import xml.XMLNodeConstants;

public interface XGTranslationConstants extends XMLNodeConstants
{
	static enum XGTranslatorTag
	{	translateNot,
		translateToText,
		translateToTextAdd1,
		translateDiv10,
		translateSub128Div10,
		translateMap,
		translatePercent,
		translateXML
	}
}
