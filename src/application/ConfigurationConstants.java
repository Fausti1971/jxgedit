package application;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConfigurationConstants
{

	static final String APPNAME = "XG";
	static final String SYSFILESEP = System.getProperty("file.separator");
	static final Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	static final Path CONFIGFILEPATH = HOMEPATH.resolve("config.xml");

	static final String OBJECTXML = "object.xml";
	static final String XGPARAMETERXML = "parameter.xml";
	static final String XGTRANSLATIONXML = "translation.xml";

	static final int DEF_MIDITIMEOUT = 150;

	public static Path getHomePath()
	{	return HOMEPATH;}

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

//	Version, Date, Time, WindowX, WindowY, WindowW, WindowH, lastConfig
//	MidiInput, MidiOutput, MIidiTimeout, SysexID, LastDumpFile
}
