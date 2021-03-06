package tag;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class XGTagableSet<T extends XGTagable> implements Iterable<T>, Set<T> 
{

	private final Map<String, T> map = new LinkedHashMap<>();

	@Override public boolean add(T t)
	{	if(t != null)
		{	this.map.put(t.getTag(), t);
			return true;
		}
		else return false;
	}

	public T get(String tag)
	{	return this.map.get(tag);
	}

	public T getOrDefault(String tag, T def)
	{	if(this.containsKey(tag)) return this.get(tag);
		else return def;
	}

	public T getOrNew(String tag, T newT)
	{	if(!this.containsKey(tag)) this.add(newT);
		return this.get(tag);
	}

	@Override public int size()
	{	return map.size();
	}

	public boolean containsKey(String tag)
	{	return this.map.containsKey(tag);
	}

	@Override public Iterator<T> iterator()
	{	return this.map.values().iterator();
	}

	public Enumeration<T> enumeration()
	{	return Collections.enumeration(this.map.values());
	}

	@Override public boolean isEmpty()
	{	return this.map.isEmpty();
	}

	@Override public boolean contains(Object o)
	{	return this.map.containsValue(o);
	}

	@Override public Object[] toArray()
	{	return this.map.values().toArray();
	}

	@Override public <A> A[] toArray(A[] a)
	{	return this.map.values().toArray(a);
	}

	@Override public boolean remove(Object o)
	{	if(o instanceof XGTagable) this.map.remove(((XGTagable)o).getTag());
		return true;
	}

	@Override public boolean containsAll(Collection<?> c)
	{	return this.map.values().containsAll(c);
	}

	@Override public boolean addAll(Collection<? extends T> c)
	{
		for(T t: c)this.add(t);
		return true;
	}

	@Override public boolean retainAll(Collection<?> c)
	{
		for(Object o: c){	if(this.map.containsValue(o)) continue;
			this.remove(o);
		}
		return true;
	}

	@Override public boolean removeAll(Collection<?> c)
	{
		for(Object o: c)this.remove(o);
		return true;
	}

	@Override public void clear()
	{	this.map.clear();
	}
}
