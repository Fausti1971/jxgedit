package adress;

import application.XGLoggable;
import java.util.*;

public class XGIdentifiableSet<T extends XGIdentifiable> implements Set<T>, Iterable<T>, XGLoggable
{	private final Map<Integer, T> map = new HashMap<>();
	private String memberName = "";

	public int size(){	return this.map.size();}

	public boolean isEmpty(){	return this.map.isEmpty();}

	public boolean contains(Object o){	return this.map.containsValue(o);}

	public Iterator<T> iterator(){	return this.map.values().iterator();}

	public Object[] toArray(){	return this.map.values().toArray();}

	public <T1> T1[] toArray(T1[] s){	return this.map.values().toArray(s);}

	public boolean add(T t)
	{	boolean res = !this.map.containsKey(t.getID());
		if(this.memberName.equals("")) this.memberName = t.getClass().getSimpleName();
		this.map.put(t.getID(), t);
		return res;
	}

	public T get(int id){	return this.map.get(id);}

	public boolean remove(Object o)
	{	boolean res = this.map.containsValue(o);
		this.map.remove(o);
		return res;
	}

	public boolean containsAll(Collection<?> collection){	return this.map.values().containsAll(collection);}

	public boolean addAll(Collection<? extends T> collection)
	{	for(T t : collection) this.add(t);
		return true;
	}

	public boolean retainAll(Collection<?> collection)
	{	for(Object o : collection)
		{	if(this.map.containsValue(o)) continue;
			this.remove(o);
		}
		return true;
	}

	public boolean removeAll(Collection<?> collection)
	{	for(Object o: collection)this.remove(o);
		return true;
	}

	public void clear(){	this.map.clear();}

	@Override public String toString()
	{	if(this.map.isEmpty()) return "empty set";
		return (this.memberName) + " (" + this.size() + ")";
	}
}
