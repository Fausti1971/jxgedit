package adress;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class XGAddressableSet<T extends XGAddressable> implements Iterable<T>, XGAddressConstants, XGAddressableSetListener
{
/***********************************************************************************************************/

	private Class<?> type = null;
	private SortedMap<XGAddress, T> map = new TreeMap<XGAddress,T>();
	private Set<XGAddressableSetListener> listeners = new HashSet<XGAddressableSetListener>();

	public synchronized void add(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return;
			if(this.type == null) this.type = obj.getClass();
			XGAddress adr;
			adr = obj.getAdress();
				this.map.put(adr, obj);
				notifyListeners(adr);
		}
	}

	public synchronized void remove(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return;
			this.map.remove(obj.getAdress(), obj);
				notifyListeners(obj.getAdress());
		}
	}

	public synchronized void remove(XGAddress adr)
	{	synchronized(this.map)
		{	this.map.remove(adr);
		}
	}

	public synchronized void clear()
	{	this.map.clear();
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

	public int size()
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
	{	for(T a : this.map.values()) if(a.getAdress().equalsValidFields(adr)) return a;
				return null;
			}

	public synchronized XGAddressableSet<T> getAllValid(XGAddress adr)
	{	XGAddressableSet<T> set = new XGAddressableSet<>();
		for(T a : this.values()) if(a.getAdress().equalsValidFields(adr)) set.add(a);
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
}
