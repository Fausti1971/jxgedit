package tag;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class XGTagableSet<T extends XGTagable> implements Iterable<T>
{

	private final Map<String, T> map = new LinkedHashMap<>();

	public void add(T t)
	{	this.map.put(t.getTag(), t);
	}

	public T get(String tag)
	{	return this.map.get(tag);
	}

	public T getOrDefault(String tag, T def)
	{	if(this.get(tag) == null) return def;
		else return this.get(tag);
	}

	public int size()
	{	return map.size();
	}

	public boolean containsKey(String tag)
	{	return this.map.containsKey(tag);
	}

	public Iterator<T> iterator()
	{	return this.map.values().iterator();
	}
}
