package xml;

import config.Configurable;import tag.XGTagable;import java.util.LinkedHashSet;import java.util.Set;

public class XGProperty implements XGTagable
{	private final String key;
	private final StringBuffer value;
	private Set<Configurable> listeners = new LinkedHashSet<>();

	XGProperty(String k, String v)
	{	this.key = k;
		this.value = new StringBuffer(v);
	}

	public String getTag()
	{	return this.key;
	}
}
