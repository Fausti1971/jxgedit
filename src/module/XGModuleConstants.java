package module;

import javax.swing.ActionMap;
import adress.*;import xml.XMLNodeConstants;

public interface XGModuleConstants extends XMLNodeConstants
{
	//enum TAG
	//{	SYSTEM, INFO, EFFECT1, MULTIEQ, EFFECT2, EFFECT2_VL, DISPLAY_LETTER, DISPLAY_BITMAP, MULTIPART, MULTIPART_ADD, MULTIPART_VL, ADPART, DRUMSET1, DRUMSET2, DRUMSET3, DRUMSET4, PLUGIN
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
