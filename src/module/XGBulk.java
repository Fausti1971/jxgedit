package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;import adress.XGAddressable;
import application.XGLoggable;import msg.*;
import tag.XGTagable;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;

public class XGBulk implements XGTagable, XGAddressable, XGMessenger, XGLoggable
{
	private final XGBulkType type;
	private final XGModule module;
	private final XGAddress address;
	private volatile XGMessageBulkDump message;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	public XGBulk(XGBulkType type, XGModule mod)throws InvalidXGAddressException, InvalidMidiDataException
	{	this.type = type;
		this.module = mod;
		this.address = type.getAddress().complement(mod.getAddress());
		this.message = new XGMessageBulkDump(this, this);
	}

	public XGBulkType getType(){	return this.type;}

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

	@Override public void submit(XGResponse res) throws InvalidXGAddressException, XGMessengerException
	{	if(res instanceof XGMessageBulkDump) this.setMessage((XGMessageBulkDump)res);
	}

	public void submit(XGRequest req) throws InvalidXGAddressException, XGMessengerException
	{	if(req instanceof XGMessageBulkRequest)
		{	XGMessageBulkRequest r = (XGMessageBulkRequest)req;
			if(req.setResponsedBy(this.message)) req.getSource().submit(this.message);
		}
	}

	@Override public void close()
	{
	}
}
