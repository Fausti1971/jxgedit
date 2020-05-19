package parm;

import java.util.Set;
import application.ConfigurationConstants;
import application.Rest;
import xml.XMLNode;

public class XGTableEntry implements ConfigurationConstants
{

/**********************************************************************************************************/

	private final int key;
	private final String name;
	private final Set<String> filters;

	public XGTableEntry(XMLNode n)
	{	int i = 0;
		if(n.hasAttribute(ATTR_VALUE)) i = n.getIntegerAttribute(ATTR_VALUE);
		if(n.hasAttribute(ATTR_MSB)) i = n.getIntegerAttribute(ATTR_MSB) << 7;
		if(n.hasAttribute(ATTR_LSB)) i |= n.getIntegerAttribute(ATTR_LSB);
		this.key = i;
		this.name = n.getStringAttribute(ATTR_NAME, "no value");
		this.filters = Rest.splitCSV(n.getStringAttribute(ATTR_TABLEFILTER));
	}

	public XGTableEntry(int i, String translate)
	{	this.key = i;
		this.name = translate;
		this.filters = null;
	}

	public int getKey()
	{	return this.key;
	}

	public String getName()
	{	return this.name;
	}

	public boolean hasFilter(String s)
	{	for(String f : this.filters)
		{	if(f.equals(s)) return true;
		}
		return false;
	}

	@Override public String toString()
	{	return this.getName();
	}
}
