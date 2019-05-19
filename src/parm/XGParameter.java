package parm;

import java.util.Map;
import msg.Bytes;
import msg.Bytes.ByteType;

public class XGParameter implements XGParameterConstants
{	

/*****************************************************************************************************/

	private ParameterType type;
	private int mutableMapIndex = 0, masterParameterOffset, offset, minValue, maxValue, byteCount;
	private String longName, shortName;
	private ValueTranslator valueTranslator;
	private Map<Integer, String> translationMap;
	private Bytes.ByteType byteType;
	private ValueType valueType;

	public XGParameter(int offs)	//automatischer Konstruktor - unbekannter Parameter
	{	this.type = ParameterType.UNKNOWN;
		this.offset = offs;
		this.minValue = 0;
		this.maxValue = 127;
		this.byteCount = DEF_BYTECOUNT;
		this.mutableMapIndex = 0;
		this.masterParameterOffset = 0;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.byteType = DEF_BYTE_TYPE;
		this.valueType = DEF_VALUE_TYPE;
		this.longName = DEF_LONGNAME + offset;
		this.shortName = DEF_SHORTNAME;
	}

	public void setParameterProperty(String key, String value, String... filter)
	{	switch(key)
		{	case TAG_OFFSET:		this.offset = Integer.parseInt(value); break;
			case TAG_MIN:			this.minValue = Integer.parseInt(value); break;
			case TAG_MAX:			this.maxValue = Integer.parseInt(value); break;
			case TAG_LONGNAME:		this.longName = value; break;
			case TAG_SHORTNAME:		this.shortName = value; break;
			case TAG_BYTECOUNT:		this.byteCount = Integer.parseInt(value); break;
			case TAG_DESCMAPINDEX:	this.mutableMapIndex = Integer.parseInt(value); break;
			case TAG_DEPENDSOF:		this.masterParameterOffset = Integer.parseInt(value); break;
			case TAG_TRANSLATOR:	this.valueTranslator = ValueTranslator.getTranslator(value); break;
			case TAG_TRANSLATIONMAP:this.translationMap = TranslationMap.getTranslationMap(value, filter); break;
			case TAG_BYTETYPE:		this.byteType = ByteType.valueOf(value); break;
			case TAG_VALUETYPE:		this.valueType = ValueType.valueOf(value); break;
		}
	}

	public int getOffset()
	{	return offset;}

	public int getMinValue()
	{	return minValue;}

	public int getMaxValue()
	{	return maxValue;}

	public boolean isLimitizable()
	{	return !this.valueTranslator.equals(ValueTranslator.translateMap);}

	public ParameterType getType()
	{	return this.type;}

	public int getMasterParameterOffset()
	{	return this.masterParameterOffset;}

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
	{	return translationMap;}

	@Override public String toString()
	{	return this.longName;}
}
