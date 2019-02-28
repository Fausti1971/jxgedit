package parm;

import java.util.Map;
import java.util.function.BiFunction;
import obj.XGObject;

public class XGParameter implements XGParameterConstants
{	private static final BiFunction<XGObject, XGParameter, String> DEF_TRANS_TYPE = ValueTranslation::translateToText;

	/*****************************************************************************************************/

	private final XGOpcode opcode;
	private final int minValue, maxValue;
	private String longName, shortName;
	private BiFunction<XGObject, XGParameter, String> valueTranslation;
	private Map<Integer, String> translationMap;

	public XGParameter(XGOpcode opc, int min, int max, String lName, String sName)
	{	this(opc, DEF_TRANS_TYPE, min, max, lName, sName);}

	public XGParameter(XGOpcode opc, Map<Integer, String> table, int min, int max, String lName, String sName)
	{	this.opcode = opc;
		this.minValue = min;
		this.maxValue = max;
		this.setLongName(lName);
		this.setShortName(sName);
		this.valueTranslation = ValueTranslation::translateMap;
		this.setTranslationMap(table);
	}

	public XGParameter(XGOpcode opc, BiFunction<XGObject, XGParameter, String> tType, int min, int max, String lName, String sName)
	{	this.opcode = opc;
		this.minValue = min;
		this.maxValue = max;
		this.setLongName(lName);
		this.setShortName(sName);
		this.valueTranslation = tType;
	}

	public String getValueAsText(XGObject o)
	{	return this.valueTranslation.apply(o, this);}

	public XGOpcode getOpcode()
	{	return this.opcode;}

	public String getLongName()
	{	return this.longName;}

	public void setLongName(String longName)
	{	this.longName = longName;}

	public String getShortName()
	{	return this.shortName;}

	public void setShortName(String shortName)
	{	this.shortName = shortName;}

	public Map<Integer, String> getTranslationMap()
	{	return this.translationMap;}

	public void setTranslationMap(Map<Integer, String> translationMap)
	{	this.translationMap = translationMap;}

	public int getMaxValue()
	{	return this.maxValue;}

	public int getMinValue()
	{	return this.minValue;}

	public int limit(int v)
	{	return Math.max(getMinValue(), Math.min(getMaxValue(), v));}

	@Override public String toString()
	{	return this.getLongName();}
}
