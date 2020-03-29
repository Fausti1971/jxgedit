package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import application.ConfigurationConstants;
import application.Rest;
import device.XGDevice;
import tag.XGTagable;
import tag.XGTagableSet;
import xml.XMLNode;

public class XGTranslationMap implements ConfigurationConstants, XGTranslationConstants, XGTagable
{	
	private static final Logger log = Logger.getAnonymousLogger();

	public static XGTagableSet<XGTranslationMap> init(XGDevice dev)
	{	XGTagableSet<XGTranslationMap> set = new XGTagableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_TRANSLATION);
		}
		catch(FileNotFoundException e)
		{	return null;
		}

		XMLNode xml = XMLNode.parse(file, null);
		for(XMLNode x : xml.getChildNodes())
		{	if(x.getTag().equals(TAG_MAP))
			{	for(String s : Rest.splitStringByComma(x.getStringAttribute(ATTR_NAME)))
					set.add(new XGTranslationMap(x, s));
			}
		}
		log.info(set.size() + " translations initialized");
		return set;
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

	private final Map<Integer, String> map = new TreeMap<>();
	private final String name;

	private XGTranslationMap(XMLNode n, String name)
	{	this.name = name;
		for(XMLNode e : n.getChildNodes())
		{	if(e.getTag().equals(TAG_ENTRY));
			{	this.map.put(e.parseChildNodeIntegerContent(TAG_KEY, 0), e.getChildNodeTextContent(TAG_VALUE, "no value"));
			}
		}
		log.info("translation initialized: " + this);
	}

	public String getValue(int value)
	{	return this.map.get(value);
	}

	public Collection<String> values()
	{	return this.map.values();
	}

	public int getKey(String text)
	{	if(this.map.containsValue(text))
		{	for(Entry<Integer, String> e : this.map.entrySet())
			{	if(e.getValue().equals(text)) return e.getKey();
			}
		}
		return 0;
	}

	public Collection<Integer> keys()
	{	return this.map.keySet();
	}

	public Set<Entry<Integer,String>> entrySet()
	{	return this.map.entrySet();
	}

	@Override public String toString()
	{	return this.name + " (" + this.map.size() + " entries)";
	}

	@Override public String getTag()
	{	return this.name;
	}
}
