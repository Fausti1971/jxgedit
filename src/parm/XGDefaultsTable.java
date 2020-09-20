package parm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public class XGDefaultsTable extends HashMap<Integer, Integer> implements XGParameterConstants, XGLoggable, XGTagable
{
	private static final long serialVersionUID = 1L;

	public static void init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_DEFAULT);
			XMLNode n = XMLNode.parse(file);
			for(XMLNode t : n.getChildNodes(TAG_DEFAULTTABLE))
			{	dev.getDefaultsTables().add(new XGDefaultsTable(t));
			}
		}
		catch(FileNotFoundException e)
		{	LOG.info(e.getMessage());
		}
	}

/*************************************************************************************************************/

	private final String tag;

	public XGDefaultsTable(XMLNode n)
	{	this.tag = n.getStringAttribute(ATTR_NAME);
		for(XMLNode d : n.getChildNodes(TAG_DEFAULT))
		{	this.put(d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_DEFAULT, 0));
		}
	}

	@Override public String getTag()
	{	return this.tag;
	}

}
