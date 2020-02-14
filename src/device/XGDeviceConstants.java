package device;

import java.util.LinkedHashSet;
import java.util.Set;

public interface XGDeviceConstants
{
	static final int HASH = 17;

	static final String
		TAG_NAME = "name",
		TAG_MIDI = "midi",
		TAG_MIDIINPUT = "input",
		TAG_MIDIOUTPUT = "output",
		TAG_MIDITIMEOUT = "timeout",
		TAG_SYSEXID = "sysexID",
		TAG_LASTDUMPFILE = "lastDumpFile",
		TAG_DEFAULTDUMPFOLDER = "defaultDumpFolder";

	static final String
		ACTION_CONFIGURE = "configure...",
		ACTION_REMOVE = "remove",
		ACTION_LOADFILE = "load file...",
		ACTION_SAVEFILE = "save file...",
		ACTION_REQUEST = "request",
		ACTION_TRANSMIT = "transmit";

	static final Set<String> ACTIONS = new LinkedHashSet<>();
}
