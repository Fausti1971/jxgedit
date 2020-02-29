package application;

//JXG: Version, Date, Time, WindowX, WindowY, WindowW, WindowH

import java.nio.file.Path;
import java.nio.file.Paths;
import xml.XMLNodeConstants;

public interface ConfigurationConstants extends XMLNodeConstants
{
	static final int HASH = 17;

	static final String XML_CONFIG = "config.xml";
	static final String XML_OPCODE = "opcode.xml";
	static final String XML_TYPE = "type.xml";
	static final String XML_PARAMETER = "parameter.xml";
	static final String XML_TRANSLATION = "translation.xml";
	static final String XML_TEMPLATE = "template.xml";

	static final String APPNAME = "JXG";
//	static final String SYSFILESEP = System.getProperty("file.separator");

	static final Path RSCPATH = Path.of("rsc");
	static final Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	static final Path CONFIGFILEPATH = HOMEPATH.resolve(XML_CONFIG);

	static final int DEF_MIDITIMEOUT = 150;
	static final int DEF_SYSEXID = 0;

	static final int MIN_W = 300, MIN_H = 400;
}
