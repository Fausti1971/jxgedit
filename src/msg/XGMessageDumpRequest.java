package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import application.MU80;
import obj.XGAdress;

public class XGMessageDumpRequest extends XGMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20;

	XGMessageBulkDump response = null;

	public XGMessageDumpRequest(byte[] array, long time) throws InvalidMidiDataException
	{	super(array);
	}

	public XGMessageDumpRequest(XGAdress adr)
	{	super(new byte[8]);
		setSysexId(MU80.device.getSysexId());
		setMessageID();
		encodeMidiByte(HI_OFFS, adr.getHi());
		encodeMidiByte(MID_OFFS, adr.getMid());
		encodeMidiByte(LO_OFFS, adr.getLo());
		setEOX(7);
		this.response = new XGMessageBulkDump(adr);
	}

	public XGMessageDumpRequest(SysexMessage msg)
	{	super(msg);
	}

	public boolean isResponsed(XGMessage msg)
	{	if(msg == null) return false;
		if(msg instanceof XGMessageBulkDump)
		{	XGMessageBulkDump x = (XGMessageBulkDump)msg;
			if(x.getSysexId() == response.getSysexId() &&
				x.getHi() == response.getHi() &&
				x.getMid() == response.getMid() &&
				x.getLo() == response.getLo()) return true;
		}
		return false;
	}

	public XGMessageBulkDump getResponse()
	{	return response;}

	protected int getHi()
	{	return decodeMidiByte(HI_OFFS);}

	protected int getMid()
	{return decodeMidiByte(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByte(LO_OFFS);}

	protected void setHi(int hi)
	{	encodeMidiByte(HI_OFFS, hi);}

	protected void setMid(int mid)
	{	encodeMidiByte(MID_OFFS, mid);}

	protected void setLo(int lo)
	{	encodeMidiByte(LO_OFFS, lo);}

	@Override public String toString()
	{	return "requesting hi: " + getHi() + " mid: " + getMid() + " lo: " + getLo();}

	protected void setMessageID()
	{	encodeHigherNibble(MSG_OFFS, MSG);}

	public void processXGMessage()
	{}
}