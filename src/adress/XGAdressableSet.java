package adress;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class XGAdressableSet<T extends XGAdressable>
{
	private Map<XGAdress, T> map;

	public XGAdressableSet()
	{	this.map = new TreeMap<>();}

	public void add(T adr)
	{	this.map.put(adr.getAdress(), adr);}

	public T get(XGAdress adr)
	{	return this.map.get(adr);}

	public int size()
	{	return this.map.size();}

	public T getOrDefault(XGAdress adr, T adror)
	{	if(this.map.containsKey(adr)) return this.map.get(adr);
		else return adror;
	}

	public XGAdressableSet<T> getValid(XGAdress adr)
	{	XGAdressableSet<T> set = new XGAdressableSet<>();
		for(XGAdress a : this.map.keySet()) if(a.equalsValidFields(adr)) set.add(this.map.get(a));
		return set;
	}

	public T[] toArray(T[] a)
	{	return this.map.values().toArray(a);}

	public Collection<T> values()
	{	return this.map.values();}

	public Collection<XGAdress> adresses()
	{	return this.map.keySet();}
}
