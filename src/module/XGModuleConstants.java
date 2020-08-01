package module;

import javax.swing.ActionMap;
import xml.XMLNodeConstants;

public interface XGModuleConstants extends XMLNodeConstants
{
//	public static enum XGModuleTag
//	{	system, info, sysfx, syseq, insfx, display, multipart, adpart, drumset, plugin
//	};

	ActionMap MOD_ACTIONS = new ActionMap();

	static final String
		ACTION_EDIT = "edit...",
		ACTION_LOADFILE = "load file...",
		ACTION_SAVEFILE = "save file...",
		ACTION_REQUEST = "request",
		ACTION_TRANSMIT = "transmit";
}
