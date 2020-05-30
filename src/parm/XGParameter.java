package parm;
//TODO: noteLimitLow und noteLimitHi beziehen min und max aufeinander; velLimitLow und velLimitHi ebenso (evtl. via parameterSet lösen?); continous-werte für knob/slider überdenken
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
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
			for(XMLNode s : t.getChildNodes(TAG_SET))
			{	Map<Integer, XGParameter> map = new HashMap<>();
				int msb = s.getIntegerAttribute(ATTR_MSB);
				int lsb = s.getIntegerAttribute(ATTR_LSB);
				int v = (msb << 7) | lsb;
				for(XMLNode p : s.getChildNodes(TAG_ENTRY))
				{	int i = p.getIntegerAttribute(ATTR_INDEX);
					XGParameter parm = dev.getParameters().get(p.getStringAttribute(ATTR_PARAMETER_ID));
					map.put(i, parm);
				}
				dev.getParameterSets().put(v, map);
			}
		}
		log.info(dev.getParameters().size() + " parameters initialized");
		return;
	}

/******************************************************************************************************************/

	private final String tag;
	private final String longName, shortName;
	/**
	 * minValue und MaxValue sind im Falle von translationTables anstatt der reelen Values die minimalen bzw. maximalen Indizes der Table und müssen via addContent gesetzt werden
	 */
	private final int minValue, maxValue;
	/**
	 * der Ursprung eines Steuerelementes (bspw. 64 bei Panorama)
	 */
	private final int origin;
//	private final XGValueTranslator valueTranslator;
	private final XGTable translationTable;
	private final String unit;
	private final XGAddress masterAddress;
	private final int index;
	private final boolean isMutable;

	protected XGParameter(XGDevice dev, XMLNode n)
	{	this.tag = n.getStringAttribute(ATTR_ID);
		this.translationTable = dev.getTables().getOrDefault(n.getStringAttribute(ATTR_TRANSLATOR), DEF_TABLE).filter(n);// muss wegen validate() vor origin-Zuweisung ausgeführt werden; ist origin evtl. besser im Component aufgehoben? (template.xml)
		this.minValue = this.setMinValue(n);
		this.maxValue = this.setMaxValue(n);
		this.origin = this.validate(n.getIntegerAttribute(ATTR_ORIGIN, 0));
		this.longName = n.getStringAttribute(ATTR_LONGNAME);
		this.shortName = n.getStringAttribute(ATTR_SHORTNAME);
		this.unit = n.getStringAttribute(ATTR_UNIT, this.translationTable.getUnit());

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
		this.minValue = this.maxValue = this.origin = v;
		this.translationTable = DEF_TABLE;
		this.unit = "*";
		this.masterAddress = null;
		this.index = 0;
		this.isMutable = false;
		log.info("parameter initialized: " + this);
	}

	public int setMinValue(XMLNode n)
	{	return  Math.max(n.getIntegerAttribute(ATTR_MIN, DEF_MIN), 0);
	}

	public int setMaxValue(XMLNode n)
	{	return Math.min(n.getIntegerAttribute(ATTR_MAX, DEF_MAX), this.translationTable.size());
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

	public XGTable getTranslationTable()
	{	return this.translationTable;
	}

	public int getOrigin()
	{	return this.origin;
	}

	public int validate(int i)
	{	return Math.max(Math.min(i, this.maxValue), this.minValue);
	}

	public String getShortName()
	{	return this.shortName;
	}

	public String getLongName()
	{	return this.longName;
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
