package application;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConfigurationConstants
{

	static final String	APPNAME = "XG";
	static final String SYSFILESEP = System.getProperty("file.separator");
	static final Path homePath = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);

	static final String DEVICEXML = "XGDevice.xml";
	static final String OBJECTXML = "XGObjects.xml";
	static final String XGPARAMETERXML = "XGParameter.xml";
	static final String XGTRANSLATIONS = "XGTranslations.xml";

	public static Path getHomePath()
	{	return homePath;}

	public static String getAppName()
	{	return APPNAME;}


	public static enum ConfigurationEvent {Device, Midi, File, Gui}

	static final String 
		VERSION = "version",
		MIDIINPUT = "MidiInput",
		MIDIOUTPUT = "MidiOutput",
		MIDITIMEOUT = "MidiTimeout",
		SYSEXID = "SysexID",
		LASTDUMPFILE = "LastDumpFile",
		WINX = "WindowX",
		WINY = "WindowY",
		WINW = "WindowW",
		WINH = "WindowH";

//	Version, Date, Time, MidiInput, MidiOutput, MIidiTimeout, SysexID, WindowX, WindowY, WindowW, WindowH, LastDumpFile
}
