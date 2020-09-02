package parm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import xml.XMLNode;

public class XGXMLTable implements XGTable
{	private static final String ALL_CATEGORIES = "All";

/********************************************************************************************************/

	protected final String name;
	private final String unit;
	private final int fallbackMask;
	private final ArrayList<XGTableEntry> list = new ArrayList<>();//entry
	private final NavigableMap<Integer, Integer> indexes = new TreeMap<>();//value, index
	private final Map<String, Integer> names = new HashMap<>();//name, index
	private final Map<String, XGXMLTable> categories = new LinkedHashMap<>();
//	private final Set<String> categoryNames = new LinkedHashSet<>();
/**
 * lediglich eine indizierte Map von Integerwerten und dazugehörigen XMLNodes
 * @param n
 */
	XGXMLTable(XMLNode n)
	{	this.name = n.getStringAttribute(ATTR_NAME);
		this.unit = n.getStringAttributeOrDefault(ATTR_UNIT, "");
		this.fallbackMask = n.getValueAttribute(ATTR_FALLBACKMASK, DEF_FALLBACKMASK);
		this.categories.put(ALL_CATEGORIES, this);
		n.traverse(TAG_ITEM, (XMLNode x)->{this.add(new XGTableEntry(x));});
		LOG.info(this.getInfo());
	}

	XGXMLTable(String name, String unit, int fbm)
	{	this.name = name;
		this.unit = unit;
		this.fallbackMask = fbm;
		this.categories.put(ALL_CATEGORIES, this);
//		LOG.info(this.name);
	}

	private void add(XGTableEntry e)
	{	this.list.add(e);
		int i = this.list.indexOf(e);
		this.indexes.put(e.getValue(), i);
		this.names.put(e.getName(), i);
		for(String s : e.getCategories()) this.categories.put(s, new XGXMLTable(this.name, this.unit, this.fallbackMask));
	}

	private int findValue(int v, Preference pref)
	{	if(this.indexes.containsKey(v)) return v;
		v = Math.max(this.indexes.firstKey(), Math.min(v, this.indexes.lastKey()));
		int above = this.indexes.ceilingKey(v);
		int below = this.indexes.floorKey(v);
		switch(pref)
		{	case CLOSEST:	return (v - below > above - v ? above : below);
			case EQUAL:		return v;
			case FALLBACK:	return this.getFallbackValue(v);
			case ABOVE:		return above;
			case BELOW:		return below;
			default:		return v;
		}
	}

	private int getFallbackValue(int v)
	{	v &= this.fallbackMask;
		if(this.indexes.containsKey(v)) return v;
		else return this.list.get(this.getMinIndex()).getValue();
	}

	@Override public XGTableEntry getByIndex(int i)
	{	if(this.list.isEmpty()) throw new IndexOutOfBoundsException("index " + i + " out of bounds in " + this.getName());
		return this.list.get(Math.min(this.getMaxIndex(), Math.max(0, i)));
	}

	@Override public XGTableEntry getByValue(int v)
	{	return this.list.get(this.indexes.get(v));
	}

	@Override public XGTableEntry getByName(String name)
	{	return this.list.get(this.names.get(name));
	}

	@Override public int getIndex(int v, Preference pref)
	{	int i = this.findValue(v, pref);
		try
		{	return this.indexes.get(i);
		}
		catch(NullPointerException e)
		{	LOG.warning("neither value " + v + " nor " + pref.name() + " value " + i  + " doesn´t exist in " + this);
		}
		return this.getMinIndex();
	}

	@Override public int getIndex(String name)
	{	return this.names.get(name);
	}

	@Override public Set<String> getCategories()
	{	return this.categories.keySet();
	}

	@Override public XGTable categorize(String cat)
	{	XGXMLTable table = this.categories.get(cat);
		if(table == null)
		{	table = new XGXMLTable(this.name + "-" + cat, this.unit, this.fallbackMask);
			this.categories.put(cat, table);
		}
		if(table.size() != 0) return table;
		for(XGTableEntry e : this) if(e.hasCategory(cat)) table.add(e);
		return table;
	}

	@Override public XGXMLTable filter(XMLNode n)
	{	if(!n.hasAttribute(ATTR_TABLEFILTER)) return this;
		String f = n.getStringAttribute(ATTR_TABLEFILTER);
		XGXMLTable table = new XGXMLTable(this.name + "-" + f, this.unit, this.fallbackMask);
		for(XGTableEntry e : this) if(e.hasFilter(f)) table.add(e);
		return table;
	}

	@Override public String getName()
	{	return this.name;
	}

	@Override public String getUnit()
	{	return this.unit;
	}

	@Override public String toString()
	{	return this.getInfo();
	}

	@Override public Iterator<XGTableEntry> iterator()
	{	return this.list.iterator();
	}

	@Override public int size()
	{	return this.list.size();
	}
}
