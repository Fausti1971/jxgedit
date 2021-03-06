package parm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import application.XGMath;
import application.XGStrings;
import device.XGDevice;
import xml.XMLNode;

public class XGVirtualTable implements XGTable
{	private static final int MIN = 0, MAX = Integer.MAX_VALUE & 0x0000FFFF;

	static final XGTable DEF_TABLE = new XGVirtualTable(MIN, MAX & 0x0000FFFF, DEF_TABLENAME,
		(Integer i)->{return i.toString();},
		(String s)->{return Integer.parseInt(s);});

	public static void init()
	{	TABLES.add(DEF_TABLE);

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_ADD1,
			(Integer i)->{return Integer.toString(i + 1);},
			(String s)->{return XGStrings.parseIntOrDefault(s, MIN + 1) - 1;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_DIV10,
			(Integer i)->{return Float.toString((float)i / 10);},
			(String s)->{Float f = Float.parseFloat(s) * 10; return f.intValue();}));

		TABLES.add
		(	new XGVirtualTable(MIN, MAX, TABLE_SUB64,
			(Integer i)->{return Integer.toString(i - 64);},
			(String s)->{return Integer.parseInt(s) + 64;}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB1024DIV10,
			(Integer i)->{Float f = (i.floatValue() - 1024) / 10; return f.toString();},
			(String s)->{Float f = Float.parseFloat(s) * 10 + 1024; return f.intValue();}));

		TABLES.add(new XGVirtualTable(MIN, MAX, TABLE_SUB128DIV10,
			(Integer i)->{Float f = (i.floatValue() - 128) / 10; return f.toString();},
			(String s)->{Float f = Float.parseFloat(s) * 10 + 128; return f.intValue();}));

		TABLES.add(new XGVirtualTable(0, 127, TABLE_PANORAMA,
			(Integer i)->{if(i == 0) return "Rnd"; if(i < 64) return "L" + Math.abs(i - 64); if(i > 64) return "R" + Math.abs(i - 64); else return "C";},
			(String s)->{return Integer.parseInt(s);}));

		TABLES.add(new XGVirtualTable(4, 124, TABLE_DEGREES,
			(Integer i)->{	return Integer.toString(XGMath.linearIO(i, 4, 124, -180, 180));},
			(String s)->{	return Integer.parseInt(s);}));

		TABLES.add(new XGVirtualTable(0, 0, TABLE_NONE,
			(Integer i)->{return "";},
			(String s)->{return 0;}));

//TODO: weiter so...

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

	@Override public int getIndex(int v, Preference pref)
	{	return v - this.minValue;
	}

	@Override public int getIndex(String name)
	{	return this.retranslate.apply(name) - this.minValue;
	}

	@Override public Iterator<XGTableEntry> iterator()
	{	return new Iterator<XGTableEntry>()
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
