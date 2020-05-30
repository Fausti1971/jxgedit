package parm;

import java.util.Iterator;
import java.util.function.Function;
import device.XGDevice;
import xml.XMLNode;

public class XGVirtualTable extends XGTable
{
	public static void init(XGDevice dev)
	{	dev.getTables().add(DEF_TABLE);

		dev.getTables().add
			(new XGVirtualTable
				(TABLE_ADD1,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	return Integer.toString(t + 1);
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	return Integer.parseInt(t) - 1;
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(TABLE_DIV10,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	return Float.toString(t / 10);
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	Float f = Float.parseFloat(t) * 10;
							return f.intValue();
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(TABLE_SUB64,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	return Integer.toString(t - 64);
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	return Integer.parseInt(t) + 64;
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(TABLE_SUB1024DIV10,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	Float f = (t.floatValue() - 1024) / 10;
							return f.toString();
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	Float f = Float.parseFloat(t) * 10 + 1024;
							return f.intValue();
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(TABLE_SUB128DIV10,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	Float f = (t.floatValue() - 128) / 10;
							return f.toString();
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	Float f = Float.parseFloat(t) * 10 + 128;
							return f.intValue();
						};
					}
				)
			);


//TODO: weiter so...

	}

/***********************************************************************************/

	private final Function<Integer, String> translate;
	private Function<String, Integer> retranslate;
	private int iteratorCount;

	public XGVirtualTable(String name, Function<Integer, String> trans, Function<String, Integer> retrans)
	{	super(name);
		this.translate = trans;
		this.retranslate = retrans;
		this.iteratorCount = this.firstKey();
		log.info("table initialized: " + this.name + " (" + this.size() + ")");
	}

	@Override public XGTableEntry get(Integer key)
	{	String s = this.translate.apply(key);
		return new XGTableEntry(key, s);
	}

	@Override public XGTableEntry get(String name)
	{	int i = this.retranslate.apply(name);
		return new XGTableEntry(i, name);
	}

	@Override public Iterator<XGTableEntry> iterator()
	{	return new Iterator<XGTableEntry>()
		{
			@Override public boolean hasNext()
			{	return iteratorCount < lastKey();
			}

			@Override public XGTableEntry next()
			{	return get(iteratorCount++);
			}
		};
	}

	@Override public XGTable filter(XMLNode n)
	{	return this;
	}

	@Override public int firstKey()
	{	return 0;
	}

	@Override public int lastKey()
	{	return Integer.MAX_VALUE;
	}

	@Override public Integer nextKey(int i)
	{	return Math.min(i + 1, this.lastKey());
	}

	@Override public Integer prevKey(int i)
	{	return Math.max(i - 1, this.firstKey());
	}

	@Override public int size()
	{	return this.lastKey() - this.firstKey();
	}
}
