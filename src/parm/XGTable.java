package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.logging.Logger;
import application.ConfigurationConstants;
import device.XGDevice;
import xml.XMLNode;

public class XGTable extends TreeMap<Integer, String> implements ConfigurationConstants
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

	private XGTable(XMLNode n)
	{	for(XMLNode e : n.getChildNodes(TAG_ITEM))
		{	this.put(e.getIntegerAttribute(ATTR_VALUE, 0), e.getStringAttribute(ATTR_NAME, "no value"));
		}
	}
}
