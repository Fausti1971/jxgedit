package value;
import java.util.HashSet;
import java.util.Set;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import msg.XGMessage;
import msg.XGMessageParameterChange;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import obj.XGObjectInstance;
import obj.XGObjectType;
import parm.XGParameter;
import parm.XGParameterConstants;

public abstract class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAdressable
{

	static XGValue factory(XGAdress adr) throws InvalidXGAdressException
	{	XGParameter p = XGParameter.getParameter(adr);
		switch(p.getValueClass())
		{	default:		throw new RuntimeException("unknown valueclass: " + p.getValueClass());
			case Integer:	return new XGIntegerValue(adr);
			case Image:		return new XGImageValue(adr);
			case String:	return new XGStringValue(adr);
		}
	}

/***********************************************************************************************/

	private final XGAdress adress;
	private final XGObjectInstance instance;
	private final XGParameter parameter;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();
	
	protected XGValue(XGAdress adr) throws InvalidXGAdressException
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
		{	new XGMessageParameterChange(this, this).transmit();
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
