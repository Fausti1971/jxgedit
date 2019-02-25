package parm;

import java.util.Map;
import java.util.function.Function;
import msg.XGMessageParameterChange;
import obj.XGObject;

public class XGParameter implements XGParameterConstants
{	private static final Function<XGParameter, String> DEF_TRANS_TYPE = ValueTranslation::translateToText;
	private static final int DEF_MIN = 0, DEF_MAX = 127;

	/*****************************************************************************************************/

	private final Opcode opcode;
	private int minValue = DEF_MIN, maxValue = DEF_MAX;
	private String longName, shortName;
	private Function<XGParameter, String> valueTranslation;
	private Map<Integer, String> translationMap;
	private XGObject xgObject;

	public XGParameter(XGObject o, Opcode opc, int min, int max, String lName, String sName)
	{	this(o, opc, DEF_TRANS_TYPE, min, max, lName, sName);
	}

	public XGParameter(XGObject o, Opcode opc, Map<Integer, String> table, int min, int max, String lName, String sName)
	{	this.xgObject = o;
		this.opcode = opc;
		this.minValue = min;
		this.maxValue = max;
		this.setLongName(lName);
		this.setShortName(sName);
		this.valueTranslation = ValueTranslation::translateMap;
		this.setTranslationMap(table);
	}

	public XGParameter(XGObject o, Opcode opc, Function<XGParameter, String> tType, int min, int max, String lName, String sName)
	{	this.xgObject = o;
		this.opcode = opc;
		this.minValue = min;
		this.maxValue = max;
		this.setLongName(lName);
		this.setShortName(sName);
		this.valueTranslation = tType;
	}

	public int getValue()
	{	return this.getOpcode().getValue();}

	public boolean setValue(int v)
	{	if(xgObject == null) return false;
		boolean changed = (this.getOpcode().setValue(Math.min(maxValue, Math.max(minValue, v)))); 
		if(changed) new XGMessageParameterChange(this).transmit();
		return changed;
	}

	public boolean addValue(int v)
	{	return setValue(getValue() + v);}

	public String getValueAsText()
	{	return valueTranslation.apply(this);}

	public XGObject getXGObject()
	{	return this.xgObject;}

	public Opcode getOpcode()
	{	return opcode;}

	public String getLongName()
	{	return longName;}

	public void setLongName(String longName)
	{	this.longName = longName;}

	public String getShortName()
	{	return shortName;}

	public void setShortName(String shortName)
	{	this.shortName = shortName;}

	public Map<Integer, String> getTranslationMap()
	{	return translationMap;}

	public void setTranslationMap(Map<Integer, String> translationMap)
	{	this.translationMap = translationMap;}

	public int getMaxValue()
	{	return maxValue;}

	public void setMaxValue(int maxValue)
	{	this.maxValue=maxValue;}

	public int getMinValue()
	{	return minValue;}

	public void setMinValue(int minValue)
	{	this.minValue=minValue;}
}
