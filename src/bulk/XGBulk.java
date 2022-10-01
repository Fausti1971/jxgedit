package bulk;

import adress.InvalidXGAddressException;
import adress.XGAddress;import adress.XGAddressRange;import adress.XGAddressable;
import application.XGLoggable;import module.XGModule;import msg.*;
import tag.XGTagable;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;

public class XGBulk implements XGTagable, XGAddressable, XGMessenger, XGLoggable
{
	public static XGBulk newBulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	if(type.tag.equals("ins48")) return new XGInsertion48Bulk(type, mod);
		else return new XGBulk(type, mod);
	}

/**********************************************************************************************************************/

	private final XGBulkType type;
	final XGModule module;
	private final XGAddress address;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	protected XGBulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	this.type = type;
		this.module = mod;
		this.address = new XGAddress(type.addressRange.getHi().getValue(), mod.getID(), type.addressRange.getLo().getMin());
	}

	public XGBulkType getType(){	return this.type;}

	public int getSize(){	return this.type.addressRange.getLo().getSize();}

	//public void transmit(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException
	//{	dest.submit(this.message);
	//	this.message.setChecksum();
	//	}

	//public boolean request(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException, InvalidMidiDataException
	//{	XGMessageBulkRequest request = new XGMessageBulkRequest(this, this);
	//	dest.submit(request);
	//	return request.isResponsed();
	//}

	@Override public String getTag(){	return this.type.getTag();}

	public XGTagableAddressableSet<XGValue> getValues(){	return this.values;}

	public XGModule getModule(){	return this.module;}

	public XGMessageBulkDump getMessage()throws InvalidMidiDataException
	{	XGMessageBulkDump res = new XGMessageBulkDump(this, this);
		int baseOffset = res.getBaseOffset();
		for(XGValue v : this.values)
		{	v.getCodec().encode(res, baseOffset + (v.getAddress().getLoValue() - this.address.getLoValue()), v.getSize(), v.getValue());
		}
		res.setChecksum();
		return res;
	}

	@Override public XGAddress getAddress(){	return this.address;}

	@Override public void submit(XGResponse res)throws XGMessengerException
	{	if(res instanceof XGMessageParameterChange) this.values.get(res.getAddress()).submit(res);
		else if(res instanceof XGMessageBulkDump)
			for(XGValue v : this.values)
				v.submit(res);

		else throw new XGMessengerException(this, res);
	}

	@Override public void submit(XGRequest req) throws XGMessengerException
	{	if(req instanceof XGMessageBulkRequest)
		{	try
			{	XGMessageBulkDump res = this.getMessage();
				if(req.setResponsedBy(res)) req.getSource().submit(res);
			}
			catch(InvalidMidiDataException e)
			{	throw new XGMessengerException(e.getMessage());
			}
		}
		else if(req instanceof XGMessageParameterRequest) this.values.get(req.getAddress()).submit(req);
		else throw new XGMessengerException(this, req);
	}

	@Override public void close(){}

	@Override public String toString(){	return this.getClass().getSimpleName() + " " + this.address;}
}
