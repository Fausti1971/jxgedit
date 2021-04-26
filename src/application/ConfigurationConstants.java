package application;

import xml.XMLNodeConstants;import java.io.*;

public interface ConfigurationConstants extends XMLNodeConstants
{
	String
		APPNAME = "JXG",
		APPPATH = getApplicationPath(),
		FILESEPERATOR = System.getProperty("file.separator"),
//		RSCPATH = ".." + FILESEPERATOR + "rsc" + FILESEPERATOR,
		XMLPATH = "/xml/",
//		RSCPATH = "./rsc/",
		CWD = System.getProperties().getProperty("user.dir"),
		USERHOMEPATH = System.getProperties().getProperty("user.home");

	static String getApplicationPath()
	{	String s[] = System.getProperty("java.class.path").split(":");
		File f = new File(s[0]);
		if(f.isDirectory()) return s[0];
		else return f.getParent();
	}

	int MIN_W = 300, MIN_H = 400, MIN_X = 20, MIN_Y = 20;
}
