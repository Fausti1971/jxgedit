package parm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import application.ConfigurationConstants;
import application.Rest;
import device.XGDevice;
import opcode.NoSuchOpcodeException;
import tag.XGTagable;
import tag.XGTagableSet;
import value.XGValue;
import value.XGValueTranslator;
import xml.XMLNode;
//TODO: Nachdenken: braucht tatsächlich der parameter den opcode und den objecttyp? eher doch der opcode...
public class XGParameter implements ConfigurationConstants, XGParameterConstants, XGTagable
{	private static final Logger log = Logger.getAnonymousLogger();
	private static final Map<XGDevice, XGTagableSet<XGParameter>> STORAGE = new HashMap<>();

	public static void init(XGDevice dev)
	{	XGTagableSet<XGParameter> set = new XGTagableSet<>();
		File file = dev.getResourceFile(XML_PARAMETER);
		try
		{	XMLNode xml = XMLNode.parse(file);
			for(XMLNode x : xml.getChildren())
				if(x.getTag().equals(TAG_PARAMETER))
					set.add(new XGParameter(dev, x));
		}
		catch(XMLStreamException | NoSuchOpcodeException e1)
		{	e1.printStackTrace();
		}
		STORAGE.put(dev, set);
		log.info(dev + ": " + set.size() + " parameters initialized");
	}

	public static XGParameter getParameter(XGDevice dev, String tag)
	{	if(STORAGE.containsKey(dev))
			if(STORAGE.get(dev).containsKey(tag))
			{	XGParameter p = STORAGE.get(dev).get(tag);
				if(p.isMutable())
				{	XGValue v = XGValue.getValue(dev, p.getDependsOf());
					String s = XGTranslationMap.getTranslatedValue(dev, v.toString(), p.getMutableKey());
					return XGParameter.getParameter(dev, s);
				}
			}
		return STORAGE.get(XGDevice.getDefaultDevice()).get(tag);
	}

	public XGDevice getDevice()
	{	return this.device;
	}

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
/*
	XGParameter(String tag, int min, int max)	//für XGMODELNAMEPARAMETER erforderlich
	{	this.tag = tag;
		this.opcode = XGOpcode.getOpcode(tag);
		this.objectType = XGObjectType.getObjectTypeOrNew(this.opcode.getAdress());
		this.minValue = min;
		this.maxValue = max;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.longName = this.opcode.getInfo();
		this.shortName = this.tag;
	}
*/
	public XGParameter(XGDevice dev, XMLNode n) throws NoSuchOpcodeException
	{	this.device = dev;
		this.name = n.getChildNode(TAG_NAME).getTextContent();
//		this.opcode = XGOpcode.getOpcode(this.tag);
//		this.objectType = XGObjectType.getObjectTypeOrNew(this.device, this.opcode.getAdress());
		this.minValue = n.parseChildNodeTextContent(TAG_MIN, DEF_MIN);
		this.maxValue = n.parseChildNodeTextContent(TAG_MAX, DEF_MAX);
		this.longName = n.getChildNodeTextContent(TAG_LONGNAME, this.name);
		this.shortName = n.getChildNodeTextContent(TAG_SHORTNAME, this.name);
		this.valueTranslator = XGValueTranslator.getTranslator(n.getChildNodeTextContent(TAG_TRANSLATOR, ""));
		XMLNode t = n.getChildNode(TAG_TRANSLATIONMAP);
		if(t != null) this.translationMap = XGTranslationMap.getTranslationMap(dev, t.getTextContent());
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
