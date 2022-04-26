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
	volatile XGMessageBulkDump message;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	protected XGBulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	this.type = type;
		this.module = mod;
		this.address = new XGAddress(type.addressRange.getHi().getValue(), mod.getID(), type.addressRange.getLo().getMin());
		this.message = new XGMessageBulkDump(this, this);
	}

	public XGBulkType getType(){	return this.type;}

	public int getSize(){	return this.type.addressRange.getLo().getSize();}

	public void transmit(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException
	{	dest.submit(this.message);
		this.message.setChecksum();
		}

	public boolean request(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException, InvalidMidiDataException
	{	XGMessageBulkRequest request = new XGMessageBulkRequest(this, this);
		dest.submit(request);
		return request.isResponsed();
	}

	@Override public String getTag(){	return this.type.getTag();}

	public XGTagableAddressableSet<XGValue> getValues(){	return this.values;}

	public XGModule getModule(){	return this.module;}

	private void setMessage(XGMessageBulkDump m)
	{	this.message = m;
		for(XGValue v : this.values) v.contentChanged(v);
	}

	public XGMessageBulkDump getMessage(){	return this.message;}

/**
* returniert die Bulkadresse, d.h. lo ist nicht fixed! für fixed Adresse nimm bulk.getMessage().getAddress()
*/
	@Override public XGAddress getAddress(){	return this.address;}

	@Override public void submit(XGResponse res)
	{	if(res instanceof XGMessageBulkDump) this.setMessage((XGMessageBulkDump)res);
	}

	public void submit(XGRequest req) throws XGMessengerException
	{	if(req instanceof XGMessageBulkRequest)
		{	XGMessageBulkRequest r = (XGMessageBulkRequest)req;
			if(req.setResponsedBy(this.message)) req.getSource().submit(this.message);
		}
	}

	@Override public void close(){}

	@Override public String toString(){	return this.getClass().getSimpleName() + " " + this.message.getAddress();}
}
