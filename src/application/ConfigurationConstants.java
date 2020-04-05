package application;

//JXG: Version, Date, Time, WindowX, WindowY, WindowW, WindowH

import java.nio.file.Path;
import java.nio.file.Paths;
import xml.XMLNodeConstants;

public interface ConfigurationConstants extends XMLNodeConstants
{
	static final int HASH = 17;

	static final String APPNAME = "JXG";
//	static final String SYSFILESEP = System.getProperty("file.separator");

	static final Path RSCPATH = Path.of("rsc");
	static final Path XSDPATH = RSCPATH.resolve("xsd");
	static final Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	static final Path CONFIGFILEPATH = HOMEPATH.resolve(XML_CONFIG);

	static final int MIN_W = 300, MIN_H = 400;
}
