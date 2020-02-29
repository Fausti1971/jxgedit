package parm;

import xml.XMLNodeConstants;

public interface XGTranslationConstants extends XMLNodeConstants
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
}
