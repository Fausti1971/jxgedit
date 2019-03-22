package msg;

import javax.sound.midi.SysexMessage;
import application.MU80;
import parm.XGValue;

public class XGMessageParameterChange extends XGMessage
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7;

/****************************************************************************************************************/

	public XGMessageParameterChange(byte[] array, long time)
	{	super(array);
	}

	public XGMessageParameterChange(XGValue v)
	{	super(new byte[8 + v.getOpcode().getByteCount()]);
		setMessageID();
		setHi(v.getAdress().getHi());
		setMid(v.getAdress().getMid());
		setLo(v.getOpcode().getOffset());
		setData(v);
		setEOX(DATA_OFFS + v.getOpcode().getByteCount());
	}

	public XGMessageParameterChange(SysexMessage msg)
	{	super(msg);
		log.info(this.getClass().toString());
	}

	protected void setHi(int value)
	{	encodeMidiByte(HI_OFFS, value);}

	protected void setMid(int value)
	{	encodeMidiByte(MID_OFFS, value);}

	protected void setLo(int value)
	{	encodeMidiByte(LO_OFFS, value);}

	protected void setData(XGValue v)
	{	encodeXGValue(DATA_OFFS, v);}

	protected int getHi()
	{	return decodeMidiByte(HI_OFFS);}

	protected int getMid()
	{	return decodeMidiByte(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByte(LO_OFFS);}

	public void transmit()
	{	MU80.device.transmit(this);}

	protected void setMessageID()
	{	encodeHigherNibble(MSG_OFFS, MSG);}

	public void processXGMessage()
	{}
}
