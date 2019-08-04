package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import application.MU80;

public class XGMessageDumpRequest extends XGMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20;

	XGMessageBulkDump response = null;

	public XGMessageDumpRequest(byte[] array, long time) throws InvalidMidiDataException
	{	super(array);
	}

	public XGMessageDumpRequest(XGAdress adr) throws InvalidXGAdressException
	{	super(new byte[8]);
		setSysexId(MU80.device.getSysexId());
		setMessageID();
		encodeMidiByteFromInteger(HI_OFFS, adr.getHi());
		encodeMidiByteFromInteger(MID_OFFS, adr.getMid());
		encodeMidiByteFromInteger(LO_OFFS, adr.getLo());
		setEOX(7);
		this.response = new XGMessageBulkDump(adr);
	}

	public XGMessageDumpRequest(SysexMessage msg) throws InvalidMidiDataException
	{	super(msg);
	}

	public boolean isResponsed(XGMessage msg)
	{	if(msg == null) return false;
		if(msg instanceof XGMessageBulkDump)
		{	XGMessageBulkDump x = (XGMessageBulkDump)msg;
			if(x.getSysexId() == response.getSysexId() && x.getAdress().equals(response.getAdress())) return true;
		}
		return false;
	}

	public XGMessageBulkDump getResponse()
	{	return response;}

	protected int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);}

	protected int getMid()
	{return decodeMidiByteToInteger(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);}

	protected void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);}

	protected void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);}

	protected void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);}

	@Override public String toString()
	{	return "requesting hi: " + getHi() + " mid: " + getMid() + " lo: " + getLo();}

	protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);}

	public void storeMessage()
	{}
}