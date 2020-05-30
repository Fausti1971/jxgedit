package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.TreeMap;
import application.ConfigurationConstants;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public class XGTable implements ConfigurationConstants, XGLoggable, XGParameterConstants, XGTagable, Iterable<XGTableEntry>
{	
	public static void init(XGDevice dev)
	{
		File file;
		try
		{	file = dev.getResourceFile(XML_TABLES);
		}
		catch(FileNotFoundException e)
		{	return;
		}

		XMLNode xml = XMLNode.parse(file);
		for(XMLNode x : xml.getChildNodes(TAG_TABLE))
		{	dev.getTables().add(new XGTable(x));
		}
		XGVirtualTable.init(dev);
		return;
	}
/*
	public static String getTranslatedValue(XGDevice dev, String name, int key)
	{	return getTranslationMap(dev, name).getValue(key);
	}

	public static XGTranslationMap getTranslationMap(XGDevice dev,String name)
	{	return STORAGE.get(dev).get(name);
	}
*/
/********************************************************************************************************/

	protected final String name;
	private final String unit;
	private final TreeMap<Integer, XGTableEntry> map = new TreeMap<>();
/**
 * lediglich eine Map von Integerwerten und dazugehörigen XMLNodes
 * @param n
 */
	private XGTable(XMLNode n)
	{	this.name = n.getStringAttribute(ATTR_NAME);
		this.unit = n.getStringAttribute(ATTR_UNIT, "");
		XGTableEntry te;
		for(XMLNode e : n.getChildNodes(TAG_ITEM))
		{	te = new XGTableEntry(e);
			this.map.put(te.getKey(), te);
		}
		log.info("table initialized: " + this.name + " (" + this.size() + ")");
	}

	XGTable(String name)
	{	this.name = name;
		this.unit = "";
	}

	public XGTableEntry get(Integer key)
	{	if(this.map.containsKey(key)) return this.map.get(key);
		if(this.map.containsKey(key & 0x3F80)) return this.map.get(key & 0x3F80);
		else return this.map.firstEntry().getValue();
	}

	public XGTableEntry get(String name)
	{	for(XGTableEntry e : this.map.values()) if(e.getName().equals(name)) return e;
		return this.map.firstEntry().getValue();
	}
	

	public XGTable filter(XMLNode n)
	{	String f = n.getStringAttribute(ATTR_TABLEFILTER);
		if(f == null) return this;
		int min = n.getIntegerAttribute(ATTR_MIN, XGParameterConstants.DEF_MIN);
		int max = n.getIntegerAttribute(ATTR_MAX, XGParameterConstants.DEF_MAX);
		XGTable table = new XGTable(this.name + "-" + f);
		for(XGTableEntry e : this.map.subMap(min, true, max, true).values())
			if(e.hasFilter(f)) table.map.put(e.getKey(), e);
		return table;
	}

	public String getName()
	{	return this.name;
	}

	public String getUnit()
	{	return this.unit;
	}

	@Override public String toString()
	{	return "table " + this.name + " (" + this.map.size() + " entrys";
	}

	@Override public String getTag()
	{	return this.name;
	}

	@Override public Iterator<XGTableEntry> iterator()
	{	return this.map.values().iterator();
	}

	public Integer nextKey(int i)
	{	Integer o = this.map.higherKey(i);
		if(o == null) return this.lastKey();
		else return o;
	}

	public Integer prevKey(int i)
	{	Integer o = this.map.lowerKey(i);
		if(o == null) return this.firstKey();
		else return o;
	}

	public int firstKey()
	{	return this.map.firstKey();
	}

	public int lastKey()
	{	return this.map.lastKey();
	}

	public int size()
	{	return this.map.size();
	}
}
