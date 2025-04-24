package table;

import java.util.HashSet;
import java.util.Objects;import java.util.Set;
import application.XGStrings;
import xml.XMLNode;import xml.XMLNodeConstants;

public class XGTableEntry implements Comparable<XGTableEntry>
{

/**********************************************************************************************************/

	private final Integer value;
	private final String name;
	private final Set<String> filters;
	private final Set<String> categories;

	public XGTableEntry(XMLNode n)
	{
		this.value = n.getValueAttribute(XMLNodeConstants.ATTR_VALUE, 0);
		this.name = n.getStringAttributeOrDefault(XMLNodeConstants.ATTR_NAME, "no value");
		this.filters = XGStrings.splitCSV(n.getStringAttribute(XMLNodeConstants.ATTR_TABLEFILTER));
		this.categories = XGStrings.splitCSV(n.getStringAttribute(XMLNodeConstants.ATTR_TABLECATEGORIES));
		if(Boolean.parseBoolean(n.getStringAttribute(XMLNodeConstants.ATTR_INS_MSB))) XGTable.INS_MSB_PROGRAMS.add(this.value);
	}

	public XGTableEntry(int v, String translate)
	{	this.value = v;
		this.name = translate;
		this.filters = null;
		this.categories = new HashSet<>();
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

	@Override public boolean equals(Object e)
	{	if(e instanceof XGTableEntry) return Objects.equals(((XGTableEntry) e).value,this.value);
		return false;
	}
	
	@Override public String toString()
	{	return this.name;
	}

	public String getInfo()
	{	return XGStrings.valueToString(this.value) + "=" + this.name;
	}

	@Override public int compareTo(XGTableEntry entry)
	{	return this.value.compareTo(entry.value);
	}
}
