package value;

import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressableSet;
import adress.XGAdressableSetListener;
import device.XGDevice;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
// wenn eh immer ein device angegeben werden muss, hätte man die StorageMethoden auch als statische in den jeweiligen Klassen lassen können; nachdenken!
public class XGValueStorage implements XGMessenger
{
	private final XGAdressableSet<XGValue> values = new XGAdressableSet<>();
	private final XGDevice device;

	public XGValueStorage(XGDevice dev)
	{	this.device = dev;
	}

	public XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		return this.values.get(adr);
	}

	public XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = this.getValue(adr);
		if(v == null) v = XGValue.factory(adr);
		return v;
	}

	public XGAdressableSet<XGValue> getValues()
	{	return this.values;
	}

	public XGAdressableSet<XGValue> getValues(XGAdress adr) throws InvalidXGAdressException
	{	return this.values.getAllValid(adr);
	}

	public synchronized XGAdressableSet<XGValue> getValues(String type)
	{	XGAdressableSet<XGValue> set = new XGAdressableSet<XGValue>();
		for(XGValue v : this.values) if(v.getInstance().getType().getName().equals(type)) set.add(v);
		this.values.addListener(set);
		return set;
	}

	public void addXGValueListener(XGAdressableSetListener l)
	{	this.values.addListener(l);}

	public void removeXGValueListener(XGAdressableSetListener l)
	{	this.values.removeListener(l);}

	public XGMessengerType getMessengerType()
	{	return XGMessengerType.Memory;
	}

	public String getMessengerName()
	{	return "Memory";
	}

	public void take(XGMessage m)
	{	try
		{	XGValue v = getValue(m.getAdress());
			m.decodeXGValue(0, v);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
		}
	}

	public XGResponse pull(XGRequest msg)
	{	return null;
	}

	public int getSysexID()
	{
	// TODO Auto-generated method stub
	return 0;
	}

}
