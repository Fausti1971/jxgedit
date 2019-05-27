package value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import adress.InvalidXGAdressException;
import adress.XGAdress;

public interface XGValueStorage
{
	static Map<XGAdress, XGValue> STORAGE = new TreeMap<>();	//adress, value
	static Map<XGAdress, Set<XGValueChangeListener>> LISTENERS = new HashMap<>();

	static XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(adr.isValueAdress()) return STORAGE.get(adr);
		else throw new InvalidXGAdressException("no valid value-adress: " + adr);
	}

	static Map<XGAdress, XGValue> getValues(XGAdress adr) throws InvalidXGAdressException
	{	Map<XGAdress, XGValue> map = new TreeMap<>();
		for(XGAdress a : STORAGE.keySet())
			if(a.equalsMaskedValidFields(adr)) map.put(a, getValue(a));
		return map;
	}

	static XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = getValue(adr);
		if(v == null);
		{	v = new XGValue(adr);
			STORAGE.put(adr, v);
			notifyListeners(v);
		}
		return v;
	}

	static XGValue getValueOrListenFor(XGAdress adr, XGValueChangeListener l) throws InvalidXGAdressException
	{	XGValue v = getValue(adr);
		if(v == null) addListener(adr, l);
		return v;
	}

	static void addListener(XGAdress adr, XGValueChangeListener l)
	{	if(!adr.isValueAdress()) return;
		Set<XGValueChangeListener> s = LISTENERS.getOrDefault(adr, new HashSet<>());
		s.add(l);
		LISTENERS.put(adr, s);
	}

	static void removeListener(XGValueChangeListener l)
	{	
	}

	static void notifyListeners(XGValue v)
	{	Set<XGValueChangeListener> s = LISTENERS.get(v.getAdress());
		if(s == null) return;
		for(XGValueChangeListener l : s) l.valueChanged(v);
	}
}
