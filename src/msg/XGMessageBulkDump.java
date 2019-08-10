package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import value.XGValue;

public class XGMessageBulkDump extends XGMessage
{	private static final int SIZE_SIZE = 2, SIZE_OFFS = 4, MSG = 0, HI_OFFS = 6, MID_OFFS = 7, LO_OFFS = 8, DATA_OFFS = 9;

	protected XGMessageBulkDump(byte[] array) throws InvalidMidiDataException
	{	super(array);
		checkSum();
	}

	public XGMessageBulkDump(XGAdress adr) throws InvalidXGAdressException	//wird als response benötigt
	{	super(new byte[11]);
		setMessageId(MSG);
		setDumpSize(0);
		encodeMidiByteFromInteger(HI_OFFS, adr.getHi());
		encodeMidiByteFromInteger(MID_OFFS, adr.getMid());
		encodeMidiByteFromInteger(LO_OFFS, adr.getLo());
		setEOX(10);
	}

	public XGMessageBulkDump(SysexMessage msg) throws InvalidMidiDataException, InvalidXGAdressException	//für MIDI und FILE
	{	super(msg);
		checkSum();
	}

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

	private int getDumpSize()
	{	return decodeMidiBytesToInteger(SIZE_OFFS, SIZE_SIZE);}

	private void setDumpSize(int size)
	{	encodeMidiBytesFromInteger(SIZE_OFFS, SIZE_SIZE, size);}

	protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);}

	private void checkSum() throws InvalidMidiDataException
	{	if(((calcChecksum(SIZE_OFFS, DATA_OFFS + getDumpSize()) & 0x7F) != 0)) throw new InvalidMidiDataException("Checksum Error!");}

	public void storeMessage() throws InvalidXGAdressException
	{	int end = getDumpSize() + DATA_OFFS, offset = getLo();
		for(int i = DATA_OFFS; i < end;)
		{	XGValue v = XGValue.getValueOrNewAndStore(new XGAdress(getHi(), getMid(), offset));
			decodeXGValue(i, v);
			offset += v.getParameter().getByteCount();
			i += v.getParameter().getByteCount();
		}
	}
}
