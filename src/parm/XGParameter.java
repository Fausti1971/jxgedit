package parm;
import java.io.File;
import java.io.FileNotFoundException;
import application.XGLoggable;
import application.XGStrings;
import device.XGDevice;
import parm.XGTable.Preference;
import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants
{
	public static XMLNode init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	LOG.info(e.getMessage());
			return new XMLNode("parameterTables");
		}
		return XMLNode.parse(file);
	}

/******************************************************************************************************************/

	private final String name, shortName;
	private final XGTable translationTable;
	private final int minIndex, maxIndex, originIndex;
	private final String unit;

	protected XGParameter(XGDevice dev, XMLNode n)
	{	this.translationTable = dev.getTables().getOrDefault(n.getStringAttribute(ATTR_TRANSLATOR), XGVirtualTable.DEF_TABLE).filter(n);
		if(this.translationTable == null)
		{	LOG.severe(this.getClass().getSimpleName() + " " + this + " has no Table!");
			throw new RuntimeException("no table: " + this);
		}

		int minValue = n.getValueAttribute(ATTR_MIN, DEF_MIN);
		int maxValue = n.getValueAttribute(ATTR_MAX, DEF_MAX);
		int originValue = n.getIntegerAttribute(ATTR_ORIGIN, DEF_ORIGIN);
		this.minIndex = this.translationTable.getIndex(minValue, Preference.ABOVE);
		this.maxIndex = this.translationTable.getIndex(maxValue, Preference.BELOW);
		this.originIndex = this.validate(this.translationTable.getIndex(originValue, Preference.CLOSEST));

		this.name = n.getStringAttribute(ATTR_NAME);
		this.shortName = n.getStringAttributeOrDefault(ATTR_SHORTNAME, XGStrings.toShortName(this.name));
		this.unit = n.getStringAttributeOrDefault(ATTR_UNIT, this.translationTable.getUnit());

		LOG.info(this.getClass().getSimpleName() + " " + this + " intialized");
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.name = DEF_PARAMETERNAME;
		this.shortName = name;
		this.translationTable = XGVirtualTable.DEF_TABLE;

		this.minIndex = this.translationTable.getIndex(v, Preference.CLOSEST);
		this.maxIndex = this.translationTable.getIndex(v, Preference.CLOSEST);
		this.originIndex = this.translationTable.getIndex(v, Preference.CLOSEST);

		this.unit = "*";
		if(this.translationTable == null)
		{	LOG.severe(this.getClass().getSimpleName() + " " + this + " has no Table!");
			throw new RuntimeException("no table: " + this);
		}
//		LOG.info(this.getClass().getSimpleName() + " " + this.getInfo() + " intialized");
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

	public String getName()
	{	return this.name;
	}

	public String getUnit()
	{	return this.unit;
	}

	public String getInfo()
	{	return this.name + " (" + this.translationTable.getInfo() + " " + this.minIndex + "..." + this.maxIndex + ")";
	}

	@Override public String toString()
	{	return this.name;
	}
}
