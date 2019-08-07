package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import application.MU80;
import value.WrongXGValueTypeException;
import value.XGValue;

public class XGMessageParameterRequest extends XGMessage implements XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

	private XGMessage response;

	protected XGMessageParameterRequest(byte[] array, long time)
	{	super(array);}

	public XGMessageParameterRequest(SysexMessage msg) throws InvalidMidiDataException
	{	super(msg);}

	public XGMessageParameterRequest(XGAdress adr) throws InvalidXGAdressException, WrongXGValueTypeException
	{	super(new byte[8]);
		setMessageId(MSG);
		setSysexId(MU80.device.getSysexId());
		setHi(adr.getHi());
		setMid(adr.getMid());
		setLo(adr.getLo());
		setEOX(7);
		this.response = new XGMessageParameterChange(XGValue.getValueOrNew(adr));
	}

	protected int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);}

	protected int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);}

	protected void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);}

	protected void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);}

	protected void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);}

	public boolean setResponsedBy(XGMessage msg)
	{	if(msg == null) return false;
		if(msg instanceof XGMessageParameterChange)
		{	XGMessageParameterChange x = (XGMessageParameterChange)msg;
			if(x.getSysexId() == response.getSysexId() && x.getAdress().equals(response.getAdress())) return true;
		}
		return false;
	}

	protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	public void storeMessage()
	{}

	public boolean isResponsed()
	{	return false;
	}

	public XGMessage getResponse()
	{	return this.response;
	}
}
