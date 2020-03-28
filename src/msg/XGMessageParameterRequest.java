package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import module.XGModuleNotFoundException;
import value.XGValue;

public class XGMessageParameterRequest extends XGSuperMessage implements XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

/*******************************************************************************************/

	private XGResponse response;
	private boolean responsed;

	protected XGMessageParameterRequest(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, dest, array, init);
	}

	public XGMessageParameterRequest(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, dest, msg);
	}

	public XGMessageParameterRequest(XGMessenger src, XGMessenger dest, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException, XGModuleNotFoundException
	{	super(src, dest, new byte[8], true);
		this.setMessageID(MSG);
		this.setHi(adr.getHi());
		this.setMid(adr.getMid());
		this.setLo(adr.getLo());
		this.setEOX(7);
		this.response = new XGMessageParameterChange(dest, src, new XGValue(src, adr));
	}

	@Override public int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);}

	@Override public int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);}

	@Override public int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);}

	@Override public void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);}

	@Override public void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);}

	@Override public void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);}


	@Override public void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	@Override public boolean getResponsed()
	{	return this.responsed;
	}

	@Override public XGResponse getResponse()
	{	return this.response;
	}

	@Override public void setResponsed(boolean s)
	{	this.responsed = s;
	}

	@Override public void setResponse(XGMessage m)
	{	this.response = (XGResponse)m;
	}
}
