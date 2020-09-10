package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import application.XGLoggable;
import device.XGDevice;
import xml.XMLNode;
import xml.XMLNodeConstants;

public interface XGDefaults extends XGLoggable, XMLNodeConstants, XGParameterConstants
{	static XMLNode init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_DEFAULT);
		}
		catch(FileNotFoundException e)
		{	LOG.info(e.getMessage());
			return new XMLNode("defaultTables");
		}
		return XMLNode.parse(file);
	}

	static Map<Integer, Integer> getDefaultsTable(XGDevice dev, XMLNode n)
	{	Map<Integer, Integer> map = new HashMap<>();
		int selValue = DEF_SELECTORVALUE, value = 0;
		if(n.hasAttribute(ATTR_DEFAULTS))
		{	String tableName = n.getStringAttribute(ATTR_DEFAULTS);
			for(XMLNode x : dev.getDefaultsTables().getChildNodes(tableName))
			{	selValue = x.getIntegerAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE);
				value = x.getIntegerAttribute(ATTR_VALUE, 0);
				map.put(selValue, value);
			}
		}
		else
		{	map.put(DEF_SELECTORVALUE, n.getIntegerAttribute(ATTR_DEFAULT, 0));
		}
		return map;
	}
}
