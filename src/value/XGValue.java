package value;
import java.util.HashSet;
import java.util.Set;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;
import adress.XGAdressableSetListener;
import msg.XGMessageParameterChange;
import obj.XGObjectInstance;
import obj.XGObjectType;
import parm.XGParameter;
import parm.XGParameterConstants;

public abstract class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAdressable
{
	static XGAdressableSet<XGValue> VALUES = new XGAdressableSet<>();

	private static XGValue factory(XGAdress adr) throws InvalidXGAdressException
	{	XGParameter p = XGParameter.getParameter(adr);
		switch(p.getValueClass())
		{	default:
			case Integer:	return new XGIntegerValue(adr);
			case Image:		return new XGImageValue(adr);
			case String:	return new XGStringValue(adr);
		}
	}

	public static XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		return VALUES.get(adr);
	}

	public static XGAdressableSet<XGValue> getValues()
	{	return VALUES;}

	public static XGAdressableSet<XGValue> getValues(XGAdress adr) throws InvalidXGAdressException
	{	return VALUES.getAllValid(adr);}

	public synchronized static XGAdressableSet<XGValue> getValues(String type)
	{	XGAdressableSet<XGValue> set = new XGAdressableSet<XGValue>();
		for(XGValue v : VALUES) if(v.getInstance().getType().getName().equals(type)) set.add(v);
		VALUES.addListener(set);
		return set;
	}

	public static XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = getValue(adr);
		if(v == null) v = XGValue.factory(adr);
		return v;
	}

	public static XGValue getValueOrNewAndStore(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = getValue(adr);
		if(v == null)
		{	v = XGValue.factory(adr);
			VALUES.add(v);
		}
		return v;
	}

	public static void addXGValueListener(XGAdressableSetListener l)
	{	VALUES.addListener(l);}

	public static void removeXGValueListener(XGAdressableSetListener l)
	{	VALUES.removeListener(l);}

/***********************************************************************************************/

	private final XGAdress adress;
	private final XGObjectInstance instance;
	private final XGParameter parameter;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();
	
	public XGValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("not a value adress: " + adr);
		this.adress = adr;
		this.instance = XGObjectType.getObjectTypeOrNew(adr).getInstance(adr);
		this.parameter = XGParameter.getParameter(adr);
	}

	public void addListener(XGValueChangeListener l)
	{	this.listeners.add(l);}

	public void removeListener(XGValueChangeListener l)
	{	this.listeners.remove(l);}

	public synchronized void notifyListeners()
	{	for(XGValueChangeListener l : this.listeners) l.contentChanged(this);
//		VALUESET.notifyListeners(this.adress);
	}

	public XGAdress getAdress()
	{	return this.adress;}

	public XGObjectInstance getInstance()
	{	return this.instance;}

	public XGParameter getParameter()
	{	return this.parameter;}

	protected abstract void validate(Object o) throws WrongXGValueTypeException;

	public abstract boolean setContent(Object o) throws WrongXGValueTypeException;

	public abstract Object getContent();

	public abstract boolean addAndTransmit(Object v) throws WrongXGValueTypeException;

	public boolean setContentAndTransmit(Object o) throws WrongXGValueTypeException, InvalidXGAdressException
	{	boolean changed = this.setContent(o);
		if(changed)
		{	new XGMessageParameterChange(this).transmit();
			this.notifyListeners();
		}
		return changed;
	}

	protected abstract Object limitize(Object v);

	public String getInfo()
	{	return this.getClass().getSimpleName() + " "
			+ this.instance.getType() + " "
			+ this.instance + ", "
			+ this.getParameter() + " = "
			+ this.toString();
}
	@Override public abstract String toString();

	public int compareTo(XGValue o)
	{	return this.adress.compareTo(o.adress);}
}
