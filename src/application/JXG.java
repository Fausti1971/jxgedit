package application;
/*
 author: Thomas Faustmann
 mail: thomas@konferenzprofi.de
*/
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
	public static XGProperty CURRENT_CONTENT = new XGProperty(TAG_ITEM, "default");
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}
	public static Path appFilePath;
	public static Path appPath;
	public static String appName;
	private static File configFile;
	public static XMLNode config;

	private static void setPaths()throws URISyntaxException
	{	appFilePath = Paths.get(JXG.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		LOG.info("appfile: " + appFilePath.toString());

		appPath = appFilePath.getParent();
		LOG.info("filepath: " + appPath.toString());

		String file =  appFilePath.getFileName().toString();
		appName =  file.substring(0, file.lastIndexOf("."));
		LOG.info("appname: " + appName);

		configFile = new File(appPath.resolve(appName + ".xml").toUri());
		LOG.info("configfile: " + configFile);
	}

	private static void setConfig()
	{	try
		{	config = XMLNode.parse(configFile);
		}
		catch(IOException e)
		{	LOG.info(e.getMessage() + ", create new configuration.");
			config = new XMLNode(TAG_CONFIG);
		}
	}

	private static void init()
	{	try
		{	setPaths();
			setConfig();
		}
		catch(URISyntaxException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public static void main(String[] args)
	{	long time = System.currentTimeMillis();
		XGSplashScreen splash = new XGSplashScreen();

		JXG.init();

		XGMidi.init();
		XGDevice.init();
		XGDatafile.init();

		XGTable.init();
		XGDefaultsTable.init();
		XGParameterTable.init();

		XGModuleType.init();//inklusive XGBulkTypes und XGValueTypes (XGOpcode)

		XGModule.init();//inklusive XGBulk
		XGValue.init();

		XGUI.init();
		XGEditWindow.init();
		XGWindow.init();

		System.gc();
		splash.dispose();

		LOG.info(appName + " initialized from " + configFile + " within " + (System.currentTimeMillis() - time) + " ms");
	}

	public static void quit()
	{	LOG.info("exiting application");
		try
		{	config.save(configFile);
			XGDevice.DEVICE.close();
			System.exit(0);
		}
		catch(IOException | XMLStreamException e)
		{	LOG.severe(e.getMessage());
		}
	}
}