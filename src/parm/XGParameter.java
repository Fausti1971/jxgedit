package parm;

import java.util.Map;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import application.Rest;
import midi.Bytes;
import midi.Bytes.ByteType;
import value.TranslationMap;
import value.ValueTranslator;

public class XGParameter implements XGParameterConstants, XGAdressable
{

/*****************************************************************************************************/

	private ParameterType type;
	private XGAdress adress, masterParameterAdress;
	private int mutableMapIndex = 0, minValue, maxValue, byteCount;
	private String longName, shortName;
	private ValueTranslator valueTranslator;
	private Map<Integer, String> translationMap;
	private Bytes.ByteType byteType;
	private ValueType valueType;

	public XGParameter(XGAdress adr)	//automatischer Konstruktor - unbekannter Parameter
	{	this.type = ParameterType.UNKNOWN;
		this.adress = adr;
		this.minValue = 0;
		this.maxValue = 127;
		this.byteCount = DEF_BYTECOUNT;
		this.mutableMapIndex = 0;
		this.masterParameterAdress = null;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.byteType = DEF_BYTE_TYPE;
		this.valueType = DEF_VALUE_TYPE;
		this.longName = DEF_LONGNAME + adr;
		this.shortName = DEF_SHORTNAME;
	}

	protected XGParameter(Map<String, String> map) throws InvalidXGAdressException
	{	this(new XGAdress(map.get(TAG_ADRESS_HI), map.get(TAG_ADRESS_MID), map.get(TAG_ADRESS_LO)));
		this.minValue = Rest.parseIntOrDefault(map.get(TAG_MIN), DEF_MIN);
		this.maxValue = Rest.parseIntOrDefault(map.get(TAG_MAX), DEF_MAX);
		this.longName = map.getOrDefault(TAG_LONGNAME, DEF_LONGNAME);
		this.shortName = map.getOrDefault(TAG_SHORTNAME, DEF_SHORTNAME);
		this.byteCount = Rest.parseIntOrDefault(map.get(TAG_BYTECOUNT), DEF_BYTECOUNT);
		try
		{	this.byteType = ByteType.valueOf(map.get(TAG_BYTETYPE));}
		catch(NullPointerException e)
		{	this.byteType = DEF_BYTE_TYPE;}
		try
		{	this.valueType = ValueType.valueOf(map.get(TAG_VALUETYPE));}
		catch(NullPointerException e)
		{	this.valueType = DEF_VALUE_TYPE;}
		this.mutableMapIndex = Rest.parseIntOrDefault(map.get(TAG_DESCMAPINDEX), 0);
		this.masterParameterAdress = new XGAdress(map.get(TAG_DEPENDSOF_HI), map.get(TAG_DEPENDSOF_MID), map.get(TAG_DEPENDSOF_LO));
		this.valueTranslator = ValueTranslator.getTranslator(map.get(TAG_TRANSLATOR));
		this.translationMap = TranslationMap.getTranslationMap(map.get(TAG_TRANSLATIONMAP), Rest.splitString(map.get(TAG_FILTER)));
	}

	public XGAdress getAdress()
	{	return this.adress;}

	public int getMinValue()
	{	return minValue;}

	public int getMaxValue()
	{	return maxValue;}

	public boolean isLimitizable()
	{	return !this.valueTranslator.equals(ValueTranslator.translateMap);}

	public ParameterType getType()
	{	return this.type;}

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

	public Bytes.ByteType getByteType()
	{	return byteType;}

	public ValueType getValueType()
	{	return valueType;}

	public ValueTranslator getValueTranslator()
	{	return valueTranslator;}

	public Map<Integer, String> getTranslationMap()
	{	if(this.translationMap == null) System.out.println("no translationmap for " + this.longName);
		return this.translationMap;}

	@Override public String toString()
	{	return this.longName;}
}
