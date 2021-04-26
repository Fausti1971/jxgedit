package parm;
import java.io.*;
import application.*;
import parm.XGTable.Preference;
import static parm.XGTable.TABLES;import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants
{
	public static XMLNode init()
	{
		try
		{	return XMLNode.parse(JXG.getResourceStream(XML_PARAMETER));
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
			return new XMLNode("parameterTables");
		}
	}

/******************************************************************************************************************/

	private final String name, shortName;
	private final XGTable translationTable;
	private final int minIndex, maxIndex, originIndex;
	private final String unit;
	private final boolean isValid;//TODO: keine Ahnung, wofür das gut ist...

	public XGParameter(XMLNode n)
	{	this.translationTable = TABLES.getOrDefault(n.getStringAttribute(ATTR_TRANSLATOR), XGVirtualTable.DEF_TABLE).filter(n);

		int minValue = n.getValueAttribute(ATTR_MIN, DEF_MIN);
		int maxValue = n.getValueAttribute(ATTR_MAX, DEF_MAX);
		int originValue = n.getValueAttribute(ATTR_ORIGIN, minValue);
		this.minIndex = this.translationTable.getIndex(minValue, Preference.ABOVE);
		this.maxIndex = this.translationTable.getIndex(maxValue, Preference.BELOW);
		this.originIndex = this.validate(this.translationTable.getIndex(originValue, Preference.CLOSEST));

		this.name = n.getStringAttribute(ATTR_NAME);
		this.shortName = n.getStringAttributeOrDefault(ATTR_SHORTNAME, this.name);
		this.unit = n.getStringAttributeOrDefault(ATTR_UNIT, this.translationTable.getUnit());
		this.isValid = true;
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.name = name;
		this.shortName = name;
		this.translationTable = XGVirtualTable.DEF_TABLE;

		this.minIndex = this.translationTable.getIndex(v, Preference.CLOSEST);
		this.maxIndex = this.translationTable.getIndex(v, Preference.CLOSEST);
		this.originIndex = this.translationTable.getIndex(v, Preference.CLOSEST);

		this.unit = "";
		this.isValid = v != NO_PARAMETERVALUE;
	}

	public boolean isValid()
	{	return this.isValid;
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

	public int getMinValue()
	{	return this.translationTable.getMinEntry().getValue();
	}

	public int getMaxValue()
	{	return this.translationTable.getMaxEntry().getValue();
	}

	public int getOriginIndex()
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

	@Override public String toString()
	{	return this.name;
	}
}
