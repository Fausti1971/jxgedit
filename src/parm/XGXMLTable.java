package parm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import xml.XMLNode;

public class XGXMLTable implements XGTable
{

/********************************************************************************************************/

	protected final String name;
	private final String unit;
	private final ArrayList<XGTableEntry> list = new ArrayList<>();
	private final Map<Integer, Integer> indexes = new HashMap<>();//value, index
	private final Map<String, Integer> names = new HashMap<>();//name, index
/**
 * lediglich eine Map von Integerwerten und dazugehörigen XMLNodes
 * @param n
 */
	XGXMLTable(XMLNode n)
	{	this.name = n.getStringAttribute(ATTR_NAME);
		this.unit = n.getStringAttribute(ATTR_UNIT, "");
		int i = 0;
		for(XMLNode e : n.getChildNodes(TAG_ITEM))
		{	this.add(i++, new XGTableEntry(e));
		}
		LOG.info(this.getInfo());
	}

	XGXMLTable(String name, String unit)
	{	this.name = name;
		this.unit = unit;
		LOG.info(this.name);
	}

	private void add(int i, XGTableEntry e)
	{	this.list.add(i, e);
		this.indexes.put(e.getValue(), i);
		this.names.put(e.getName(), i);
	}
	
	@Override public XGTableEntry getByIndex(int i)
	{	return this.list.get(Math.min(this.list.size() - 1, Math.max(0, i)));
	}

	@Override public XGTableEntry getByValue(int v)
	{	return this.list.get(this.indexes.get(v));
	}

	@Override public XGTableEntry getByName(String name)
	{	return this.list.get(this.names.get(name));
	}

	@Override public int getIndex(int v)
	{	if(this.indexes.containsKey(v)) return(this.indexes.get(v));
		if(v > this.getMaxEntry().getValue()) return this.getMaxIndex();
		else return this.getMinIndex();
	}

	@Override public int getIndex(String name)
	{	return this.names.get(name);
	}

	@Override public XGXMLTable filter(XMLNode n)
	{	String f = n.getStringAttribute(ATTR_TABLEFILTER);
		int minValue = n.getIntegerAttribute(ATTR_MIN, this.getMinEntry().getValue());
		int maxValue = n.getIntegerAttribute(ATTR_MAX, this.getMaxEntry().getValue());
		XGXMLTable table = new XGXMLTable(this.name + "-" + f, this.unit);
		int i = 0;
		for(XGTableEntry e : this.list.subList(this.getIndex(minValue), this.getIndex(maxValue) + 1))
			if(e.hasFilter(f)) table.add(i++, e);
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
