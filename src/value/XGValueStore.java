package value;

import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import device.TimeoutException;
import device.XGDevice;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import obj.XGType;
import tag.XGTagableSet;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger
{
	private final XGDevice device;

	public XGValueStore(XGDevice dev)
	{	this.device = dev;
	}

	public XGTagableSet<XGType> getTypes()
	{	XGTagableSet<XGType> set = new XGTagableSet<XGType>();
		for(XGValue v : this) set.add(v.getInstance().getType());
		return set;
	}
	
	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public String getMessengerName()
	{	return this.getDevice().toString() + " memory";
	}

	@Override public void transmit(XGMessage m) throws MidiUnavailableException
	{	try
		{	for(XGValue v : ((XGResponse)m).getValues()) this.add(v);
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

	@Override public XGResponse request(XGRequest msg) throws TimeoutException,MidiUnavailableException
	{	return null;
	}

}
