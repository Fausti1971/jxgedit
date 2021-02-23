package application;

import xml.XMLNodeConstants;import java.io.*;

public interface ConfigurationConstants extends XMLNodeConstants
{
	int HASH = 17;
	String
		APPNAME = "JXG",
		RSCPATH = "rsc",
		FILESEPERATOR = System.getProperty("file.separator"),
		CWD = System.getProperties().getProperty("user.dir"),
		USERHOMEPATH = System.getProperties().getProperty("user.home"),
		APPPATH = getApplicationPath();

	static String getApplicationPath()
	{	String s[] = System.getProperty("java.class.path").split(":");
		File f = new File(s[0]);
		if(f.isDirectory()) return s[0];
		else return f.getParent(); 
	}

	int MIN_W = 300, MIN_H = 400, MIN_X = 20, MIN_Y = 20;
}
