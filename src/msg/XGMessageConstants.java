package msg;

import javax.sound.midi.SysexMessage;

public interface XGMessageConstants extends XGByteArray
{	public static final int
		VENDOR = 0x43,
		VENDOR_OFFS = 1,
		MODEL = 0x4C,
		MODEL_OFFS = 3,
		MSG_OFFS = 2,
		SYSEX_OFFS = 2,
		SOX = SysexMessage.SYSTEM_EXCLUSIVE,
		SOX_OFFS = 0,
		EOX = SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE,
		BD = 0x00,
		PC = 0x10,
		DR = 0x20,
		PR = 0x30;

	default void setSOX()
	{	encodeMidiByteFromInteger(SOX_OFFS, SOX);
	}

	default void setEOX(int index)
	{	encodeMidiByteFromInteger(index, EOX);
	}

	default int getSysexID()
	{	return decodeLowerNibble(SYSEX_OFFS);
	}

	default void setSysexID(int id)
	{	encodeLowerNibble(SYSEX_OFFS, id);
	}

	default int getMessageID()
	{	return decodeHigherNibbleToInteger(MSG_OFFS);
	}

	default void setMessageID(int id)
	{	encodeHigherNibbleFromInteger(MSG_OFFS, id);
	}

	default int getVendorID()
	{	return decodeMidiByteToInteger(VENDOR_OFFS);
	}

	default void setVendorID()
	{	encodeMidiByteFromInteger(VENDOR_OFFS, VENDOR);
	}

	default int getModelID()
	{	return decodeMidiByteToInteger(MODEL_OFFS);
	}

	default void setModelID()
	{	encodeMidiByteFromInteger(MODEL_OFFS, MODEL);
	}
}
