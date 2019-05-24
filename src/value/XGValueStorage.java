package value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import adress.InvalidXGAdressException;
import adress.XGAdress;

public interface XGValueStorage
{
	static Map<Integer, XGValue> STORAGE = new TreeMap<>();	//hashCode, value
	static Set<XGValueChangeListener> LISTENERS = new HashSet<>();
	
	static XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	if(STORAGE.containsKey(adr.hashCode())) return STORAGE.get(adr.hashCode());
		else
		{	XGValue v = new XGValue(adr);
			STORAGE.put(adr.hashCode(), v);
			notifyListeners(v);
			return v;
		}
	}

	static XGValue getValue(XGAdress adr)
	{	return STORAGE.get(adr.hashCode());}

	static void notifyListeners(XGValue v)
	{	for(XGValueChangeListener l : LISTENERS) l.valueChanged(v);}
}
