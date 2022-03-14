package parm;

import java.util.*;
import java.util.function.Function;
import application.XGMath;
import application.XGStrings;
import device.XGDevice;
import module.XGModule;import xml.XMLNode;

public class XGVirtualTable implements XGTable
{	private static final int MIN = 0, MAX = Integer.MAX_VALUE & 0x0000FFFF;
	private static final String[] KEY = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	public static final XGTable DEF_TABLE = new XGVirtualTable(MIN, MAX & 0x0000FFFF, DEF_TABLENAME, Object::toString, Integer::parseInt);

	public static void init()
	{	TABLES.add(DEF_TABLE);

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_ADD1,
			(Integer i)->{return String.format("%02d", i + 1);},
			(String s)->{return XGStrings.parseIntOrDefault(s, MIN + 1) - 1;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_DIV10,
			(Integer i)->{return Float.toString((float)i / 10);},
			(String s)->{float f = Float.parseFloat(s) * 10; return (int)f;}));

		TABLES.add
		(	new XGVirtualTable(MIN, MAX, TABLE_SUB64,
			(Integer i)->{return Integer.toString(i - 64);},
			(String s)->{return Integer.parseInt(s) + 64;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB1024DIV10,
			(Integer i)->{float f = (i.floatValue() - 1024) / 10; return Float.toString(f);},
			(String s)->{float f = Float.parseFloat(s) * 10 + 1024; return (int)f;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB128DIV10,
			(Integer i)->{float f = (i.floatValue() - 128) / 10; return Float.toString(f);},
			(String s)->{float f = Float.parseFloat(s) * 10 + 128; return (int)f;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB8k,
			(Integer i)->{return Integer.toString(i - 0x2040);},
			(String s)->{return Integer.parseInt(s) + 0x2040;}));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_PANORAMA,
			(Integer i)->
			{	if(i == 0) return "Rnd";
				if(i < 64) return "L" + Math.abs(i - 64);
				if(i > 64) return "R" + Math.abs(i - 64);
				else return "C";
			},
			XGStrings::toNumber));

		TABLES.add(new XGVirtualTable(4, 124, TABLE_DEGREES,
			(Integer i)->{	return Integer.toString(XGMath.linearIO(i, 4, 124, -180, 180)) + "°";},
			XGStrings::toNumber));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_PERCENT,
			(Integer i)->{	return Integer.toString(XGMath.linearIO(i, 0, 127, 0, 100)) + "%";},
			(String s)->{	return XGMath.linearIO(XGStrings.toNumber(s), 0, 100, 0, 127);}));

		TABLES.add(new XGVirtualTable(0,127, TABLE_KEYS,
			(Integer i)->
			{	String s = KEY[i % 12] + (i/12 - 2);
				return s;
			},
			(String s)->
			{	int o = (XGStrings.toNumber(s) + 2) * 12;
				int k = Arrays.binarySearch(KEY, XGStrings.toAlpha(s));
				return o + k;
			}));

		TABLES.add(new XGVirtualTable(0, 0, TABLE_NONE,
			(Integer i)->{return "";},
			(String s)->{return 0;}));
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

	@Override public XGTableEntry getByName(String name)
	{	int v = this.retranslate.apply(name);
		return new XGTableEntry(v, name);
	}

	@Override public XGTableEntry getByValue(int v)
	{	v -= this.minValue;
		return new XGTableEntry(v, this.translate.apply(v));
	}

	@Override public int getIndex(int v)
	{	return v - this.minValue;
	}

	@Override public int getIndex(String name)
	{	return this.retranslate.apply(name) - this.minValue;
	}

	@Override public int getMinIndex()
	{	return 0;
	}

	@Override public int getMaxIndex()
	{	return this.size() - 1;
	}

	@Override public Iterator<XGTableEntry> iterator()
	{	return new Iterator<>()
		{
			@Override public boolean hasNext()
			{	return iteratorCount < maxValue;
			}

			@Override public XGTableEntry next()
			{	return getByIndex(iteratorCount++);
			}
		};
	}

	@Override public XGTable filter(XMLNode n)
	{	return this;
	}

	@Override public int size()
	{	return (this.maxValue - this.minValue) + 1;
	}

	@Override public String getName()
	{	return this.name;
	}

	@Override public String getUnit()
	{	return "";
	}

	@Override public String toString()
	{	return this.getInfo();
	}

	@Override public XGTable categorize(String cat)
	{	return this;
	}

	@Override public Set<String> getCategories()
	{	return new HashSet<>();
	}

}
