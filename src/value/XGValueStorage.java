package value;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import adress.InvalidXGAdressException;
import adress.XGAdress;

public interface XGValueStorage extends XGValueChangeListener
{
	static Set<XGValue> STORAGE = new TreeSet<>();
	static Set<XGValueChangeListener> LISTENERS = new HashSet<>();

	static XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		for(XGValue v : STORAGE)
		{	if(v.getAdress().equals(adr)) return v;}
		return null;
	}

	static Set<XGValue> getValues(XGAdress adr) throws InvalidXGAdressException
	{	Set<XGValue> set = new TreeSet<>();
		for(XGValue v : STORAGE)
		{	if(v.getAdress().equalsMaskedValidFields(adr)) set.add(v);}
		return set;
	}

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
		for(XGValue a : STORAGE) if(a.getAdress().equalsMaskedValidFields(adr))
		{	try
			{	s.add(new XGAdress(a.getAdress().getHi(), a.getAdress().getMid()));}
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
			if(l.getAdress().equalsMaskedValidFields(v.getAdress())) l.valueChanged(v);
			//l.valueChanged(v);
	}
}
