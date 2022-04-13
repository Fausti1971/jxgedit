package msg;

import javax.sound.midi.InvalidMidiDataException;import javax.swing.*;
import adress.InvalidXGAddressException;
import adress.XGAddress;import gui.XGMainWindow;import module.XGBulk;import value.XGValue;

public class XGMessageBulkDump extends XGSuperMessage implements XGResponse
{
	private static final int OVERHAED = 11, SIZE_SIZE = 2, SIZE_OFFS = 4, MSG = 0, HI_OFFS = 6, MID_OFFS = 7, LO_OFFS = 8, DATA_OFFS = 9;

/********************************************************************************************************************************************************/

	XGMessageBulkDump(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
		this.checkSum();
	}

	public XGMessageBulkDump(XGMessenger src, XGBulk blk) throws InvalidXGAddressException, InvalidMidiDataException //wird manuell angelegt und als response benötigt
	{	this(src, blk.getAddress());
	}

	public XGMessageBulkDump(XGMessenger src, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException //wird manuell angelegt und als response benötigt
	{	super(src, new byte[OVERHAED + adr.getLo().getSize()], true);
		this.setBulkSize(adr.getLo().getSize());
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getMin());
		this.setChecksum();
	}

	public  XGMessageBulkDump(XGMessenger src, XGValue val)throws InvalidMidiDataException, InvalidXGAddressException// für "bulkende" Values
	{	this(src, new XGAddress(val.getAddress().getHi(), val.getAddress().getMid(), val.getType().getAddress().getLo()));
		val.getCodec().encode(this, this.getBaseOffset(), this.getBulkSize(), val.getValue());
		this.setChecksum();
	}

	@Override public int getHi(){	return decodeLSB(HI_OFFS);}

	@Override public int getMid(){	return decodeLSB(MID_OFFS);}

	@Override public int getLo(){	return decodeLSB(LO_OFFS);}

	@Override public void setHi(int hi){	encodeLSB(HI_OFFS, hi);}

	@Override public void setMid(int mid){	encodeLSB(MID_OFFS, mid);}

	@Override public void setLo(int lo){	encodeLSB(LO_OFFS, lo);}

	@Override public int getBulkSize(){	return decodeLSB(SIZE_OFFS, SIZE_SIZE);}

	@Override public void setBulkSize(int size){	encodeLSB(SIZE_OFFS, SIZE_SIZE, size);}

/**
 * The checksum shall be set such that the low-order 7 bits of the sum of the Byte Count, the Address, the Data, and the Checksum itself are 0.
 * For details about support for reception of block-unit bulk dumps, see Attached Chart 5.
 */
	@Override public void checkSum() throws InvalidMidiDataException
	{	int sum = this.calcChecksum(SIZE_OFFS, DATA_OFFS + this.getBulkSize());
		if((sum & LSB) != 0)
		{	JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, "checksum error @ " + this.getAddress());
			throw new InvalidMidiDataException("checksum error @ " + this.getAddress());
		}
	}

/**
* The checksum shall be set such that the low-order 7 bits of the sum of the Byte Count, the Address, the
  * Data, and the Checksum itself are 0. For details about support for reception of block-unit bulk dumps,
  * see Attached Chart 5.
*/
	@Override public void setChecksum()
	{	int pos = DATA_OFFS + this.getBulkSize();//checksum-offset
		int sum = this.calcChecksum(SIZE_OFFS, pos - 1);//Berechnung erstmal ohne checksum-offset, da diese erst errechnet und gesetzt werden muss
		super.encodeLSB(pos, (- sum) & LSB);
	}

	@Override public int getBaseOffset(){	return DATA_OFFS;}

	@Override public void setMessageID(){	this.setMessageID(MSG);}
}
