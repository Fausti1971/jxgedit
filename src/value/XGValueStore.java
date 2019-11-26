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
	
	public XGDevice getDevice()
	{	return this.device;
	}

	public String getMessengerName()
	{	return null;
	}

	public void transmit(XGMessage m) throws MidiUnavailableException
	{
	}

	public XGResponse request(XGRequest msg) throws TimeoutException,MidiUnavailableException
	{	return null;
	}

}
