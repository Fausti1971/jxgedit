package adress;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class XGAddressableSet<T extends XGAddressable> implements Set<T>, Iterable<T>, XGAddressConstants, XGAddressableSetListener
{

/***********************************************************************************************************/

	private Class<?> type = null;
	private final SortedMap<XGAddress, T> map = new TreeMap<XGAddress,T>();
	private final Set<XGAddressableSetListener> listeners = new HashSet<XGAddressableSetListener>();

	@Override public synchronized boolean add(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return false;
			if(this.type == null) this.type = obj.getClass();
			XGAddress adr;
			adr = obj.getAddress();
			this.map.put(adr, obj);
			this.notifyListeners(adr);
		}
		return true;
	}

	public synchronized void remove(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return;
			this.map.remove(obj.getAddress(), obj);
			this.notifyListeners(obj.getAddress());
		}
	}

	public synchronized void remove(XGAddress adr)
	{	synchronized(this.map)
		{	this.map.remove(adr);
			this.notifyListeners(adr);
		}
	}

	@Override public synchronized void clear()
	{	this.map.clear();
		this.notifyListeners(INVALIDADRESS);
	}

	public synchronized T get(XGAddress adr)
	{	synchronized(this.map)
		{	return this.map.get(adr);
		}
	}

	public synchronized T get(int index)
	{	synchronized(this.map)
		{	Vector<T> v = new Vector<>(this.values());
			return v.get(index);
		}
	}

	public synchronized int indexOf(XGAddress adr)
	{	synchronized(this.map)
		{	Vector<XGAddress> v = new Vector<>(this.adresses());
			return v.indexOf(adr);
		}
	}

	@Override public int size()
	{	return this.map.size();
	}

	public boolean contains(XGAddress adr)
	{	return this.map.containsKey(adr);
	}

	public T getOrDefault(XGAddress adr, T def)
	{	if(this.map.containsKey(adr)) return this.map.get(adr);
		else return def;
	}

	public T getFirstValidOrDefault(XGAddress adr, T def)
	{	T res = getFirstValid(adr);
		if(res == null) return def;
		return res;
	}

	public synchronized T getFirstValid(XGAddress adr)
	{	for(T a : this.map.values()) if(a.getAddress().equalsValidFields(adr)) return a;
		return null;
	}

	public synchronized XGAddressableSet<T> getAllValid(XGAddress adr)
	{	XGAddressableSet<T> set = new XGAddressableSet<>();
		for(T a : this.values()) if(a.getAddress().equalsValidFields(adr)) set.add(a);
		this.addListener(set);
		return set;
	}

	public T[] toArray(T[] a)
	{	return this.map.values().toArray(a);
	}

	public synchronized Collection<T> values()
	{	return this.map.values();
	}

	public synchronized Collection<XGAddress> adresses()
	{	return this.map.keySet();
	}

	@Override public synchronized Iterator<T> iterator()
	{	return this.map.values().iterator();
	}

	public void addListener(XGAddressableSetListener l)
	{	listeners.add(l);
	}

	public void removeListener(XGAddressableSetListener l)
	{	listeners.remove(l);
	}

	private synchronized void notifyListeners(XGAddress adr)
	{	if(adr != null) for(XGAddressableSetListener l : listeners) l.setChanged(adr);
	}

	@Override public void setChanged(XGAddress adr)
	{	this.notifyListeners(adr);
	}

	@Override public String toString()
	{	if(this.map.isEmpty()) return "empty set";
		return (this.type.getSimpleName()) + " (" + size() + ")";
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
	{	return this.remove(o);
	}

	@Override public boolean containsAll(Collection<?> c)
	{	return this.map.values().containsAll(c);
	}

	@Override public boolean addAll(Collection<? extends T> c)
	{	Iterator<? extends T> i = c.iterator();
		while(i.hasNext()) this.add(i.next());
		return true;
	}

	@Override public boolean retainAll(Collection<?> c)
	{	Iterator<?> i = c.iterator();
		while(i.hasNext())
		{	Object o = i.next();
			if(this.map.containsValue(o)) continue;
			this.remove(o);
		}
		return true;
	}

	@Override public boolean removeAll(Collection<?> c)
	{	Iterator<?> i = c.iterator();
		while(i.hasNext()) this.remove(i.next());
		return true;
	}
}
