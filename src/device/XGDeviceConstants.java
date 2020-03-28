package device;

//Device: Midi, SysexID, LastDumpFile, defaultDumpFolder, Name, Color

import java.util.LinkedHashSet;
import java.util.Set;
import xml.XMLNodeConstants;

public interface XGDeviceConstants extends XMLNodeConstants
{
	static final int
		DEF_DEVCOLOR = 0xFFFFFF;

	static final String
		DEF_DEVNAME = "nameless device";

	static final String
		ACTION_CONFIGURE = "configure...",
		ACTION_REMOVE = "remove",
		ACTION_LOADFILE = "load file...",
		ACTION_SAVEFILE = "save file...",
		ACTION_REQUEST = "request",
		ACTION_TRANSMIT = "transmit";

	static final Set<String> ACTIONS = new LinkedHashSet<>();
}
