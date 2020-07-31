package parm;

import java.util.Iterator;
import java.util.function.Function;
import application.Rest;
import device.XGDevice;
import xml.XMLNode;

public class XGVirtualTable implements XGTable
{	private static final int MIN = 0, MAX = Integer.MAX_VALUE & 0x0000FFFF;

	static final XGTable DEF_TABLE = new XGVirtualTable
		(MIN, MAX & 0x0000FFFF, DEF_TABLENAME,
			new Function<Integer, String>()
			{	@Override public String apply(Integer t)
				{	return t.toString();
				};
			},
			new Function<String, Integer>()
			{	@Override public Integer apply(String s)
				{	return Integer.parseInt(s);
				}
			});

	public static void init(XGDevice dev)
	{	dev.getTables().add(DEF_TABLE);

		dev.getTables().add
			(new XGVirtualTable
				(MIN, MAX, TABLE_ADD1,
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
				(MIN, MAX, TABLE_DIV10,
					new Function<Integer,  String>()
					{	@Override public String apply(Integer t)
						{	return Float.toString((float)t / 10);
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
				(MIN, MAX, TABLE_SUB64,
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
				(MIN, MAX, TABLE_SUB1024DIV10,
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
				(MIN, MAX, TABLE_SUB128DIV10,
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

		dev.getTables().add
			(new XGVirtualTable
				(0, 127, TABLE_PANORAMA,
					new Function<Integer, String>()
					{	@Override public String apply(Integer t)
						{	if(t == 0) return "Rnd";
							if(t < 64) return "L" + Math.abs(t - 64);
							if(t > 64) return "R" + Math.abs(t - 64);
							else return "C";
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	return Integer.parseInt(t);//TODO:
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(4, 124, TABLE_DEGREES,
					new Function<Integer, String>()
					{	@Override public String apply(Integer t)
						{	return Integer.toString(Rest.linearIO(t, 4, 124, -180, 180));
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	return Integer.parseInt(t);//TODO:
						};
					}
				)
			);

		dev.getTables().add
			(new XGVirtualTable
				(0, 0, TABLE_NONE,
					new Function<Integer, String>()
					{	@Override public String apply(Integer t)
						{	return "";
						};
					},
					new Function<String, Integer>()
					{	@Override public Integer apply(String t)
						{	return 0;
						};
					}
				)
			);

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
		log.info(this.getInfo());
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
	{	int min = n.getIntegerAttribute(ATTR_MIN, XGParameterConstants.DEF_MIN);
		int max = n.getIntegerAttribute(ATTR_MAX, XGParameterConstants.DEF_MAX);
		return new XGVirtualTable(min, max, this.name, this.translate, this.retranslate);
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

}
