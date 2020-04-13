package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGMessageBulkDump extends XGSuperMessage implements XGResponse
{
	private static final int OVERHAED = 11, SIZE_SIZE = 2, SIZE_OFFS = 4, MSG = 0, HI_OFFS = 6, MID_OFFS = 7, LO_OFFS = 8, DATA_OFFS = 9;

/********************************************************************************************************************************************************/

	XGMessageBulkDump(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, dest, array, init);
		this.checkSum();
	}

	public XGMessageBulkDump(XGMessenger src, XGMessenger dest, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException //wird manuell angelegt und als response benötigt
	{	super(src, dest, new byte[OVERHAED + adr.getLo().getSize()], true);
		this.setMessageID(MSG);
		this.setBulkSize(adr.getLo().getSize());
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getMin());
		this.setChecksum();
		this.setEOX(10);
	}

	public XGMessageBulkDump(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException, InvalidXGAddressException	//für MIDI und FILE
	{	super(src, dest, msg);
		this.checkSum();
	}
/*
	private XGMessageBulkDump(XGMessenger src, XGMessenger dest, XGBulkDump blk) throws InvalidXGAddressException
	{	super(src, dest, new byte[OVERHAED + blk.getAddress().getLo().getSize()], true);
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
	{	return decodeLSB(HI_OFFS);
	}

	@Override public int getMid()
	{return decodeLSB(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeLSB(LO_OFFS);
	}

	@Override public void setHi(int hi)
	{	encodeLSB(HI_OFFS, hi);
	}

	@Override public void setMid(int mid)
	{	encodeLSB(MID_OFFS, mid);
	}

	@Override public void setLo(int lo)
	{	encodeLSB(LO_OFFS, lo);
	}

	@Override public int getBulkSize()
	{	return decodeLSB(SIZE_OFFS, SIZE_SIZE);
	}

	@Override public void setBulkSize(int size)
	{	encodeLSB(SIZE_OFFS, SIZE_SIZE, size);
	}

	@Override public void checkSum() throws InvalidMidiDataException
	{	if(((calcChecksum(SIZE_OFFS, DATA_OFFS + this.getBulkSize()) & LSB) != 0)) throw new InvalidMidiDataException("checksum error!");
	}

	@Override public int setChecksum()
	{	int size = this.getBulkSize();
		int sum = 0 - this.calcChecksum(SIZE_OFFS, DATA_OFFS + size - 1);
		this.encodeLSB(DATA_OFFS + size, sum);
		return sum;
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
