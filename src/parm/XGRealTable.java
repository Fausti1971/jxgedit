package parm;

import java.util.*;
import application.XGStrings;import xml.XMLNode;

public class XGRealTable implements XGTable
{	private static final String ALL_CATEGORIES = "All";

/********************************************************************************************************/

	private final String name;
	private final String unit;
	private final int fallbackMask;
	private final boolean sort;
	private final List<XGTableEntry> entries = new ArrayList<>();//entry
/**
* key=(int)value; value=(int)index (in entries)
*/
	private final NavigableMap<Integer, Integer> indexes = new TreeMap<>();//value, index
/**
* key=(String)name; value=(int)index (in entries);
*/
	private final Map<String, Integer> names = new HashMap<>();//name, index
	private final Set<String> categories = new LinkedHashSet<>();

/**
 * lediglich eine indizierte Map von Integerwerten und dazugehörigen XMLNodes
 * @param n
 */
	XGRealTable(XMLNode n)
	{	this(n.getStringAttribute(ATTR_NAME), n.getStringAttributeOrDefault(ATTR_UNIT, ""), n.getValueAttribute(ATTR_FALLBACKMASK, DEF_FALLBACKMASK), false);
		n.traverse(TAG_ITEM, (XMLNode x)->this.add(new XGTableEntry(x)));
		LOG.info(this.getInfo());
	}

	public XGRealTable(String name, String unit, int fbm, boolean sortByValue)
	{	this.name = name;
		this.unit = unit;
		this.fallbackMask = fbm;
		this.sort = sortByValue;
		this.categories.add(ALL_CATEGORIES);
//		LOG.info(this.name);
	}

	public XGRealTable(String name){	this(name, "", DEF_FALLBACKMASK, true);}

/**
* Die Indizierung der Entries erfolgt aufsteigend nach e.value
**/
	public void add(XGTableEntry e)
	{	this.entries.add(e);
		int i = this.entries.indexOf(e);
		this.indexes.put(e.getValue(), i);
		this.names.put(e.getName(), i);

		if(this.sort) this.sortByValue();

		this.categories.addAll(e.getCategories());

		int is = this.indexes.size();
		int es = this.entries.size();
		int ns = this.names.size();
		if(es != is)
		{	System.out.println(this.entries);
			System.out.println(this.indexes);
			throw new RuntimeException("sizes mismatch on adding " + e.getInfo() + " to " + this.getInfo() + ": entries=" + es + ", indexes=" + is + ", names=" + ns);
		}
	}

	private void sortByValue()
	{	this.indexes.clear();
		this.names.clear();
		this.entries.sort(null);
		for(XGTableEntry n : this.entries)
		{	int i = this.entries.indexOf(n);
			this.indexes.put(n.getValue(), i);
			this.names.put(n.getName(), i);
		}
	}

	@Override public XGTableEntry getByIndex(int i)
	{	if(this.entries.isEmpty()) throw new IndexOutOfBoundsException("index " + i + " out of bounds in " + this.getName());
		return this.entries.get(Math.min(this.getMaxIndex(), Math.max(0, i)));
	}

	public XGTableEntry getByValue(int v, XGTableEntry def)
	{	if(this.indexes.containsKey(v)) return this.entries.get(this.indexes.get(v));
		v &= this.fallbackMask;
		if(this.indexes.containsKey(v)) return this.entries.get(this.indexes.get(v));
		else return def;
	}

	@Override public XGTableEntry getByValue(int v){	return this.getByValue(v, new XGTableEntry(v, "**" + XGStrings.valueToString(v) + "**"));}

	@Override public XGTableEntry getByName(String name) throws NumberFormatException
	{	if(this.names.containsKey(name)) return this.entries.get(this.names.get(name));
		else return this.getByValue(Integer.parseInt(name));
	}

	@Override public int getIndex(int v)
	{	if(this.indexes.containsKey(v)) return this.indexes.get(v);
		int f = v & this.fallbackMask;
		if(f != v && this.indexes.containsKey(f)) return this.indexes.get(f);
		LOG.warning("neither value " + XGStrings.valueToString(v) + " nor fallback " + XGStrings.valueToString(f) + " found in " + this);
		return this.getMinIndex();
	}

	@Override public int getIndex(String name)
	{	if(this.names.containsKey(name)) return this.names.get(name);
		else return this.entries.indexOf(this.getByName(name));
	}

	@Override public int getMinIndex(){	return 0;}

	@Override public int getMaxIndex(){	return this.size() - 1;}

	@Override public Set<String> getCategories(){	return this.categories;}

	@Override public XGTable categorize(String cat)
	{	if(ALL_CATEGORIES.equals(cat)) return this;
		XGRealTable table = new XGRealTable(this.name + "-" + cat, this.unit, this.fallbackMask, this.sort);
		for(XGTableEntry e : this) if(e.hasCategory(cat)) table.add(e);
		return table;
	}

	@Override public XGRealTable filter(XMLNode n)
	{	if(!n.getAttributes().containsKey(ATTR_TABLEFILTER)) return this;
		String f = n.getStringAttribute(ATTR_TABLEFILTER);
		XGRealTable table = new XGRealTable(this.name + "-" + f, this.unit, this.fallbackMask, this.sort);
		for(XGTableEntry e : this) if(e.hasFilter(f)) table.add(e);
		return table;
	}

	@Override public String getName(){	return this.name;}

	@Override public String getUnit(){	return this.unit;}

	@Override public String toString(){	return this.getInfo();}

	@Override public Iterator<XGTableEntry> iterator(){	return this.entries.iterator();}

	@Override public int size(){	return this.entries.size();}
}
