package adress;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class XGAdressableSet<T extends XGAdressable> implements Iterable<T>
{
	private Map<XGAdress, T> map;

	public XGAdressableSet()
	{	this.map = new TreeMap<>();}

	public boolean contains(XGAdress adr)
	{	return this.map.containsKey(adr);}

	public void add(T adr)
	{	this.map.put(adr.getAdress(), adr);}

	public T get(XGAdress adr)
	{	return this.map.get(adr);}

	public int size()
	{	return this.map.size();}

	public T getOrDefault(XGAdress adr, T def)
	{	if(this.map.containsKey(adr)) return this.map.get(adr);
		else return def;
	}

	public XGAdressableSet<T> getAllValid(XGAdress adr)
	{	XGAdressableSet<T> set = new XGAdressableSet<>();
		for(XGAdress a : this.map.keySet()) if(a.equalsValidFields(adr)) set.add(this.map.get(a));
		return set;
	}

	public T getValid(XGAdress adr)
	{	for(T a : this.map.values()) if(a.getAdress().equalsValidFields(adr)) return a;
		return null;
	}


	public T[] toArray(T[] a)
	{	return this.map.values().toArray(a);}

	public Collection<T> values()
	{	return this.map.values();}

	public Collection<XGAdress> adresses()
	{	return this.map.keySet();}

	public Iterator<T> iterator()
	{	return this.map.values().iterator();}
}
