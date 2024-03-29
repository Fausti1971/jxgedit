package table;

import java.util.*;
import java.util.function.Function;
import application.XGMath;
import application.XGStrings;
import xml.XMLNode;

public class XGVirtualTable implements XGTable
{	private static final int MIN = 0, MAX = Integer.MAX_VALUE & 0x0000FFFF;
	public static final XGTable DEF_TABLE = new XGVirtualTable(MIN, MAX & 0x0000FFFF, DEF_TABLENAME, Object::toString, Integer::parseInt);

	public static void init()
	{	TABLES.add(DEF_TABLE);

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_ADD1,
			(Integer i)->String.format("%02d", i + 1),
			(String s)->XGStrings.parseIntOrDefault(s, MIN + 1) - 1));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_DIV10,
			(Integer i)->Float.toString((float)i / 10),
			(String s)->{float f = Float.parseFloat(s) * 10; return (int)f;}));

		TABLES.add
		(	new XGVirtualTable(MIN, MAX, TABLE_SUB64,
			(Integer i)->Integer.toString(i - 64),
			(String s)->Integer.parseInt(s) + 64));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB1024DIV10,
			(Integer i)->{float f = (i.floatValue() - 1024) / 10; return Float.toString(f);},
			(String s)->{float f = Float.parseFloat(s) * 10 + 1024; return (int)f;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB128DIV10,
			(Integer i)->{float f = (i.floatValue() - 128) / 10; return Float.toString(f);},
			(String s)->{float f = Float.parseFloat(s) * 10 + 128; return (int)f;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB8k,
			(Integer i)->Integer.toString(i - 0x2040),
			(String s)->Integer.parseInt(s) + 0x2040));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_PANORAMA,
			(Integer i)->
			{	if(i == 0) return "Rnd";
				if(i < 64) return "L" + Math.abs(i - 64);
				if(i > 64) return "R" + Math.abs(i - 64);
				else return "C";
			},
			(String s)->
			{	if(s.equalsIgnoreCase("Rnd")) return 0;
				if(s.equalsIgnoreCase("C")) return 64;
				int val = XGStrings.toNumber(s);
				if(s.substring(0, 1).equalsIgnoreCase("R")) return val + 64;
				if(s.substring(0, 1).equalsIgnoreCase("L")) return 64 - val;
				return val;
			}));

		TABLES.add(new XGVirtualTable(4, 124, TABLE_DEGREES,
			(Integer i)->XGMath.linearScale(i, 4, 124, -180, 180) + "°",
			(String s)->XGMath.linearScale(XGStrings.toNumber(s), -180, 180, 4, 124)));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_PERCENT,
			(Integer i)->XGMath.linearScale(i, 0, 127, 0, 100) + "%",
			(String s)->XGMath.linearScale(XGStrings.toNumber(s), 0, 100, 0, 127)));

		TABLES.add(new XGVirtualTable(0,127, TABLE_KEYS, XGStrings::encodeKey, XGStrings::decodeKey));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_GAIN,
			(Integer i)->String.valueOf(XGMath.linearScale(i, 0, 127, -12, 12)),
			(String s)->XGMath.linearScale(XGStrings.toNumber(s), -12, 12, 0, 127)));

		TABLES.add(new XGVirtualTable(0, 0, TABLE_NONE,
			(Integer i)->"",
			(String s)->0));
	}

/***********************************************************************************/

	private final String name;
	private final Function<Integer, String> translate;
	private final Function<String, Integer> retranslate;
	private final int minValue, maxValue;
	private int iteratorCount;

	public XGVirtualTable(int min, int max, String name, Function<Integer, String> translate, Function<String, Integer> retranslate)
	{	this.name = name;
		this.minValue = min;
		this.maxValue = max;
		this.translate = translate;
		this.retranslate = retranslate;
		this.iteratorCount = 0;
		LOG.info(this.getInfo());
	}

	@Override public XGTableEntry getByIndex(int i)
	{	i += this.minValue;
		String s = this.translate.apply(i);
		return new XGTableEntry(i, s);
	}

	@Override public XGTableEntry getByName(String name) throws NumberFormatException
	{	int v = this.retranslate.apply(name);
		return new XGTableEntry(v, name);
	}

	@Override public XGTableEntry getByValue(int v)
	{	v -= this.minValue;
		return new XGTableEntry(v, this.translate.apply(v));
	}

	@Override public int getIndex(int v){	return v - this.minValue;}

	@Override public int getIndex(String name){	return this.retranslate.apply(name) - this.minValue;}

	@Override public int getMinIndex(){	return 0;}

	@Override public int getMaxIndex(){	return this.size() - 1;}

	@Override public Iterator<XGTableEntry> iterator()
	{	return new Iterator<>()
		{
			@Override public boolean hasNext(){	return iteratorCount < maxValue;}
			@Override public XGTableEntry next(){	return getByIndex(iteratorCount++);}
		};
	}

	@Override public XGTable filter(XMLNode n){	return this;}

	@Override public XGTable filter(String filter){	return this;}

	@Override public int size(){	return (this.maxValue - this.minValue) + 1;}

	@Override public boolean containsValue(int value){	return value >= this.minValue && value <= this.maxValue;}

	@Override public String getName(){	return this.name;}

	@Override public String getUnit(){	return "";}

	@Override public String toString(){	return this.getInfo();}

	@Override public XGTable categorize(String cat){	return this;}

	@Override public Set<String> getCategories(){	return new HashSet<>();}
}
