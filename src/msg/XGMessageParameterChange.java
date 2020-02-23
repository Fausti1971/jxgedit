package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import value.XGValue;

public class XGMessageParameterChange extends XGSuperMessage implements XGResponse
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7, OVERHEAD = 8;

/**
 * @param init 
 * @throws InvalidMidiDataException **************************************************************************************************************/

	public XGMessageParameterChange(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
	}

	public XGMessageParameterChange(XGMessenger src, XGValue v) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, new byte[OVERHEAD + v.getOpcode().getByteCount()], true);
		setMessageID();
		setHi(v.getAdress().getHi());
		setMid(v.getAdress().getMid());
		setLo(v.getAdress().getLo());
		setData(v);
		setEOX(DATA_OFFS + v.getOpcode().getByteCount());
	}

	public XGMessageParameterChange(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
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

	protected void setData(XGValue v)
	{	encodeXGValue(DATA_OFFS, v);
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
	{	return this.getSource().getDevice().getOpcodes().get(this.getAdress()).getByteCount();
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
