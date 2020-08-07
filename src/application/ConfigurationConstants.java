package application;

import java.net.URL;

//JXG: Version, Date, Time, WindowX, WindowY, WindowW, WindowH

import java.nio.file.Path;
import java.nio.file.Paths;
import xml.XMLNodeConstants;

public interface ConfigurationConstants extends XMLNodeConstants
{
	int HASH = 17;

	String APPNAME = "JXG";
//	static final String SYSFILESEP = System.getProperty("file.separator");

//	URL RSCPATH = ClassLoader.getSystemResource("rsc");
//	URL ICONPATH = new URL(RSCPATH.getPath() + "icon");

//	Path RSCPATH = Path.of("rsc");
//	Path ICONPATH = RSCPATH.resolve("icon");
//	Path XSDPATH = RSCPATH.resolve("xsd");

	Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	Path CONFIGFILEPATH = HOMEPATH.resolve(XML_CONFIG);

	int MIN_W = 300, MIN_H = 400;
}
