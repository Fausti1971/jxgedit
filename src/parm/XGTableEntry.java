package parm;

import java.util.Set;
import application.ConfigurationConstants;
import application.XGStrings;
import xml.XMLNode;

public class XGTableEntry implements ConfigurationConstants
{

/**********************************************************************************************************/

	private final int value;
	private final String name;
	private final Set<String> filters;
	private final Set<String> categories;

	public XGTableEntry(XMLNode n)
	{
		this.value = n.getValueAttribute(ATTR_VALUE, 0);
		this.name = n.getStringAttributeOrDefault(ATTR_NAME, "no value");
		this.filters = XGStrings.splitCSV(n.getStringAttribute(ATTR_TABLEFILTER));
		this.categories = XGStrings.splitCSV(n.getStringAttribute(ATTR_TABLECATEGORIES));
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

	public boolean hasCategory(String cat)
	{	if(cat == null || cat.isEmpty()) return true;
		for(String f : this.categories)
		{	if(f.equals(cat)) return true;
		}
		return false;
	}

	public boolean hasFilter(String s)
	{	if(s == null || s.isEmpty()) return true;
		for(String f : this.filters)
		{	if(f.equals(s)) return true;
		}
		return false;
	}

	@Override public String toString()
	{	return this.name;
	}

	public String getInfo()
	{	return this.value + "(" + this.name + ")";
	}
}
