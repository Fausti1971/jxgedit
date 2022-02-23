package msg;

import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import value.XGValue;

public class XGMessageParameterChange extends XGSuperMessage implements XGResponse
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7, OVERHEAD = 8;

/**************************************************************************************************************/

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, dest, array, init);
	}

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, XGValue v) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, dest, new byte[OVERHEAD + v.getSize()], true);
		this.setMessageID(MSG);
		XGAddress adr = v.getAddress();
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
		v.encodeMessage(this);
		this.setEOX();
	}

	@Override public void setHi(int value){	encodeLSB(HI_OFFS, value);}

	@Override public void setMid(int value){	encodeLSB(MID_OFFS, value);}

	@Override public void setLo(int value){	encodeLSB(LO_OFFS, value);}

	@Override public int getHi(){	return decodeLSB(HI_OFFS);}

	@Override public int getMid(){	return decodeLSB(MID_OFFS);}

	@Override public int getLo(){	return decodeLSB(LO_OFFS);}

	@Override public int getBulkSize(){	return this.data.length - OVERHEAD;}

	@Override public void setBulkSize(int size){}

	@Override public int getBaseOffset(){	return DATA_OFFS;}

	@Override public void checkSum() throws InvalidMidiDataException{}

	@Override public void setChecksum(){}

	@Override public void setMessageID(){	this.setMessageID(MSG);}
}
