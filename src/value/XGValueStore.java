package value;

import javax.sound.midi.MidiUnavailableException;
import adress.XGAdressableSet;
import device.TimeoutException;
import device.XGDevice;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGValueStore extends XGAdressableSet<XGValue> implements XGMessenger
{
	private final XGDevice device;

	public XGValueStore(XGDevice dev)
	{	this.device = dev;
	}
	
	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public String getMessengerName()
	{	return null;
	}

	@Override public void transmit(XGMessage m) throws MidiUnavailableException
	{
	}

	@Override public XGResponse request(XGRequest msg) throws TimeoutException,MidiUnavailableException
	{	return null;
	}

}
