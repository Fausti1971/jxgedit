package parm;

import java.io.File;
import java.io.FileNotFoundException;
import adress.XGAddress;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants, XGTagable
{
	public static void init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return;
		}

		XMLNode xml = XMLNode.parse(file);
		for(XMLNode t : xml.getChildNodes(TAG_TABLE))
		{	for(XMLNode p : t.getChildNodes(TAG_ITEM))
			{	XGParameter prm = new XGParameter(dev, p);
				dev.getParameters().add(prm);
			}
			//TODO: parameterSets nicht vergessen...
		}
		log.info(dev.getParameters().size() + " parameters initialized");
		return;
	}

/******************************************************************************************************************/

	private final String tag;
	private final String longName, shortName;
	private final int minValue, maxValue;
	private final XGValueTranslator valueTranslator;
	private final String translationMapName;
	private final String unit;
	private final XGAddress masterAddress;
	private final int index;
	private final boolean isMutable;

	protected XGParameter(XGDevice dev, XMLNode n)
	{	this.tag = n.getStringAttribute(ATTR_ID);
		this.minValue = n.getIntegerAttribute(ATTR_MIN, DEF_MIN);
		this.maxValue = n.getIntegerAttribute(ATTR_MAX, DEF_MAX);
		this.longName = n.getStringAttribute(ATTR_LONGNAME);
		this.shortName = n.getStringAttribute(ATTR_SHORTNAME);
		this.valueTranslator = XGValueTranslator.getTranslator(n.getStringAttribute(ATTR_TRANSLATOR));
		this.translationMapName = n.getStringAttribute(ATTR_TRANSLATIONMAP);
		this.unit = n.getStringAttribute(ATTR_UNIT, "");

		if(n.hasAttribute(ATTR_MASTER))
		{	this.masterAddress = new XGAddress(n.getStringAttribute(ATTR_MASTER), null);
			this.index = n.getIntegerAttribute(ATTR_INDEX, 0);
			this.isMutable = true;
		}
		else
		{	this.masterAddress = null;
			this.index = 0;
			this.isMutable = false;
		}
		log.info("parameter initialized: " + this);
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.tag = name;
		this.longName = DEF_PARAMETERNAME;
		this.shortName = name;
		this.minValue = this.maxValue = v;
		this.valueTranslator = XGValueTranslator.normal;
		this.unit = "*";
		this.translationMapName = null;
		this.masterAddress = null;
		this.index = 0;
		this.isMutable = false;
		log.info("parameter initialized: " + this);
	}

	public boolean isMutable()
	{	return this.isMutable;
	}

	public XGAddress getMasterAddress()
	{	return this.masterAddress;
	}

	public int getIndex()
	{	return this.index;
	}

	public int getMinValue()
	{	return this.minValue;
	}

	public int getMaxValue()
	{	return this.maxValue;
	}

	public String getShortName()
	{	return this.shortName;
	}

	public String getLongName()
	{	return this.longName;
	}

	public XGValueTranslator getValueTranslator()
	{	return this.valueTranslator;
	}

	public String getTranslationMapName()
	{	return this.translationMapName;
	}

	public String getUnit()
	{	return this.unit;
	}

	@Override public String toString()
	{	return this.tag;
	}

	@Override public String getTag()
	{	return this.tag;
	}
}
