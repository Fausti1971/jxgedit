package module;

import javax.swing.ActionMap;
import adress.*;import xml.XMLNodeConstants;

public interface XGModuleConstants extends XMLNodeConstants
{
	//enum TAG
	//{	SYSTEM, INFO, REV, CHO, VAR, EQ, INSFX, DISPLAY, MULTIPART, ADPART, DRUMSET, PLUGIN
	//};


	static final String DEF_MODULENAME = "nameless module";
	ActionMap MOD_ACTIONS = new ActionMap();

	static final String
		ACTION_EDIT = "edit...",
		ACTION_LOADFILE = "load file...",
		ACTION_SAVEFILE = "save file...",
		ACTION_REQUEST = "request",
		ACTION_TRANSMIT = "transmit",
		ACTION_RESET = "reset";
}
