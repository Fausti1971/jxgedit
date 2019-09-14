package parm;

import java.util.Map;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import adress.XGAdress;
import adress.XGAdressable;
import application.Rest;
import msg.XGByteArray;
import msg.XGByteArray.DataType;
import obj.XGObjectType;
import value.XGValueTranslationMap;
import value.XGValueTranslator;

public class XGParameter implements XGParameterConstants, XGAdressable
{	static Logger log = Logger.getAnonymousLogger();

/*****************************************************************************************************/

	private final XGObjectType objectType;
	private final XGAdress adress, masterParameterAdress;
	private final int mutableMapIndex, minValue, maxValue, byteCount;
	private final String longName, shortName;
	private final XGValueTranslator valueTranslator;
	private final Map<Integer, String> translationMap;
	private final XGByteArray.DataType byteType;
	private final ValueDataClass valueClass;

	XGParameter(XGAdress adr, int min, int max, int bc, DataType bt, ValueDataClass vdc)	//für XGMODELNAMEPARAMETER erforderlich
	{	this.objectType = XGObjectType.getObjectTypeOrNew(adr);
		this.adress = adr;
		this.minValue = min;
		this.maxValue = max;
		this.byteCount = bc;
		this.mutableMapIndex = 0;
		this.masterParameterAdress = null;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.byteType = bt;
		this.valueClass = vdc;
		this.longName = DEF_LONGNAME + " " + adr;
		this.shortName = DEF_SHORTNAME;
	}
	public XGParameter(XGAdress adr)	//automatischer Konstruktor - unbekannter Parameter
	{	this(adr, 0, 127, DEF_BYTECOUNT, DEF_BYTE_TYPE, DEF_VALUECLASS);
	}

	public XGParameter(Node e)
	{	Node n = Rest.getFirstChildNodeByTag(e, TAG_ADRESS);
		this.adress = new XGAdress(n);

		this.objectType = XGObjectType.getObjectTypeOrNew(this.adress);

		String s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_MIN);
		this.minValue = Rest.parseIntOrDefault(s, DEF_MIN);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_MAX);
		this.maxValue = Rest.parseIntOrDefault(s, DEF_MAX);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_LONGNAME);
		this.longName = Rest.getStringOrDefault(s, DEF_LONGNAME);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_SHORTNAME);
		this.shortName = Rest.getStringOrDefault(s, DEF_SHORTNAME);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_BYTECOUNT);
		this.byteCount = Rest.parseIntOrDefault(s, DEF_BYTECOUNT);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_BYTETYPE);
		this.byteType = DataType.valueOf(Rest.getStringOrDefault(s, DEF_BYTE_TYPE.name()));

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_VALUECLASS);
		this.valueClass = ValueDataClass.valueOf(Rest.getStringOrDefault(s, DEF_VALUECLASS.name()));
		
		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_DESCMAPINDEX);
		this.mutableMapIndex = Rest.parseIntOrDefault(s, 0);

		n = Rest.getFirstChildNodeByTag(e,TAG_DEPENDSOFADRESS);
		this.masterParameterAdress = new XGAdress(n);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_TRANSLATOR);
		this.valueTranslator = XGValueTranslator.getTranslator(s);

		n = Rest.getFirstChildNodeByTag(e, TAG_TRANSLATIONMAP);
		if(n != null) this.translationMap = XGValueTranslationMap.getTranslationMap(n.getTextContent(), Rest.splitNodesAttributeMap(n, ATTR_FILTER));
		else this.translationMap = null;
	}

	public XGAdress getAdress()
	{	return this.adress;}

	public int getMinValue()
	{	return minValue;}

	public int getMaxValue()
	{	return maxValue;}

	public boolean isLimitizable()
	{	return !this.valueTranslator.equals(XGValueTranslator.translateMap);}

	public XGObjectType getObjectType()
	{	return this.objectType;}

	public XGAdress getMasterParameterAdress()
	{	return this.masterParameterAdress;}

	public int getMutableIndex()
	{	return this.mutableMapIndex;}

	public int getByteCount()
	{	return byteCount;}

	public String getShortName()
	{	return shortName;}

	public String getLongName()
	{	return this.longName;}

	public XGByteArray.DataType getByteType()
	{	return byteType;}

	public ValueDataClass getValueClass()
	{	return valueClass;}

	public XGValueTranslator getValueTranslator()
	{	return valueTranslator;}

	public Map<Integer, String> getTranslationMap()
	{	if(this.translationMap == null) log.info("no translationmap for " + this.longName);
		return this.translationMap;}

	@Override public String toString()
	{	return this.longName;}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " " + this.toString();
	}
}
