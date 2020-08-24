package parm;

import java.io.File;
import java.io.FileNotFoundException;
import application.ConfigurationConstants;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public interface XGTable extends ConfigurationConstants, XGLoggable, XGParameterConstants, XGTagable, Iterable<XGTableEntry>
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
		{	dev.getTables().add(new XGXMLTable(x));
		}
		XGVirtualTable.init(dev);
		return;
	}

/*************************************************************************************************************/

	public XGTableEntry getByIndex(int i);
	public XGTableEntry getByValue(int v);
	public XGTableEntry getByName(String name);
	public int getIndex(int v);
	public int getIndex(String name);
	public XGTable filter(XMLNode n);
	public String getName();
	public String getUnit();
	public int size();

	public default XGTableEntry getMinEntry()
	{	return this.getByIndex(this.getMinIndex());
	}

	public default XGTableEntry getMaxEntry()
	{	return this.getByIndex(this.getMaxIndex());
	}

	@Override public default String getTag()
	{	return this.getName();
	}

	public default String getInfo()
	{	return this.getClass().getSimpleName()
			+ ": " + this.getName()
			+ " (" + this.size()
			+ "); min=" + this.getMinEntry().getValue() + "(" + this.getMinEntry()
			+ "); max=" + this.getMaxEntry().getValue() + "(" + this.getMaxEntry() + ")";
	}

	public default int getMinIndex()
	{	return 0;
	}

	public default int getMaxIndex()
	{	return this.size() - 1;
	}
}
