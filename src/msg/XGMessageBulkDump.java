package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import application.InvalidXGAdressException;
import obj.XGAdress;
import obj.XGObject;
import parm.XGValue;

public class XGMessageBulkDump extends XGMessage
{	private static final int SIZE_SIZE = 2, SIZE_OFFS = 4, MSG = 0, HI_OFFS = 6, MID_OFFS = 7, LO_OFFS = 8, DATA_OFFS = 9;

	public XGMessageBulkDump(byte[] array) throws InvalidMidiDataException
	{	super(array);
		if(((calcChecksum(SIZE_OFFS, DATA_OFFS + getDumpSize()) & 0x7F) != 0)) throw new InvalidMidiDataException("Checksum Error!");
	}
/*
	public XGMessageBulkDump(XGBulkDump dump)
	{	super(new byte[11 + dump.getData().length]);
		int dataSize = dump.getData().length;
		setMessageId(MSG);
		set2MidiByte(SIZE_OFFS, dataSize);
		set1MidiByte(HI_OFFS, dump.getHi());
		set1MidiByte(MID_OFFS, dump.getMid());
		set1MidiByte(LO_OFFS, dump.getLo());
		setByteArray(DATA_OFFS, dump.getData());
		set1MidiByte(DATA_OFFS + dataSize, 0 - dump.calcChecksum(SIZE_OFFS, DATA_OFFS + dataSize)); //TODO Checksumcalc
		setEOX(10 + dataSize);
	}
*/
	public XGMessageBulkDump(XGAdress adr) throws InvalidXGAdressException	//wird als response benötigt
	{	super(new byte[11]);
		setMessageId(MSG);
		setDumpSize(0);
		encodeMidiByte(HI_OFFS, adr.getHi());
		encodeMidiByte(MID_OFFS, adr.getMid());
		encodeMidiByte(LO_OFFS, adr.getLo());
		setEOX(10);
	}

	public XGMessageBulkDump(SysexMessage msg)
	{	super(msg);}

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

	private int getDumpSize()
	{	return decodeMidiBytes(SIZE_OFFS, SIZE_SIZE);}

	private void setDumpSize(int size)
	{	encodeMidiBytes(SIZE_OFFS, SIZE_SIZE, size);}

	protected void setMessageID()
	{	encodeHigherNibble(MSG_OFFS, MSG);}

	public void processXGMessage() throws InvalidXGAdressException
	{	int end = getDumpSize() + DATA_OFFS, offset = getLo();
		XGObject obj = XGObject.getXGObjectInstance(new XGAdress(getHi(), getMid(), offset));
		for(int i = DATA_OFFS; i < end;)
		{	XGValue v = obj.getXGValue(offset);
			decodeXGValue(i, v);
			offset += v.getByteCount();
			i += v.getByteCount();
		}
	}
}
