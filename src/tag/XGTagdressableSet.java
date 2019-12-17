package tag;

import java.util.Iterator;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;

public class XGTagdressableSet<T extends XGAdressable & XGTagable> implements Iterable<T>
{
	private XGAdressableSet<T> adrSet = new XGAdressableSet<>();
	private XGTagableSet<T> tagSet = new XGTagableSet<T>();

	public void add(T obj)
	{	this.adrSet.add(obj);
		this.tagSet.add(obj);
		if(this.tagSet.size() != this.adrSet.size()) throw new RuntimeException("opcodeset difference!");
	}

	public T get(String tag)
	{	return this.tagSet.get(tag);
	}

	public T get(XGAdress adr)
	{	return this.adrSet.get(adr);
	}

	public T getOrDefault(XGAdress adr, T def)
	{	if(this.adrSet.contains(adr)) return this.get(adr);
		else return def;
	}

	public int size()
	{	return adrSet.size();
	}

	public boolean containsKey(XGAdress adr)
	{	return this.adrSet.contains(adr);
	}

	public boolean containsKey(String tag)
	{	return this.tagSet.containsKey(tag);
	}

	@Override public Iterator<T> iterator()
	{	return this.adrSet.iterator();
	}
}
