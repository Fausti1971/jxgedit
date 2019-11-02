package application;
//Idee: Configuration lädt, hält und speichert "config.xml" und übergibt bei Initialisation Zeiger auf Nodes an Konstruktoren, über die Veränderungen direkt im Document durchgeführt werden.
import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConfigurationConstants
{

	static final String APPNAME = "JXG";
	static final String SYSFILESEP = System.getProperty("file.separator");
	static final Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	static final Path CONFIGFILEPATH = HOMEPATH.resolve("config.xml");
	static final Path RSCPATH = Path.of("rsc/XG");

	static final String XML_OPCODE = "opcode.xml";
	static final String XML_OBJECT = "object.xml";
	static final String XML_PARAMETER = "parameter.xml";
	static final String XML_TRANSLATION = "translation.xml";

	static final int DEF_MIDITIMEOUT = 150;
	static final int DEF_SYSEXID = 0;

	public static enum ConfigurationEvent {Device, Midi, File, Gui}

	static final String 
		TAG_VERSION = "version",
		TAG_DEVICE = "device",
		TAG_MIDIINPUT = "MidiInput",
		TAG_MIDIOUTPUT = "MidiOutput",
		TAG_MIDITIMEOUT = "MidiTimeout",
		TAG_SYSEXID = "SysexID",
		TAG_LASTDUMPFILE = "LastDumpFile",
		TAG_WINX = "WindowX",
		TAG_WINY = "WindowY",
		TAG_WINW = "WindowW",
		TAG_WINH = "WindowH";

//	Version, Date, Time, WindowX, WindowY, WindowW, WindowH, lastConfig
//	MidiInput, MidiOutput, MIidiTimeout, SysexID, LastDumpFile
}
