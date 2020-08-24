package parm;
import java.io.File;
import java.io.FileNotFoundException;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants, XGTagable
{
	public static void init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_MODULE);
		}
		catch(FileNotFoundException e)
		{	LOG.info(e.getMessage());
			return;
		}

		XMLNode xml = XMLNode.parse(file);
		XMLNode t = xml.getChildNode(TAG_PARAMETERS);
		for(XMLNode p : t.getChildNodes(TAG_PARAMETER)) dev.getParameters().add(new XGParameter(dev, p));

		LOG.info(dev.getParameters().size() + " parameters initialized");
		return;
	}

/******************************************************************************************************************/

	private final String tag;
	private final String longName, shortName;
	private final XGTable translationTable;
	private final int minIndex, maxIndex, originIndex;
	private final String unit;

	protected XGParameter(XGDevice dev, XMLNode n)
	{	this.tag = n.getStringAttribute(ATTR_ID).toString();
		this.translationTable = dev.getTables().getOrDefault(n.getStringAttribute(ATTR_TRANSLATOR), XGVirtualTable.DEF_TABLE).filter(n);// ist origin evtl. besser im Component aufgehoben? (template.xml)

		int minValue = n.getIntegerAttribute(ATTR_MIN, DEF_MIN);
		int maxValue = n.getIntegerAttribute(ATTR_MAX, DEF_MAX);
		int originValue = n.getIntegerAttribute(ATTR_ORIGIN, DEF_ORIGIN);
		this.minIndex = this.translationTable.getIndex(minValue);
		this.maxIndex = this.translationTable.getIndex(maxValue);
		this.originIndex = this.validate(this.translationTable.getIndex(originValue));

		this.longName = n.getStringAttribute(ATTR_LONGNAME);
		this.shortName = n.getStringAttribute(ATTR_SHORTNAME);
		this.unit = n.getStringAttributeOrDefault(ATTR_UNIT, this.translationTable.getUnit());
		if(this.translationTable == null)
		{	LOG.severe(this.getClass().getSimpleName() + " " + this + " has no Table!");
			throw new RuntimeException("no table: " + this);
		}
		LOG.info(this.getClass().getSimpleName() + " " + this + " intialized");
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.tag = name;
		this.longName = DEF_PARAMETERNAME;
		this.shortName = name;
		this.translationTable = XGVirtualTable.DEF_TABLE;

		this.minIndex = this.translationTable.getIndex(v);
		this.maxIndex = this.translationTable.getIndex(v);
		this.originIndex = this.translationTable.getIndex(v);

		this.unit = "*";
		if(this.translationTable == null)
		{	LOG.severe(this.getClass().getSimpleName() + " " + this + " has no Table!");
			throw new RuntimeException("no table: " + this);
		}
		LOG.info(this.getClass().getSimpleName() + " " + this + " intialized");
	}

	public XGTable getTranslationTable()
	{	return this.translationTable;
	}

	public int getMinIndex()
	{	return this.minIndex;
	}

	public int getMaxIndex()
	{	return this.maxIndex;
	}

	public int getOrigin()
	{	return this.originIndex;
	}

	public int validate(int i)
	{	return Math.max(Math.min(i, this.getMaxIndex()), this.getMinIndex());
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
	{	return this.longName;
	}

	@Override public String getTag()
	{	return this.tag;
	}
}
