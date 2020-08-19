package parm;

import java.util.Set;
import application.ConfigurationConstants;
import application.XGStrings;
import jdk.internal.joptsimple.internal.Strings;
import xml.XMLNode;

public class XGTableEntry implements ConfigurationConstants
{

/**********************************************************************************************************/

	private final int value;
	private final String name;
	private final Set<String> filters;
	private final Set<String> categories;

	public XGTableEntry(XMLNode n)
	{	int v = 0;
		if(n.hasAttribute(ATTR_VALUE)) v = n.getIntegerAttribute(ATTR_VALUE);
		if(n.hasAttribute(ATTR_MSB)) v = n.getIntegerAttribute(ATTR_MSB) << 7;
		if(n.hasAttribute(ATTR_LSB)) v |= n.getIntegerAttribute(ATTR_LSB);
		this.value = v;
		this.name = n.getStringAttribute(ATTR_NAME, "no value").toString();
		this.filters = XGStrings.splitCSV(n.getStringAttribute(ATTR_TABLEFILTER).toString());
		this.categories = XGStrings.splitCSV(n.getStringAttribute(ATTR_TABLECATEGORIES).toString());
	}

	public XGTableEntry(int v, String translate)
	{	this.value = v;
		this.name = translate;
		this.filters = null;
		this.categories = null;
	}

	public int getValue()
	{	return this.value;
	}

	public String getName()
	{	return this.name;
	}

	public Set<String> getCategories()
	{	return this.categories;
	}

	public boolean hasFilter(String s)
	{	if(s == null || s.isBlank()) return true;
		for(String f : this.filters)
		{	if(f.equals(s)) return true;
		}
		return false;
	}

	@Override public String toString()
	{	return this.name;
	}
}
