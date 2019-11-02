package parm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import application.ConfigurationConstants;
import application.Rest;
import device.XGDevice;
import xml.XMLNode;

public class XGTranslationMap implements ConfigurationConstants, XGTranslationConstants
{	
	private static final Logger log = Logger.getAnonymousLogger();
	private static Map<XGDevice, Map<String, XGTranslationMap>> STORAGE = new HashMap<>();

	public static void init(XGDevice dev)
	{	File file = dev.getResourceFile(XML_TRANSLATION);
		Map<String, XGTranslationMap> map = new HashMap<>();

		try
		{	XMLNode xml = XMLNode.parse(file);
			for(XMLNode x : xml.getChildren())
			{	if(x.getTag().equals(TAG_MAP))
				{	XGTranslationMap temp = new XGTranslationMap(x);
					for(String s : temp.getNames()) map.put(s, temp);
				}
			}
		}
		catch(XMLStreamException e1)
		{	e1.printStackTrace();
		}
		STORAGE.put(dev, map);
		log.info(dev + ": " + map.size() + " translations initialized");
	}

	public static String getTranslatedValue(XGDevice dev, String name, int key)
	{	return getTranslationMap(dev, name).getValue(key);
	}

	public static XGTranslationMap getTranslationMap(XGDevice dev,String name)
	{	return STORAGE.get(dev).get(name);
	}

/********************************************************************************************************/

	private final Map<Integer, String> map = new TreeMap<>();
	private final Set<String> names;

	private XGTranslationMap(XMLNode n)
	{	for(XMLNode e : n.getChildren())
		{	if(e.getTag().equals(TAG_ENTRY));
			{	this.map.put(e.parseChildNodeTextContent(TAG_KEY, 0), e.getChildNodeTextContent(TAG_VALUE, "unknown"));
			}
		}
		this.names = Rest.splitStringByComma(n.getAttribute(ATTR_NAME));
		log.info("translation initialized: " + this);
	}

	private Set<String> getNames()
	{	return this.names;
	}

	public String getValue(int value)
	{	return this.map.get(value);
	}

	public int getKey(String text)
	{	if(this.map.containsValue(text))
		{	for(Entry<Integer, String> e : this.map.entrySet())
			{	if(e.getValue().equals(text)) return e.getKey();
			}
		}
		return 0;
	}

	@Override public String toString()
	{	return names + " (" + this.map.size() + " entries)";
	}
}
