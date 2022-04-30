package application;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.stream.XMLStreamException;
import device.*;
import file.*;
import gui.*;
import module.XGModule;
import module.XGModuleType;
import table.XGDefaultsTable;
import table.XGParameterTable;
import table.XGTable;
import value.*;
import xml.*;

public class JXG implements XGLoggable, XGUI, XMLNodeConstants
{
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}
	public static Path appFilePath;
	public static Path appPath;
	public static String appName;
	private static File configFile;
	public static XMLNode config;

//		APPNAME = "JXG";
//		FILESEPERATOR = System.getProperty("file.separator");
//		XMLPATH = "/xml/";
//		CWD = System.getProperties().getProperty("user.dir"),
//		USERHOMEPATH = System.getProperties().getProperty("user.home");

	private static void setAppFilePath()throws URISyntaxException
	{	appFilePath = Paths.get(JXG.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		LOG.info(appFilePath.toString());
	}

	private static void setAppPath()
	{	appPath = appFilePath.getParent();
		LOG.info(appPath.toString());
	}

	private static void setAppName()
	{	String file =  appFilePath.getFileName().toString();
		appName =  file.substring(0, file.lastIndexOf("."));
		LOG.info(appName);
	}

	private static void setConfigFile()
	{	configFile = new File(appPath.resolve(appName + ".xml").toUri());
		LOG.info(configFile.toString());
	}

	private static void setConfig()throws IOException
	{	config = XMLNode.parse(configFile);
	}

	private static void init()
	{	try
		{	setAppFilePath();
			setAppPath();
			setAppName();
			setConfigFile();
			setConfig();
		}
		catch(URISyntaxException | IOException e)
		{	LOG.severe(e.getMessage());
			config = new XMLNode(TAG_CONFIG);
		}
	}

	public static void main(String[] args)
	{
		XGSplashScreen splash = new XGSplashScreen();

		JXG.init();

		XGMidi.init();
		XGDevice.init();
		XGSysexFile.init();

		XGTable.init();
		XGDefaultsTable.init();
		XGParameterTable.init();

		XGModuleType.init();

		XGModule.init();
		XGValue.init();

		XGUI.init();
		XGWindow.init();
		XGEditWindow.init();

		System.gc();
		splash.dispose();

		LOG.info(appName + " initialized from " + configFile);
	}

	public static void quit()
	{	LOG.info("exiting application");
		try
		{	config.save(configFile);
			XGDevice.device.close();
			System.exit(0);
		}
		catch(IOException | XMLStreamException e)
		{	LOG.severe(e.getMessage());
		}
	}
}