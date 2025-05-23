package bulk;

import adress.XGInvalidAddressException;
import adress.XGAddress;import adress.XGAddressable;
import application.XGLoggable;import module.XGModule;import msg.*;
import tag.XGTagable;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;

public class XGBulk implements XGTagable, XGAddressable, XGMessenger, XGLoggable
{

/**********************************************************************************************************************/

	private final XGBulkType type;
	final XGModule module;
	private final XGAddress address;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	public XGBulk(XGBulkType type, XGModule mod)throws  XGInvalidAddressException
	{	this.type = type;
		this.module = mod;
		this.address = new XGAddress(type.addressRange.getHi().getValue(), mod.getID(), type.addressRange.getLo().getMin());
	}

	public XGBulkType getType(){	return this.type;}

	public int getSize(){	return this.type.addressRange.getLo().getSize();}

	@Override public String getTag(){	return this.type.getTag();}

	public XGTagableAddressableSet<XGValue> getValues(){	return this.values;}

	public XGModule getModule(){	return this.module;}

	public XGMessageBulkDump getMessage()throws InvalidMidiDataException
	{	XGMessageBulkDump res = new XGMessageBulkDump(this, this);
		int baseoffset = res.getBaseOffset() - this.address.getLoValue();
		for(XGValue v : this.values)
		{	v.getCodec().encode(res, baseoffset + v.getAddress().getLoValue(), v.getSize(), v.getValue());
		}
		res.setChecksum();
		return res;
	}

	@Override public XGAddress getAddress(){	return this.address;}

	@Override public void submit(XGResponse res)throws XGMessengerException
	{	if(res instanceof XGMessageParameterChange) this.values.get(res.getAddress()).submit(res);
		if(res instanceof XGMessageBulkDump)
			for(XGValue v : this.values) v.submit(res);
	}

	@Override public void request(XGRequest req) throws XGMessengerException
	{	if(req instanceof XGMessageBulkRequest)
		{	try
			{	XGMessageBulkDump res = this.getMessage();
				if(req.setResponsedBy(res)) req.getSource().submit(res);
			}
			catch(InvalidMidiDataException e)
			{	throw new XGMessengerException(e.getMessage());
			}
		}
		if(req instanceof XGMessageParameterRequest)
			this.values.get(req.getAddress()).request(req);
	}

	@Override public void close(){}

	@Override public String toString(){	return this.getClass().getSimpleName() + " " + this.address;}
}
