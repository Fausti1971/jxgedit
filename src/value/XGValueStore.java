package value;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
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
	{	return super.toString();
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
			{	v.decodeMessage(msg);
				size = v.getSize();
			}
			else
			{	LOG.severe("value not found: " + adr);
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
	{	XGResponse res = req.getResponse();
		int
			hi = res.getAddress().getHi().getValue(),
			mid = res.getAddress().getMid().getValue(),
			lo = res.getAddress().getLo().getValue(),
			end = res.getBulkSize() + res.getBaseOffset(),
			size = 1;

		for(int i = res.getBaseOffset(); i < end;)
		{	XGAddress adr = new XGAddress(hi, mid, lo);
			XGValue v = this.get(adr);
			if(v != null)
			{	v.encodeMessage(res);
				size = v.getSize();
			}
			else
			{	LOG.warning("value not found:"  + adr);
				size = 1;
			}
			lo += size;
			i += size;
		}
		res.setChecksum();
		try
		{	res.checkSum();
		}
		catch(InvalidMidiDataException e)
		{	e.printStackTrace();
		}
		res.getDestination().submit(res);
	}

	@Override public JComponent getConfigComponent()
	{	return null;
	}

	@Override public void close()
	{
	}
}
