package value;

import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import device.TimeoutException;
import device.XGDevice;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger
{
	@Override public XGDevice getDevice()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public String getMessengerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{
		// TODO Auto-generated method stub
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
