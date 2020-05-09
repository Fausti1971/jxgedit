package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.logging.Logger;
import application.ConfigurationConstants;
import device.XGDevice;
import xml.XMLNode;

public class XGTable extends TreeMap<Integer, XGTableEntry> implements ConfigurationConstants
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getAnonymousLogger();

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
		{	String name = x.getStringAttribute(ATTR_NAME);
			dev.getTables().put(name, new XGTable(x));
			log.info("table initialized: " + name);
		}
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

	private final String name;
/**
 * lediglich eine Map von Integerwerten und dazugehörigen XMLNodes
 * @param n
 */
	private XGTable(XMLNode n)
	{	this.name = n.getStringAttribute(ATTR_NAME);
		XGTableEntry te;
		for(XMLNode e : n.getChildNodes(TAG_ITEM))
		{	te = new XGTableEntry(e);
			this.put(te.getKey(), te);
		}
	}

	private XGTable(String name)
	{	this.name = name;
	}

	@Override public XGTableEntry get(Object key)
	{	int i = (int)key;
		if(this.containsKey(i)) return super.get(i);
		if(this.containsKey(i & 0x3F80)) return super.get(i & 0x3F80);
		else return this.firstEntry().getValue();
	}

	public XGTable filter(XMLNode n)
	{	String f = n.getStringAttribute(ATTR_TABLEFILTER);
		int min = n.getIntegerAttribute(ATTR_MIN, XGParameterConstants.DEF_MIN);
		int max = n.getIntegerAttribute(ATTR_MAX, XGParameterConstants.DEF_MAX);
		XGTable table = new XGTable(this.name);
		for(XGTableEntry e : this.values())
			if(e.hasFilter(f) && e.getKey() >= min && e.getKey() <= max) table.put(e.getKey(), e);
		return table;
	}

	@Override public String toString()
	{	return "table " + this.name + " (" + this.size() + " entrys";
	}
}
