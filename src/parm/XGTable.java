package parm;

import java.io.*;
import java.util.Set;
import application.*;
import tag.*;
import xml.XMLNode;

public interface XGTable extends ConfigurationConstants, XGLoggable, XGParameterConstants, XGTagable, Iterable<XGTableEntry>
{
	public XGTagableSet<XGTable> TABLES = new XGTagableSet<>();
	static int DEF_FALLBACKMASK = 127;
	static enum Preference{BELOW, EQUAL, ABOVE, CLOSEST, FALLBACK};

	public static void init()
	{	try
		{	XMLNode xml = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_TABLES));
			for(XMLNode x : xml.getChildNodes(TAG_TABLE))
			{	TABLES.add(new XGXMLTable(x));
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
		XGVirtualTable.init();
		return;
	}

/*************************************************************************************************************/

	public XGTableEntry getByIndex(int i);
	public XGTableEntry getByValue(int v);
	public XGTableEntry getByName(String name);
	public int getIndex(int v, Preference pref);
	public int getIndex(String name);
	public XGTable categorize(String cat);
	public Set<String> getCategories();
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
	{	return this.getClass().getSimpleName() + ": " + this.getName() + "(" + this.size() + "): " + this.getMinEntry().getInfo() + "..." + this.getMaxEntry().getInfo();
	}

	public default int getMinIndex()
	{	return 0;
	}

	public default int getMaxIndex()
	{	return this.size() - 1;
	}
}
