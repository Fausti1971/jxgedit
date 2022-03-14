package msg;

import javax.sound.midi.SysexMessage;

public interface XGMessageConstants extends XGByteArray
{	int
		VENDOR = 0x43,
		VENDOR_OFFS = 1,

		MODEL = 0x4C,
		MODEL_OFFS = 3,

		MSG_BD = 0x00,
		MSG_PC = 0x10,
		MSG_DR = 0x20,
		MSG_PR = 0x30,
		MSG_OFFS = 2,

		SYSEX_OFFS = 2,

		SOX = SysexMessage.SYSTEM_EXCLUSIVE,
		SOX_OFFS = 0,

		EOX = SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE;
}
