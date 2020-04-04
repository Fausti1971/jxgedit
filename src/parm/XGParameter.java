package parm;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import application.ConfigurationConstants;
import application.Rest;
import opcode.XGOpcode;
import parm.XGTranslationConstants.XGTranslatorTag;
import xml.XMLNode;

public class XGParameter implements ConfigurationConstants, XGParameterConstants
{	private static final Logger log = Logger.getAnonymousLogger();

	public static Map<Integer, XGParameter> init(XGOpcode op, XMLNode n)
	{	Map<Integer, XGParameter> map = new HashMap<>();
		for(XMLNode x : n.getChildNodes())
			if(x.getTag().equals(TAG_PARAMETER))
			{	XGParameter p = new XGParameter(x);
				for(String o : Rest.splitCSV(x.getStringAttribute(ATTR_DEP_VALUES)))
				{	map.put(Integer.parseInt(o), p);
				}
			}
		return map;
	}

/*****************************************************************************************************/

	private final String longName, shortName;
	private final int minValue, maxValue;
	private final XGValueTranslator valueTranslator;
	private final String translationMapName;

	public XGParameter(String tag)
	{	this(DEF_PARAMETERNAME + tag, "unknw", DEF_MIN, DEF_MAX,XGTranslatorTag.translateNot, "");
	}

	public XGParameter(String longN, String shortN, int min, int max, XGTranslatorTag t, String translMap)
	{
		this.longName = longN;
		this.shortName = shortN;
		this.minValue = min;
		this.maxValue = max;
		this.valueTranslator = XGValueTranslator.getTranslator(t);
		this.translationMapName = translMap;
	}

	public XGParameter(XMLNode n)
	{
		this.minValue = n.getIntegerAttribute(ATTR_MIN, DEF_MIN);
		this.maxValue = n.getIntegerAttribute(ATTR_MAX, DEF_MAX);
		this.longName = n.getStringAttribute(ATTR_LONGNAME);
		this.shortName = n.getStringAttribute(ATTR_SHORTNAME);
		this.valueTranslator = XGValueTranslator.getTranslator(n.getStringAttribute(ATTR_TRANSLATOR));
		this.translationMapName = n.getStringAttribute(ATTR_TRANSLATIONMAP);
//		log.info("parameter initialized: " + this);
	}

	public int getMinValue()
	{	return minValue;
	}

	public int getMaxValue()
	{	return maxValue;
	}

	public boolean isLimitizable()
	{	return !this.valueTranslator.equals(XGValueTranslator.translateMap);
	}

	public String getShortName()
	{	return shortName;
	}

	public String getLongName()
	{	return this.longName;
	}

	public XGValueTranslator getValueTranslator()
	{	return valueTranslator;
	}

	public String getTranslationMapName()
	{	return translationMapName;
	}

	@Override public String toString()
	{	return this.longName;
	}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " " + this.toString();
	}
}
