package tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;

public class XGTagableAddressableSet<T extends XGAddressable & XGTagable> implements Iterable<T>, Set<T>, XGLoggable
{
	private final XGAddressableSet<T> adrSet = new XGAddressableSet<>();
	private final XGTagableSet<T> tagSet = new XGTagableSet<>();

	@Override public boolean add(T obj)
	{	boolean res;
		res = this.adrSet.add(obj);
		res = this.tagSet.add(obj);
		try
		{	this.checkConsistency();
		}
		catch(Exception e)
		{	LOG.severe(e.getMessage() + " caused by " + obj);
			return false;
		}
		return res;
	}

	private void checkConsistency()throws Exception
	{	if(this.tagSet.size() != this.adrSet.size()) throw new Exception("Consistencycheck failed: " + this.adrSet.size() + " addresses/" + this.tagSet.size());
	}

	public T get(String tag)
	{	return this.tagSet.get(tag);
	}

	public T get(XGAddress adr)
	{	return this.adrSet.get(adr);
	}

	//public T getValidOrDefault(XGAddress adr, T def)
	//{	for(T i : this.getAllValid(adr))
	//		return i;
	//	return def;
	//}

	//public Set<T> getAllValid(XGAddress adr)
	//{	return this.adrSet.getAllIncluding(adr);
	//}

	//public T getOrDefault(String s, T def)
	//{	T res = this.tagSet.get(s);
	//	if(res == null) res = def;
	//	return res;
	//}

	//public T getOrDefault(XGAddress adr, T def)
	//{	T v = this.adrSet.getFirstIncluding(adr);
	//	if(v == null)
	//	{	LOG.info(def.getClass().getSimpleName() + " " + adr + " not found, using " + def);
	//		return def;
	//	}
	//	return v;
	//}

	@Override public int size(){	return adrSet.size();}

	//public boolean containsKey(XGAddress adr){	return this.adrSet.contains(adr);}

	//public boolean containsKey(String tag){	return this.tagSet.containsKey(tag);}

	@Override public Iterator<T> iterator(){	return this.adrSet.iterator();}

	@Override public boolean isEmpty(){	return this.adrSet.isEmpty() && this.tagSet.isEmpty();}

	@Override public boolean contains(Object o)
	{	if(o instanceof XGTagable) return this.tagSet.contains(o);
		if(o instanceof XGAddressable) return this.adrSet.contains(o);
		return false;
	}

	@Override public Object[] toArray(){	return this.adrSet.toArray();}

	@Override public <T> T[] toArray(T[] a){	return this.adrSet.toArray(a);}

	@Override public boolean remove(Object o){	return this.adrSet.remove(o) && this.tagSet.remove(o);}

	@Override public boolean containsAll(Collection<?> c){	return this.adrSet.containsAll(c) && this.tagSet.containsAll(c);}

	@Override public boolean addAll(Collection<? extends T> c)
	{	boolean res = this.adrSet.addAll(c) && this.tagSet.addAll(c);
		try
		{	this.checkConsistency();
		}
		catch(Exception e)
		{	LOG.severe(e.getMessage() + " caused by " + c);
			return false;
		}
		return res;
	}

	@Override public boolean retainAll(Collection<?> c)
	{	boolean res = this.adrSet.retainAll(c) && this.tagSet.retainAll(c);
		try
		{	this.checkConsistency();
		}
		catch(Exception e)
		{	LOG.severe(e.getMessage() + " caused by " + c);
			return false;
		}
		return res;
	}

	@Override public boolean removeAll(Collection<?> c)
	{	boolean res = this.adrSet.removeAll(c) && this.tagSet.removeAll(c);
		try
		{	this.checkConsistency();
		}
		catch(Exception e)
		{	LOG.severe(e.getMessage() + " caused by " + c);
			return false;
		}
		return res;
	}

	@Override public void clear()
	{	this.adrSet.clear();
		this.tagSet.clear();
	}
}
