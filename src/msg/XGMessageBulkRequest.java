package msg;

import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGMessageBulkRequest extends XGSuperMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20, OVERHAED = 8;

/********************************************************************************/

	private boolean responsed = false;
	XGResponse response = null;

	public XGMessageBulkRequest(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException, InvalidXGAddressException
	{	super(src, dest, array, init);
		XGAddress adr = new XGAddress(array[HI_OFFS], array[MID_OFFS], array[LO_OFFS]);
		this.setAddress(adr);
		this.setResponse(new XGMessageBulkDump(dest, src, adr));
	}

	public XGMessageBulkRequest(XGMessenger src, XGMessenger dest, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, dest, new byte[OVERHAED], true);
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getMin());
		this.setResponse(new XGMessageBulkDump(dest, src, adr));
	}

//	public XGMessageDumpRequest(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
//	{	super(src, dest, msg);
//	}

	public XGMessageBulkRequest(XGMessenger src, XGMessenger dest, XGBulkDump dump) throws InvalidMidiDataException, InvalidXGAddressException
	{	this(src, dest, dump.getAddress());
		this.setResponse(new XGMessageBulkDump(src, dest, dump.getAddress()));
	}

	@Override public boolean isResponsed()
	{	return this.responsed;
	}

	@Override public void setResponsed(boolean s)
	{	this.responsed = s;
	}

	@Override public XGResponse getResponse()
	{	return this.response;
	}

	@Override public void setResponse(XGResponse m)
	{	this.response = m;
		m.setDestination(this.getSource());
	}

	@Override public int getHi()
	{	return decodeLSB(HI_OFFS);
	}

	@Override public int getMid()
	{return decodeLSB(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeLSB(LO_OFFS);
	}

	@Override public void setHi(int hi)
	{	encodeLSB(HI_OFFS, hi);
	}

	@Override public void setMid(int mid)
	{	encodeLSB(MID_OFFS, mid);
	}

	@Override public void setLo(int lo)
	{	encodeLSB(LO_OFFS, lo);
	}

	@Override public void setMessageID()
	{	this.setMessageID(MSG);
	}
}