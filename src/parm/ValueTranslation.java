package parm;

import java.util.function.Function;

public interface ValueTranslation extends Function<XGParameter, String>
{
	static String translateToText(XGParameter p)
	{	return "" + p.getValue();
	}

	static String translateToTextPlus1(XGParameter p)
	{	return "" + (p.getValue() + 1);
	}

	static String translateTable(XGParameter p)
	{	try
		{	return p.translationMap.get(p.getValue());
		}
		catch(NullPointerException e)
		{	return "no value";
		}
	}
/*
	static Map<Integer, String> channelTable = Map.ofEntries(
		Map.entry(0, "A01"), Map.entry(1, "A02"), Map.entry(2, "A03"), Map.entry(3, "A04"), Map.entry(4, "A05"), Map.entry(5, "A06"), Map.entry(6, "A07"), Map.entry(7, "A08"),
		Map.entry(8, "A09"), Map.entry(9, "A10"), Map.entry(10, "A11"), Map.entry(11, "A12"), Map.entry(12, "A13"), Map.entry(13, "A14"), Map.entry(14, "A15"), Map.entry(15, "A16"),
		Map.entry(16, "B01"), Map.entry(17, "B02"), Map.entry(18, "B03"), Map.entry(19, "B04"), Map.entry(20, "B05"), Map.entry(21, "B06"), Map.entry(22, "B07"), Map.entry(23, "B08"),
		Map.entry(24, "B09"), Map.entry(25, "B10"), Map.entry(26, "B11"), Map.entry(27, "B12"), Map.entry(28, "B13"), Map.entry(29, "B14"), Map.entry(30, "B15"), Map.entry(31, "B16"), Map.entry(127, "OFF"));
*/
}
