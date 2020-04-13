package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGMessageDumpRequest extends XGSuperMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20;

/********************************************************************************/

	private boolean responsed = false;

	XGResponse response = null;

	public XGMessageDumpRequest(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, dest, array, init);
	}

	public XGMessageDumpRequest(XGMessenger src, XGMessenger dest, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, dest, new byte[8], true);
		this.setMessageID(MSG);
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
		this.setEOX(7);
		this.response = new XGMessageBulkDump(dest, src, adr);
	}

	public XGMessageDumpRequest(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, dest, msg);
	}

	@Override public boolean getResponsed()
	{	return this.responsed;
	}

	@Override public void setResponsed(boolean s)
	{	this.responsed = s;
	}

	@Override public XGResponse getResponse()
	{	return this.response;
	}

	@Override public void setResponse(XGMessage m)
	{	this.response = (XGResponse)m;
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

	@Override public String toString()
	{	return this.getAddress().toString();
	}
}