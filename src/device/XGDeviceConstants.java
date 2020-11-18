package device;

//Device: Midi, SysexID, LastDumpFile, defaultDumpFolder, Name, Color

import java.util.LinkedHashSet;
import java.util.Set;
import xml.XMLNodeConstants;

public interface XGDeviceConstants extends XMLNodeConstants
{
	static final int
		DEF_SYSEXID = 0;

	static final String
		DEF_DEVNAME = "nameless device",
		DEF_SYXFILENAME = "default.syx";

	static final String
		ACTION_CONFIGURE = "configure...",
		ACTION_REMOVE = "remove",
		ACTION_LOADFILE = "load file...",
		ACTION_SAVEFILE = "save file...",
		ACTION_REQUEST = "request",
		ACTION_TRANSMIT = "transmit",
		ACTION_XGON = "switch xg ",
		ACTION_GMON = "switch gm",
		ACTION_RESET = "reset all parameters";

	static final Set<String> ACTIONS = new LinkedHashSet<>();
}
