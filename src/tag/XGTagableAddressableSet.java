package tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;

public class XGTagableAddressableSet<T extends XGAddressable & XGTagable> implements Iterable<T>, Set<T>
{	private static Logger log = Logger.getAnonymousLogger();
	private XGAddressableSet<T> adrSet = new XGAddressableSet<>();
	private XGTagableSet<T> tagSet = new XGTagableSet<T>();

	@Override public boolean add(T obj)
	{	boolean res;
		res = this.adrSet.add(obj);
		res = this.tagSet.add(obj);
		if(this.tagSet.size() != this.adrSet.size()) throw new RuntimeException("adresses/tags =" + this.adrSet.size() + "/" + this.tagSet.size() + " by: " + obj);
		return res;
	}

	public T get(String tag)
	{	return this.tagSet.get(tag);
	}

	public T get(XGAddress adr)
	{	return this.adrSet.get(adr);
	}

	public T getValidOrDefault(XGAddress adr, T def)
	{	for(T i : this.getAllValid(adr))
			return i;
		return def;
	}

	public Set<T> getAllValid(XGAddress adr)
	{	return this.adrSet.getAllValid(adr);
	}

	public T getOrDefault(XGAddress adr, T def)
	{	T v = this.adrSet.getFirstValid(adr);
		if(v == null)
		{	log.info(def.getClass().getSimpleName() + " " + adr + " not found, using " + def);
			return def;
		}
		return v;
	}

	@Override public int size()
	{	return adrSet.size();
	}

	public boolean containsKey(XGAddress adr)
	{	return this.adrSet.contains(adr);
	}

	public boolean containsKey(String tag)
	{	return this.tagSet.containsKey(tag);
	}

	@Override public Iterator<T> iterator()
	{	return this.adrSet.iterator();
	}

	@Override public boolean isEmpty()
	{	return this.adrSet.isEmpty() && this.tagSet.isEmpty();
	}

	@Override public boolean contains(Object o)
	{	if(o instanceof XGTagable) return this.tagSet.contains(o);
		if(o instanceof XGAddressable) return this.adrSet.contains(o);
		return false;
	}

	@Override public Object[] toArray()
	{	return this.adrSet.toArray();
	}

	@Override public <T> T[] toArray(T[] a)
	{	return this.adrSet.toArray(a);
	}

	@Override public boolean remove(Object o)
	{	boolean res = this.adrSet.remove(o);
		res = this.tagSet.remove(o);
		return res;
	}

	@Override public boolean containsAll(Collection<?> c)
	{	boolean res = this.adrSet.containsAll(c);
		res = this.tagSet.containsAll(c);
		return res;
	}

	@Override public boolean addAll(Collection<? extends T> c)
	{	boolean res = false;
		res = this.adrSet.addAll(c);
		res = this.tagSet.addAll(c);
		return res;
	}

	@Override public boolean retainAll(Collection<?> c)
	{	boolean res = this.adrSet.retainAll(c);
		res = this.tagSet.retainAll(c);
		return res;
	}

	@Override public boolean removeAll(Collection<?> c)
	{	boolean res = this.adrSet.removeAll(c);
		res = this.tagSet.removeAll(c);
		return res;
	}

	@Override public void clear()
	{	this.adrSet.clear();
		this.tagSet.clear();
	}
}
