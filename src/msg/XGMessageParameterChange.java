package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import value.XGValue;

public class XGMessageParameterChange extends XGSuperMessage implements XGResponse
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x10, DATA_OFFS = 7, OVERHEAD = 8;

/****************************************************************************************************************/

	public XGMessageParameterChange(XGMessenger src, byte[] array, long time)
	{	super(src, array);
	}

	public XGMessageParameterChange(XGMessenger src, XGValue v) throws InvalidXGAddressException
	{	super(src, new byte[OVERHEAD + v.getOpcode().getByteCount()]);
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

	@Override protected void setHi(int value)
	{	encodeMidiByteFromInteger(HI_OFFS, value);
	}

	@Override protected void setMid(int value)
	{	encodeMidiByteFromInteger(MID_OFFS, value);
	}

	@Override protected void setLo(int value)
	{	encodeMidiByteFromInteger(LO_OFFS, value);
	}

	protected void setData(XGValue v)
	{	encodeXGValue(DATA_OFFS, v);
	}

	@Override protected int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);
	}

	@Override protected int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);
	}

	@Override protected int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);
	}

	@Override protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	@Override public XGAddressableSet<XGValue> getValues()
	{	return null;
	}
}
