package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import application.ConfigurationConstants;
import application.Rest;
import device.XGDevice;
import tag.XGTagable;
import tag.XGTagableSet;
import xml.XMLNode;

public class XGParameter implements ConfigurationConstants, XGParameterConstants, XGTagable
{	private static final Logger log = Logger.getAnonymousLogger();

	public static XGTagableSet<XGParameter> init(XGDevice dev)
	{	XGTagableSet<XGParameter> set = new XGTagableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	return set;
		}
		XMLNode xml = XMLNode.parse(file);
		for(XMLNode x : xml.getChildren())
			if(x.getTag().equals(TAG_PARAMETER))
				set.add(new XGParameter(dev, x));
		
		log.info(set.size() + " parameters initialized");
		return set;
	}
/*
	public static XGParameter getParameter(XGDevice dev, XGValue val)
	{	XGTagableSet<XGParameter> set;
		XGParameter p;

		if(STORAGE.containsKey(dev)) set = STORAGE.get(dev);
		else set = STORAGE.get(XGDevice.getDefaultDevice());

		if(set.containsKey(val.getTag())) p = set.get(val.getTag());
		else return new XGParameter(dev, val.getTag());

		if(p.isMutable())
		{	try
			{	XGAdress a = XGOpcode.getOpcode(p.getDependsOf()).getAdress().complement(val.getAdress());
				XGValue v = XGValue.getValue(dev, a);
				String s = XGTranslationMap.getTranslatedValue(dev, v.toString(), p.getMutableKey());
				return set.get(s);
			}
			catch(InvalidXGAdressException e)
			{	log.info(e.getMessage());
				return new XGParameter(dev, val.getTag());
			}
		}
		else return p;
	}
*/

/*****************************************************************************************************/

	private final XGDevice device;
	private final String name;
//	private final XGOpcode opcode;
//	private final XGObjectType objectType;
	private final int minValue, maxValue;
	private final String longName, shortName;
	private final XGValueTranslator valueTranslator;
	private final XGTranslationMap translationMap;
	private final int mutableKey;
	private final String dependsOf;

	public XGParameter(XGDevice dev, String tag)
	{	this.device = dev;
		this.name = tag;
		this.minValue = DEF_MIN;
		this.maxValue = DEF_MAX;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.longName = DEF_PARAMETERNAME + tag;
		this.shortName = DEF_PARAMETERNAME;
		this.mutableKey = 0;
		this.dependsOf = null;
	}

	public XGParameter(XGDevice dev, XMLNode n)
	{	this.device = dev;
		this.name = n.getChildNode(TAG_NAME).getTextContent();
//		this.opcode = XGOpcode.getOpcode(this.tag);
//		this.objectType = XGObjectType.getObjectTypeOrNew(this.device, this.opcode.getAdress());
		this.minValue = n.parseChildNodeIntegerContent(TAG_MIN, DEF_MIN);
		this.maxValue = n.parseChildNodeIntegerContent(TAG_MAX, DEF_MAX);
		this.longName = n.getChildNodeTextContent(TAG_LONGNAME, this.name);
		this.shortName = n.getChildNodeTextContent(TAG_SHORTNAME, this.name);
		this.valueTranslator = XGValueTranslator.getTranslator(n.getChildNodeTextContent(TAG_TRANSLATOR, ""));
		XMLNode t = n.getChildNode(TAG_TRANSLATIONMAP);
		if(t != null) this.translationMap = dev.getTranslations().get(t.getTextContent());
		else this.translationMap = null;
		this.dependsOf = n.getChildNodeTextContent(TAG_DEPENDSOF, null);
		this.mutableKey = Rest.parseIntOrDefault(Rest.splitStringByUnderscore(this.name), 0);
		log.info("parameter initialized: " + this);
	}

	public String getTag()
	{	return this.name;
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

	public boolean isMutable()
	{	return this.dependsOf != null && this.mutableKey != 0;
	}
//	public XGObjectType getObjectType()
//	{	return this.objectType;}

	public String getDependsOf()
	{	return this.dependsOf;
	}

	public int getMutableKey()
	{	return this.mutableKey;
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

	public XGTranslationMap getTranslationMap()
	{	if(this.translationMap == null) log.info("no translationmap for " + this.longName);
		return this.translationMap;}

	@Override public String toString()
	{	return this.longName;
	}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " " + this.toString();
	}
}
