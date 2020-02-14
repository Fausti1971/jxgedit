package tag;

import java.util.Iterator;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;

public class XGTagableAdressableSet<T extends XGAddressable & XGTagable> implements Iterable<T>
{	private static Logger log = Logger.getAnonymousLogger();
	private XGAddressableSet<T> adrSet = new XGAddressableSet<>();
	private XGTagableSet<T> tagSet = new XGTagableSet<T>();

	public void add(T obj)
	{	this.adrSet.add(obj);
		this.tagSet.add(obj);
		if(this.tagSet.size() != this.adrSet.size()) throw new RuntimeException("adresses/tags =" + this.adrSet.size() + "/" + this.tagSet.size() + " by: " + obj);
	}

	public T get(String tag)
	{	return this.tagSet.get(tag);
	}

	public T get(XGAddress adr)
	{	return this.adrSet.get(adr);
	}

	public T getOrDefault(XGAddress adr, T def)
	{	T v = this.adrSet.getFirstValid(adr);
		if(v == null)
		{	log.info(def.getClass().getSimpleName() + " " + adr + " not found, using " + def);
			return def;
		}
		return v;
	}

	public int size()
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
}
