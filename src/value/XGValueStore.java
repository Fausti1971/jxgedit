package value;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.TimeoutException;
import device.XGDevice;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger, XGLoggable
{	
	private final XGDevice device;

	public XGValueStore(XGDevice dev)
	{	this.device = dev;
	}

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public String getMessengerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{
		int end = msg.getBulkSize() + msg.getBaseOffset(),
			offset = msg.getAddress().getLo().getValue(),
			hi = msg.getAddress().getHi().getValue(),
			mid = msg.getAddress().getMid().getValue(),
			size = 1;

		for(int i = msg.getBaseOffset(); i < end;)
		{	XGAddress adr = new XGAddress(hi, mid, offset);
			XGValue v = this.get(adr);
			if(v != null)
			{	v.setValue(v.decodeMessage(msg));
				size = v.getSize();
			}
			else
			{	LOG.warning("value not found: " + adr);
				size = 1;
			}
			offset += size;
			i += size;
		}
	}

	@Override public String toString()
	{	return this.device + " ValueStore";
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{
	}
}
