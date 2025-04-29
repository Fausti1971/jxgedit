package xml;

import config.XGPropertyChangeListener;import tag.XGTagable;import java.util.LinkedHashSet;import java.util.Set;

public class XGProperty implements XGTagable
{	private final String key;
	private final StringBuffer value;
	private final Set<XGPropertyChangeListener> listeners = new LinkedHashSet<>();

	public XGProperty(String k, String v)
	{	if(v == null) v = "";
		this.key = k;
		this.value = new StringBuffer(v);
	}

	@Override public String getTag(){ return this.key;}

	public void setValue(String s)
	{	if(s != null && !s.equals(this.value.toString()))
		{	this.value.replace(0, this.value.length(), s);
			this.notifyListeners();
		}
	}

	public StringBuffer getValue(){ return this.value;}

	public Set<XGPropertyChangeListener> getListeners(){ return this.listeners;}

	@Override public String toString(){ return this.key + "=" + this.value;}

	private void notifyListeners()
	{	for(XGPropertyChangeListener l : this.listeners)
		{	System.out.println("info to listener: " + l);
			l.propertyChanged(this);
		}
	}
}
