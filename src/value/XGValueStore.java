package value;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import device.XGDevice;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGResponse;
import obj.XGType;
import tag.XGTagableSet;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger
{	private final Thread thread;
	private final XGDevice device;

	public XGValueStore(XGDevice dev)
	{	this.device = dev;
		this.thread = new Thread(this);
		this.thread.run();
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
	{	return this.getDevice().toString() + " ValueStore";
	}

	@Override public void submit(XGMessage m) throws InvalidXGAddressException
	{	if(m instanceof XGResponse) this.receiveResponse((XGResponse)m);
	}

	private void receiveResponse(XGResponse r)
	{	int end = r.getBulkSize() + r.getBaseOffset(), offset = r.getLo();
		for(int i = r.getBaseOffset(); i < end;)
		{	XGValue v;
			try
			{
				v = XGValue.factory(r.getSource(), new XGAddress(r.getHi(), r.getMid(), offset));
				this.add(v);
				r.decodeXGValue(i, v);
				offset += v.getOpcode().getByteCount();
				i += v.getOpcode().getByteCount();
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
			}
		}
	}

	@Override public void run()
	{
	}

}
