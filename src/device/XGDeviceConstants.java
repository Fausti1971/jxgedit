package device;

//Device: Midi, SysexID, LastDumpFile, defaultDumpFolder, Name, Color

import java.util.LinkedHashSet;
import java.util.Set;
import parm.XGParameter;
import parm.XGTranslationConstants;
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

	static final XGParameter
		PARAM_SYSEXID = new XGParameter(TAG_SYSEXID, "System Exclusive ID", "sysexID", 0, 15, XGTranslationConstants.XGTranslator.translateToText, null);
		;
}
