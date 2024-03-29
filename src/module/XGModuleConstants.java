package module;

import javax.swing.ActionMap;
import xml.XMLNodeConstants;

public interface XGModuleConstants extends XMLNodeConstants
{
	//enum ModuleTypeTag
	//{	SYSTEM, INFO, FX1REV, FX1CHO, FX1VAR, FX1EQ, FX2, DISPLAY_LETTER, DISPLAY_BITMAP, MULTIPART, ADPART, DRUMSET1, DRUMSET2, DRUMSET3, DRUMSET4, PLUGIN
	//};

	String
		MT_SYSTEM = "sys",
		MT_INFO = "info",
		MT_FX1REV = "rev",
		MT_FX1CHO = "cho",
		MT_FX1VAR = "var",
		MT_FX1EQ = "eq",
		MT_FX2 = "ins",
		MT_MULTIPART = "mp",
		MT_ADPART = "ad",
		MT_DRUMSET1 = "ds1",
		MT_DRUMSET2 = "ds2",
		MT_DRUMSET3 = "ds3",
		MT_DRUMSET4 = "ds4";

	String DEF_MODULENAME = "nameless module";
	ActionMap MOD_ACTIONS = new ActionMap();
}
