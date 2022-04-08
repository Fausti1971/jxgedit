package adress;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import application.XGLoggable;

public class XGAddressableSet<T extends XGAddressable> implements Set<T>, Iterable<T>, XGAddressConstants, XGLoggable
{

/***********************************************************************************************************/

	private String memberName = "";
	private final SortedMap<XGAddress, T> map = new TreeMap<>();

	@Override public synchronized boolean add(T obj)
	{	synchronized(this.map)
		{	if(obj == null) return false;
			if(this.memberName.equals("")) this.memberName = obj.getClass().getSimpleName();
			XGAddress adr = obj.getAddress();
			this.map.put(adr, obj);
		}
		return true;
	}


	@Override public synchronized void clear(){	this.map.clear();}

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

	@Override public int size(){	return this.map.size();}

	public synchronized Collection<T> values(){	return this.map.values();}

	@Override public synchronized Iterator<T> iterator(){	return this.map.values().iterator();}

	@Override public String toString()
	{	if(this.map.isEmpty()) return "empty set";
		return (this.memberName) + " (" + size() + ")";
	}

	@Override public boolean isEmpty(){	return this.map.isEmpty();}

	@Override public boolean contains(Object o){	return this.map.containsValue(o);}

	@Override public Object[] toArray(){	return this.map.values().toArray();}

	@Override public <A> A[] toArray(A[] a){	return this.map.values().toArray(a);}

	@Override public synchronized boolean remove(Object o)
	{	if(!(o instanceof XGAddressable)) return false;
		synchronized(this.map){	return this.map.remove(((XGAddressable)o).getAddress(), o);}
	}

	@Override public boolean containsAll(Collection<?> c){	return this.map.values().containsAll(c);}

	@Override public boolean addAll(Collection<? extends T> c)
	{	for(T t: c) this.add(t);
		return true;
	}

	@Override public boolean retainAll(Collection<?> c)
	{	for(Object o: c)
		{	if(this.map.containsValue(o)) continue;
			this.remove(o);
		}
		return true;
	}

	@Override public boolean removeAll(Collection<?> c)
	{	for(Object o: c)this.remove(o);
		return true;
	}
}
