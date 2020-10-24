package application;

//JXG: Version, Date, Time, WindowX, WindowY, WindowW, WindowH

import java.nio.file.Path;
import java.nio.file.Paths;
import xml.XMLNodeConstants;

public interface ConfigurationConstants extends XMLNodeConstants
{
	int HASH = 17;

	String APPNAME = "JXG";
	Path CWD = Paths.get(System.getProperties().getProperty("user.dir"));
	Path HOMEPATH = Paths.get(System.getProperties().getProperty("user.home")).resolve(APPNAME);
//merke: "user.dir" returniert das (cwd) currentWorkingDirectory und nicht den applicationPath

	int MIN_W = 300, MIN_H = 400;
}
