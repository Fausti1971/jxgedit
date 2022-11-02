package bulk;

import adress.XGInvalidAddressException;
import adress.XGAddress;import adress.XGAddressable;
import application.XGLoggable;import module.XGModule;import msg.*;
import tag.XGTagable;
import tag.XGTagableAddressableSet;
import value.XGFixedValue;import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;

public class XGBulk implements XGTagable, XGAddressable, XGMessenger, XGLoggable
{

/**********************************************************************************************************************/

	private final XGBulkType type;
	final XGModule module;
	private final XGAddress address;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	public XGBulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, XGInvalidAddressException
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

	@Override public void submit(XGMessageParameterChange res)throws XGMessengerException
	{	this.values.get(res.getAddress()).submit(res);
	}

	@Override public void submit(XGMessageBulkDump res)throws XGMessengerException
	{	for(XGValue v : this.values)
			v.submit(res);
	}

	@Override public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{	try
		{	XGMessageBulkDump res = this.getMessage();
			if(req.setResponsedBy(res)) req.getSource().submit(res);
		}
		catch(InvalidMidiDataException e)
		{	throw new XGMessengerException(e.getMessage());
		}
	}

	@Override public void submit(XGMessageParameterRequest req) throws XGMessengerException
	{	this.values.get(req.getAddress()).submit(req);
	}

	@Override public void close(){}

	@Override public String toString(){	return this.getClass().getSimpleName() + " " + this.address;}
}
