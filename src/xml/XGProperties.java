package xml;

import java.util.LinkedHashMap;
import application.XGStrings;

public class XGProperties extends LinkedHashMap<String, StringBuffer>
{
	private static final long serialVersionUID = 1L;

/*********************************************************************************************/

	public XGProperties(String name, String value)
	{	super();
		this.put(XGStrings.toAlNum(name), value);
	}

	public XGProperties()
	{	super();
	}

	public void put(String name, String value)
	{	if(!this.containsKey(name)) this.put(name, new StringBuffer(value));
		else
		{	StringBuffer buf = this.get(name);
			buf.replace(0, buf.length(), value);
		}
	}
}
