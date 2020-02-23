package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGMessageBulkDump extends XGSuperMessage implements XGResponse
{
	private static final int OVERHAED = 11, SIZE_SIZE = 2, SIZE_OFFS = 4, MSG = 0, HI_OFFS = 6, MID_OFFS = 7, LO_OFFS = 8, DATA_OFFS = 9;

/********************************************************************************************************************************************************/

	XGMessageBulkDump(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
		this.checkSum();
	}

	public XGMessageBulkDump(XGMessenger src, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException //wird manuell angelegt und als response benötigt
	{	super(src, new byte[OVERHAED], true);
		this.setMessageID(MSG);
		this.setBulkSize(0);
		this.setHi(adr.getHi());
		this.setMid(adr.getMid());
		this.setLo(adr.getLo());
		this.setEOX(10);
	}

	public XGMessageBulkDump(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException, InvalidXGAddressException	//für MIDI und FILE
	{	super(src, msg);
		this.checkSum();
	}
/*
	private XGMessageBulkDump(XGMessenger src, XGBulkDump blk) throws InvalidXGAddressException
	{	super(src, new byte[OVERHAED + blk.getBulkSize()]);
		this.setMessageID();
		this.setHi(blk.getAdress().getHi());
		this.setMid(blk.getAdress().getMid());
		this.setLo(blk.getAdress().getLo());
		this.setBulkSize(blk.getBulkSize());
		this.encodeByteArray(DATA_OFFS, blk.getBulkData());
		this.encodeMidiByteFromInteger(DATA_OFFS + blk.getBulkSize(), 0 - this.calcChecksum(SIZE_OFFS, DATA_OFFS + blk.getBulkSize() - 1));
		this.setEOX(this.getByteArray().length - 1);
	}
*/
	@Override public int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);
	}

	@Override public int getMid()
	{return decodeMidiByteToInteger(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);
	}

	@Override public void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);
	}

	@Override public void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);
	}

	@Override public void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);
	}

	@Override public int getBulkSize()
	{	return decodeMidiBytesToInteger(SIZE_OFFS, SIZE_SIZE);
	}

	@Override public void setBulkSize(int size)
	{	encodeMidiBytesFromInteger(SIZE_OFFS, SIZE_SIZE, size);
	}

	@Override public void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	@Override public void checkSum() throws InvalidMidiDataException
	{	if(((calcChecksum(SIZE_OFFS, DATA_OFFS + this.getBulkSize()) & MIDIBYTEMASK) != 0)) throw new InvalidMidiDataException("checksum error!");
	}

	@Override public int setChecksum()
	{	return 0;	//TODO:
	}

	@Override public int getBaseOffset()
	{	return DATA_OFFS;
	}
/*
	@Override public XGAddressableSet<XGValue> getValues() throws InvalidXGAddressException
	{	XGAddressableSet<XGValue> set = new XGAddressableSet<XGValue>();
		int end = getBulkSize() + DATA_OFFS, offset = getLo();
		for(int i = DATA_OFFS; i < end;)
		{	XGValue v = XGValue.factory(this.getSource(), new XGAddress(getHi(), getMid(), offset));
			set.add(v);
			decodeXGValue(i, v);
			offset += v.getOpcode().getByteCount();
			i += v.getOpcode().getByteCount();
		}
		return set;
	}
*/
}
