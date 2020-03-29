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
	{	super(src, dest, new byte[OVERHEAD + v.getOpcode().getSize()], true);
		setMessageID();
		setHi(v.getAddress().getHi());
		setMid(v.getAddress().getMid());
		setLo(v.getAddress().getLo());
		v.encodeBytes(this, v.getContent());
		setEOX(DATA_OFFS + v.getOpcode().getSize());
	}

	public XGMessageParameterChange(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, dest, msg);
		log.info(this.getClass().toString());
	}

	@Override public void setHi(int value)
	{	encodeMidiByteFromInteger(HI_OFFS, value);
	}

	@Override public void setMid(int value)
	{	encodeMidiByteFromInteger(MID_OFFS, value);
	}

	@Override public void setLo(int value)
	{	encodeMidiByteFromInteger(LO_OFFS, value);
	}

	@Override public int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);
	}

	@Override public int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);
	}

	@Override public void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	@Override public int getBulkSize()
	{	XGAddress adr = this.getAddress();
		return this.getSource().getDevice().getModule(adr).getBulks().get(adr).getOpcodes().get(adr).getSize();
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
