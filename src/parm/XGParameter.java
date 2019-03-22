package parm;

import java.util.Map;
import java.util.function.BiFunction;
import obj.XGObject;

public class XGParameter implements XGParameterConstants
{	private static final BiFunction<XGObject, XGParameter, String> DEF_TRANS_TYPE = ValueTranslation::translateToText;

	/*****************************************************************************************************/

	private final int minValue, maxValue, offset, masterOffset;
	private final String longName, shortName;
	private final BiFunction<XGObject, XGParameter, String> valueTranslation;
	private final Map<Integer, String> translationMap;

	public XGParameter(int offs)
	{	this(offs, 0, 127, "parameter " + offs, "unknown");}
	
	public XGParameter(int offs, int master)
	{	this.offset = offs;
		this.masterOffset= master;
		this.minValue = 0;
		this.maxValue = 0;
		this.longName = "";
		this.shortName = "";
		this.valueTranslation = DEF_TRANS_TYPE;
		this.translationMap = null;
	}

	public XGParameter(int offs, int min, int max, String lName, String sName)
	{	this(offs, DEF_TRANS_TYPE, min, max, lName, sName);}

	public XGParameter(int offs, Map<Integer, String> table, int min, int max, String lName, String sName)
	{	this.offset = offs;
		this.masterOffset= offs;
		this.minValue = min;
		this.maxValue = max;
		this.longName = lName;
		this.shortName = sName;
		this.valueTranslation = ValueTranslation::translateMap;
		this.translationMap = table;
	}

	public XGParameter(int offs, BiFunction<XGObject, XGParameter, String> tType, int min, int max, String lName, String sName)
	{	this.offset = offs;
		this.masterOffset= offs;
		this.minValue = min;
		this.maxValue = max;
		this.longName = lName;
		this.shortName = sName;
		this.valueTranslation = tType;
		this.translationMap = null;
	}

	public int getMasterValueOffset()
	{	if(this.isVariable()) return this.masterOffset;
		else return this.offset;
	}

	public boolean isVariable()
	{	return this.offset != this.masterOffset;}

	public String getValueAsText(XGObject o)
	{	return this.valueTranslation.apply(o, this);}

	public String getLongName()
	{	return this.longName;}

	public String getShortName()
	{	return this.shortName;}

	public Map<Integer, String> getTranslationMap()
	{	return this.translationMap;}

	public int getMaxValue()
	{	return this.maxValue;}

	public int getMinValue()
	{	return this.minValue;}

	public int limitize(int v)
	{	return Math.max(getMinValue(), Math.min(getMaxValue(), v));}

	@Override public String toString()
	{	return this.getLongName();}
}
