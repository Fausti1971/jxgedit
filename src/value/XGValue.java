package value;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import device.XGDevice;
import device.XGMidi;
import msg.XGMessageException;
import msg.XGMessageParameterChange;
import obj.XGObjectInstance;
import obj.XGObjectType;
import opcode.NoSuchOpcodeException;
import opcode.XGOpcode;
import parm.XGParameter;
import parm.XGParameterConstants;
import tag.XGTagable;
import tag.XGTagdressableSet;
//TODO: ValueStorage integrieren und entsorgen
public abstract class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAdressable, XGTagable
{	private static Map<XGDevice, XGTagdressableSet<XGValue>> STORAGE = new HashMap<>();

	public static XGValue factory(XGDevice dev, XGAdress adr) throws InvalidXGAdressException, NoSuchOpcodeException
	{	XGOpcode o = XGOpcode.getOpcode(adr);
		switch(o.getValueClass())
		{	case Integer:	return new XGIntegerValue(dev, adr);
			case Image:		return new XGImageValue(dev, adr);
			case String:	return new XGStringValue(dev, adr);
			default:		throw new RuntimeException("unknown valueclass: " + o.getValueClass());
		}
	}
	public static XGValue getValue(XGDevice dev, XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValidAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		return STORAGE.get(dev).get(adr);
	}

	public static XGValue getValueOrNew(XGDevice dev, XGAdress adr) throws InvalidXGAdressException, NoSuchOpcodeException
	{	XGValue v = getValue(dev, adr);
		if(v == null) v = XGValue.factory(dev, adr);
		return v;
	}

	public static XGTagdressableSet<XGValue> getValues(XGDevice dev)
	{	return STORAGE.get(dev);
	}

	public static XGValue getValue(XGDevice dev, String tag)
	{	return STORAGE.get(dev).get(tag);
	}
/*
	public static XGAdressableSet<XGValue> getValues(XGDevice dev, XGAdress adr) throws InvalidXGAdressException
	{	return STORAGE.getAllValid(adr);
	}

	public synchronized XGAdressableSet<XGValue> getValues(XGDevice dev, String type)
	{	XGAdressableSet<XGValue> set = new XGAdressableSet<XGValue>();
		for(XGValue v : getValues(dev)) if(v.getInstance().getType().getName().equals(type)) set.add(v);
		this.values.addListener(set);
		return set;
	}
*/
/***********************************************************************************************/

	private final XGDevice device;
	private final XGAdress adress;
	private final XGObjectInstance instance;
	private final XGOpcode opcode;
//	private final XGParameter parameter;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();
	
	protected XGValue(XGDevice dev, XGAdress adr) throws InvalidXGAdressException, NoSuchOpcodeException
	{	if(!adr.isValidAdress()) throw new InvalidXGAdressException("not a valid adress: " + adr);
		this.device = dev;
		this.opcode = XGOpcode.getOpcode(adr);
		this.adress = adr;
		this.instance = XGObjectType.getObjectTypeOrNew(dev, adr).getInstance(adr);
//		this.parameter = XGParameter.getParameter(dev, this.opcode.getTag());
	}

	public void addListener(XGValueChangeListener l)
	{	this.listeners.add(l);}

	public void removeListener(XGValueChangeListener l)
	{	this.listeners.remove(l);}

	public synchronized void notifyListeners()
	{	for(XGValueChangeListener l : this.listeners) l.contentChanged(this);
//		VALUESET.notifyListeners(this.adress);
	}

	public String getTag()
	{	return this.opcode.getTag();
	}

	public XGAdress getAdress()
	{	return this.adress;
	}

	public XGObjectInstance getInstance()
	{	return this.instance;
	}

	public XGParameter getParameter()
	{	return XGParameter.getParameter(this.device, this.getTag());
	}

	public XGOpcode getOpcode()
	{	return this.opcode;
	}

	protected abstract void validate(Object o) throws WrongXGValueTypeException;

	public abstract boolean setContent(Object o) throws WrongXGValueTypeException;

	public abstract Object getContent();

	public abstract boolean addContent(Object v) throws WrongXGValueTypeException;

	public void transmit(XGMidi midi) throws XGMessageException, InvalidXGAdressException
	{	XGMessageParameterChange m = new XGMessageParameterChange(midi, this);
		m.setDestination(midi);
		m.transmit();
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
