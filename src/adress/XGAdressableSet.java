package adress;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class XGAdressableSet<T extends XGAdressable> implements Iterable<T>, XGAdressConstants, XGAdressableSetListener
{
/***********************************************************************************************************/

	private Class<?> type = null;
	private SortedMap<XGAdress, T> map = new TreeMap<XGAdress,T>();
	private Set<XGAdressableSetListener> listeners = new HashSet<XGAdressableSetListener>();

	public synchronized void add(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return;
			if(this.type == null) this.type = obj.getClass();
			XGAdress adr = obj.getAdress();
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

	public synchronized void remove(XGAdress adr)
	{	synchronized(this.map)
		{	this.map.remove(adr);
		}
	}

	public synchronized T get(XGAdress adr)
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

	public synchronized int indexOf(XGAdress adr)
	{	synchronized(this.map)
		{	Vector<XGAdress> v = new Vector<>(this.adresses());
			return v.indexOf(adr);
		}
	}

	public int size()
	{	return this.map.size();
	}

	public boolean contains(XGAdress adr)
	{	return this.map.containsKey(adr);
	}

	public T getOrDefault(XGAdress adr, T def)
	{	if(this.map.containsKey(adr)) return this.map.get(adr);
		else return def;
	}

	public T getFirstValidOrDefault(XGAdress adr, T def)
	{	T res = getFirstValid(adr);
		if(res == null) return def;
		return res;
	}

	public synchronized T getFirstValid(XGAdress adr)
	{	for(T a : this.map.values()) if(a.getAdress().equalsValidFields(adr)) return a;
		return null;
	}

	public synchronized XGAdressableSet<T> getAllValid(XGAdress adr)
	{	XGAdressableSet<T> set = new XGAdressableSet<>();
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

	public synchronized Collection<XGAdress> adresses()
	{	return this.map.keySet();
	}

	@Override public synchronized Iterator<T> iterator()
	{	return this.map.values().iterator();
	}

	public void addListener(XGAdressableSetListener l)
	{	listeners.add(l);
	}

	public void removeListener(XGAdressableSetListener l)
	{	listeners.remove(l);
	}

	private synchronized void notifyListeners(XGAdress adr)
	{	if(adr != null) for(XGAdressableSetListener l : listeners) l.setChanged(adr);
	}

	@Override public void setChanged(XGAdress adr)
	{	this.notifyListeners(adr);
	}

	@Override public String toString()
	{	if(this.map.isEmpty()) return "empty set";
		return (this.type.getSimpleName()) + " (" + size() + ")";
	}
}
