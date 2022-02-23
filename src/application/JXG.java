package application;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.stream.XMLStreamException;
import device.*;
import file.*;
import gui.*;
import module.XGModule;
import module.XGModuleType;
import msg.XGMessageParameterRequest;import parm.*;
import value.*;
import xml.*;

public class JXG implements XGLoggable, XGUI, XMLNodeConstants
{
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}
	public static XMLNode config;
	private static File configFile;
	public static final String
		APPNAME = "JXG",
//		FILESEPERATOR = System.getProperty("file.separator"),
		XMLPATH = "/xml/";
//		CWD = System.getProperties().getProperty("user.dir"),
//		USERHOMEPATH = System.getProperties().getProperty("user.home");

	public static void init()
	{	try
		{	URI uri = JXG.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			configFile = new File(uri.resolve(XML_CONFIG));
			config = XMLNode.parse(configFile);
		}
		catch(URISyntaxException | IOException e)
		{	LOG.info(e.getMessage());
			config = new XMLNode(TAG_CONFIG);
		}
	}

	//private static String getApplicationPath()
	//{	
	//	String s[] = System.getProperty("java.class.path").split(":");
	//	File f = new File(s[0]);
	//	if(f.isDirectory()) return s[0];
	//	else return f.getParent();
	//}

	public static void main(String[] args)
	{
		XGSplashScreen splash = new XGSplashScreen();

		JXG.init();
		XGTable.init();
		XGDrumNames.init();
		XGDefaultsTable.init();
		XGParameterTable.init();
		XGSysexFile.init();
		XGMidi.init();
		XGDevice.init();
		XGModuleType.init();
		XGModule.init();
		XGValueStore.init();
		XGUI.init();
		XGMainWindow.init();
		System.gc();
		splash.dispose();

		LOG.info(APPNAME + " initialized from " + configFile);
	}

	public static void quit()
	{	LOG.info("exiting application");
		try
		{	config.save(configFile);
			XGDevice.device.exit();
			System.exit(0);
		}
		catch(IOException | XMLStreamException e)
		{	LOG.severe(e.getMessage());
		}
	}
}