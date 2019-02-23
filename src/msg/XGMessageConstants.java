package msg;

import javax.sound.midi.SysexMessage;

import memory.Bytes;

public interface XGMessageConstants extends Bytes
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
	{	encodeMidiByte(SOX_OFFS, SOX);
	}

	default void setEOX(int index)
	{	encodeMidiByte(index, EOX);
	}

	default int getSysexId()
	{	return decodeLowerNibble(SYSEX_OFFS);
	}

	default void setSysexId(int id)
	{	encodeLowerNibble(SYSEX_OFFS, id);
	}

	default int getMessageId()
	{	return decodeHigherNibble(MSG_OFFS);
	}

	default void setMessageId(int id)
	{	encodeHigherNibble(MSG_OFFS, id);
	}

	default int getVendorId()
	{	return decodeMidiByte(VENDOR_OFFS);
	}

	default void setVendorId()
	{	encodeMidiByte(VENDOR_OFFS, VENDOR);
	}

	default int getModelId()
	{	return decodeMidiByte(MODEL_OFFS);
	}

	default void setModelId()
	{	encodeMidiByte(MODEL_OFFS, MODEL);
	}
}
