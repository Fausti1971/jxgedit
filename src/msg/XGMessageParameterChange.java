package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import value.XGValue;

public class XGMessageParameterChange extends XGSuperMessage implements XGResponse
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7, OVERHEAD = 8;

/**
 * @param init 
 * @throws InvalidMidiDataException
 */

/**************************************************************************************************************/

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, dest, array, init);
	}

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, XGValue v) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, dest, new byte[OVERHEAD + v.getOpcode().getDataSize()], true);
		this.setMessageID(MSG);
		XGAddress adr = v.getAddress();
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
		v.encodeMessage(this, v.getContent());
		this.setEOX(DATA_OFFS + v.getOpcode().getDataSize());
	}

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, dest, msg);
		log.info(this.getClass().toString());
	}

	@Override public void setHi(int value)
	{	encodeLSB(HI_OFFS, value);
	}

	@Override public void setMid(int value)
	{	encodeLSB(MID_OFFS, value);
	}

	@Override public void setLo(int value)
	{	encodeLSB(LO_OFFS, value);
	}

	@Override public int getHi()
	{	return decodeLSB(HI_OFFS);
	}

	@Override public int getMid()
	{	return decodeLSB(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeLSB(LO_OFFS);
	}

	@Override public int getBulkSize()
	{	XGAddress adr = this.getAddress();
		return this.getSource().getDevice().getOpcodes().get(adr).getDataSize();
	}

	@Override public void setBulkSize(int size)
	{	//überflüssig für ParameterChange
	}

	@Override public int getBaseOffset()
	{	return DATA_OFFS;
	}

	@Override public void checkSum() throws InvalidMidiDataException
	{	//überflüssig für ParameterChange
	}

	@Override public int setChecksum()
	{	//überflüssig für ParameterChange
		return 0;
	}
}
