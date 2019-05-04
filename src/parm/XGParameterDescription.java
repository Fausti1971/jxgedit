package parm;

import java.util.Map;

public class XGParameterDescription
{

/*****************************************************************************************/

	private final int offset, minValue, maxValue;
	private final String longName, shortName;
	private final ValueTranslator valueTranslator;
	private final Map<Integer, String> translationMap;

	public XGParameterDescription(int offs, int min, int max, String lName, String sName, String translator, String mapName, String mapFilter)
	{	this.offset = offs;
		this.minValue = min;
		this.maxValue = max;
		this.longName = lName;
		this.shortName = sName;
		this.valueTranslator = ValueTranslator.getTranslator(translator);
		this.translationMap = TranslationMap.getTranslationMap(mapName, mapFilter);
	}

	public int getOffset()
	{	return offset;}

	public int getMinValue()
	{	return minValue;}

	public int getMaxValue()
	{	return maxValue;}

	public String getLongName()
	{	return longName;}

	public String getShortName()
	{	return shortName;}

	public ValueTranslator getValueTranslator()
	{	return valueTranslator;}

	public Map<Integer, String> getTranslationMap()
	{	return translationMap;}
}
