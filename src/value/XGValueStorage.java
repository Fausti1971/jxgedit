package value;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressableSet;

public interface XGValueStorage extends XGValueChangeListener
{
	static XGAdressableSet<XGValue> STORAGE = new XGAdressableSet<>();
	static Set<XGValueChangeListener> LISTENERS = new HashSet<>();

	static XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		return STORAGE.get(adr);
	}

	static XGAdressableSet<XGValue> getValues(XGAdress adr) throws InvalidXGAdressException
	{	return STORAGE.getValid(adr);}

	static XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = getValue(adr);
		if(v == null);
		{	v = new XGValue(adr);
			STORAGE.add(v);
			notifyListeners(v);
		}
		return v;
	}

	static XGValue getValueOrListenFor(XGValueChangeListener l) throws InvalidXGAdressException
	{	XGValue v = getValue(l.getAdress());
		if(v == null) addListener(l);
		else
		{	v.addListener(l);
			removeListener(l);
		}
		return v;
	}

	static Set<XGAdress> getObjectInstances(XGAdress adr)
	{	Set<XGAdress> s = new TreeSet<>();
		for(XGAdress a : STORAGE.adresses()) if(a.equalsValidFields(adr))
		{	try
			{	s.add(new XGAdress(a.getHi(), a.getMid()));}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();}
		}
		return s;
	}

	static void addListener(XGValueChangeListener l)
	{	LISTENERS.add(l);}

	static void removeListener(XGValueChangeListener l)
	{	LISTENERS.remove(l);}

	static void notifyListeners(XGValue v)
	{	for(XGValueChangeListener l : LISTENERS)
			if(l.getAdress().equalsValidFields(v.getAdress())) l.valueChanged(v);
			//l.valueChanged(v);
	}
}
