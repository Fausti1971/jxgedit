package xml;

import java.util.LinkedHashMap;

public class XGProperties extends LinkedHashMap<String, StringBuffer>
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void put(String name, String value)
	{	if(!this.containsKey(name)) this.put(name, new StringBuffer(value));
		else
		{	StringBuffer buf = this.get(name);
			buf.replace(0, buf.length(), value);
		}
	}
}
