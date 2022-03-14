package parm;
import java.io.*;
import application.*;
import static parm.XGTable.TABLES;import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants
{
//	public static XMLNode init()
//	{
//		try
//		{	return XMLNode.parse(JXG.class.getResourceAsStream(XML_PARAMETER));
//		}
//		catch(IOException e)
//		{	LOG.severe(e.getMessage());
////			return new XMLNode(TAG_PARAMETERTABLES);
//			return null;
//		}
//	}

/******************************************************************************************************************/

	private final String name, shortName;
	private final XGTable translationTable;
	private final int minValue, maxValue, originValue, defaultValue;
	private final String unit;
	private final boolean isValid;//TODO: keine Ahnung, wofür das gut ist...

	public XGParameter(XMLNode n)
	{	this.translationTable = TABLES.getOrDefault(n.getStringAttribute(ATTR_TABLE), XGVirtualTable.DEF_TABLE).filter(n);
		this.minValue = n.getValueAttribute(ATTR_MIN, UNLIMITED);//falls keine ATTR_MIN angegeben wurde wird min auf UNLIMITED gesetzt, was bewirkt, dass die Range durch die Table bestimmt wird
		this.maxValue = n.getValueAttribute(ATTR_MAX, UNLIMITED);//falls keine ATTR_MAX angegeben wurde wird max auf UNLIMITED gesetzt, was bewirkt, dass die Range durch die Table bestimmt wird
		this.defaultValue = n.getValueAttribute(ATTR_DEFAULT, this.getMinValue());
		this.originValue = n.getValueAttribute(ATTR_ORIGIN, this.defaultValue);
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
		this.originValue = v;
		this.defaultValue = v;
		this.unit = "";
		this.isValid = v != NO_PARAMETERVALUE;
	}

	public boolean isValid(){	return this.isValid;}

	public XGTable getTranslationTable(){	return this.translationTable;}

	public int getMinIndex()
	{	if(this.minValue == UNLIMITED) return this.translationTable.getMinIndex();
		else return this.translationTable.getIndex(this.minValue);
	}

	public int getMaxIndex()
	{	if(this.maxValue == UNLIMITED) return this.translationTable.getMaxIndex();
		else return this.translationTable.getIndex(this.maxValue);
	}

	public int getMinValue()
	{	if(this.minValue == UNLIMITED) return this.translationTable.getMinEntry().getValue();
		else return this.minValue;
	}

	public int getMaxValue()
	{	if(this.maxValue == UNLIMITED) return this.translationTable.getMaxEntry().getValue();
		else return this.maxValue;
	}

	public int getOriginIndex(){	return this.translationTable.getIndex(this.originValue);}

	public int getLimitizedIndex(int i)
	{	return Math.max(Math.min(i, this.getMaxIndex()), this.getMinIndex());
	}

	public int getLimitizedValue(int v)
	{	return Math.max(Math.min(v, this.getMaxValue()), this.getMinValue());
	}

	public String getShortName(){	return this.shortName;}

	public String getName(){	return this.name;}

	public String getUnit()
	{	if(this.unit.isEmpty()) return this.translationTable.getUnit();
		else return this.unit;
	}

	@Override public String toString(){	return this.name;}
}
