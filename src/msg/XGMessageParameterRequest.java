package msg;

import javax.sound.midi.SysexMessage;

public class XGMessageParameterRequest extends XGMessage implements XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

	private XGMessage response;

	protected XGMessageParameterRequest(byte[] array, long time)
	{	super(array);}

	public XGMessageParameterRequest(SysexMessage msg)
	{	super(msg);}
/*
	public XGMessageParameterRequest(int sysexId, int hi, int mid, int lo)	//TODO entferne diesen Konstruktor wenn XGParamemter steht...
	{	super(new byte[8]);
		setMessageId(MSG);
		setSysexId(sysexId);
		setHi(hi);
		setMid(mid);
		setLo(lo);
		setEOX(7);
		this.response = new XGMessageParameterChange(sysexId, hi, mid, lo, 0, 0);
	}
*/
	protected int getHi()
	{	return decodeMidiByte(HI_OFFS);}

	protected int getMid()
	{	return decodeMidiByte(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByte(LO_OFFS);}

	protected void setHi(int hi)
	{	encodeMidiByte(HI_OFFS, hi);}

	protected void setMid(int mid)
	{	encodeMidiByte(MID_OFFS, mid);}

	protected void setLo(int lo)
	{	encodeMidiByte(LO_OFFS, lo);}

	public boolean isResponsed(XGMessage msg)
	{	if(msg == null) return false;
		if(msg instanceof XGMessageParameterChange)
		{	XGMessageParameterChange x = (XGMessageParameterChange)msg;
			if(x.getSysexId() == response.getSysexId() && x.getAdress().equals(response.getAdress())) return true;
		}
		return false;
	}

	protected void setMessageID()
	{	encodeHigherNibble(MSG_OFFS, MSG);
	}

	public void processXGMessage()
	{}
}
