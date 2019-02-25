package msg;

import javax.sound.midi.SysexMessage;
import application.MU80;
import parm.Opcode;
import parm.XGParameter;

public class XGMessageParameterChange extends XGMessage
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7;

/****************************************************************************************************************/

	public XGMessageParameterChange(byte[] array, long time)
	{	super(array);
	}

	public XGMessageParameterChange(XGParameter p)
	{	super(new byte[8 + p.getOpcode().getByteCount()]);
		setMessageID();
		setHi(p.getXGObject().getAdr().getHi());
		setMid(p.getXGObject().getAdr().getMid());
		setLo(p.getOpcode().getOffset());
		setData(p.getOpcode());
		setEOX(DATA_OFFS + p.getOpcode().getByteCount());
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

	protected void setData(Opcode opc)
	{	encodeOpcode(DATA_OFFS, opc);}

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
