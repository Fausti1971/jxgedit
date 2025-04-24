package tag;


import java.util.*;
import adress.*;
import application.XGLoggable;
/**
* zweidimensionale Map aus Tag und ID Map<Tag,XGIdentifiableSet<T>>
*/
public class XGTagableIdentifiableSet<T extends XGIdentifiable & XGTagable> implements XGLoggable
{
	private final Map<String, XGIdentifiableSet<T>> map = new HashMap<>();

	public void add(T obj)
	{	XGIdentifiableSet<T> set = this.map.getOrDefault(obj.getTag(), new XGIdentifiableSet<>());
		set.add(obj);
		this.map.putIfAbsent(obj.getTag(), set);
	}

	public T get(String tag, int id)
	{	if(this.map.containsKey(tag)) return this.map.get(tag).get(id);
		else return null;
	}

	public boolean contains(T o)
	{	if(this.map.containsKey(o.getTag())) return this.map.get(o.getTag()).contains(o);
		else return false;
	}

}
