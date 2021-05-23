package parm;
import java.io.*;
import application.*;
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
			return new XMLNode(TAG_PARAMETERTABLES);
		}
	}

/******************************************************************************************************************/

	private final String name, shortName;
	private final XGTable translationTable;
	private final int minValue, maxValue, originIndex;
	private final String unit;
	private final boolean isValid;//TODO: keine Ahnung, wofür das gut ist...

	public XGParameter(XMLNode n)
	{	this.translationTable = TABLES.getOrDefault(n.getStringAttribute(ATTR_TABLE), XGVirtualTable.DEF_TABLE).filter(n);

		this.minValue = n.getValueAttribute(ATTR_MIN, UNLIMITED);
		this.maxValue = n.getValueAttribute(ATTR_MAX, UNLIMITED);
		int originValue = n.getValueAttribute(ATTR_ORIGIN, n.getValueAttribute(ATTR_DEFAULT, this.minValue));
		if(originValue == UNLIMITED) originValue = 0;
		this.originIndex = this.translationTable.getIndex(originValue, this.translationTable.getMinIndex());

		this.name = n.getStringAttribute(ATTR_NAME);
		this.shortName = n.getStringAttributeOrDefault(ATTR_SHORTNAME, this.name);
		this.unit = n.getStringAttributeOrDefault(ATTR_UNIT, this.translationTable.getUnit());
		this.isValid = true;
//System.out.println("par:" + this.name + " tab:" + this.translationTable + " min:" + this.minIndex + " max:" + this.maxIndex);
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.name = name;
		this.shortName = name;
		this.translationTable = XGVirtualTable.DEF_TABLE;

		this.minValue = v;
		this.maxValue = v;
		this.originIndex = v;

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
	{	if(this.minValue == UNLIMITED) return this.translationTable.getMinIndex();
		else return this.translationTable.getIndex(this.minValue, this.translationTable.getMinIndex());
	}

	public int getMaxIndex()
	{	if(this.maxValue == UNLIMITED) return this.translationTable.getMaxIndex();
		else return this.translationTable.getIndex(this.maxValue, this.translationTable.getMaxIndex());
	}

	public int getMinValue()
	{	return this.minValue;
	}

	public int getMaxValue()
	{	return this.maxValue;
	}

	public int getOriginIndex()
	{	return this.originIndex;
	}

	public int limitize(int i)
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
