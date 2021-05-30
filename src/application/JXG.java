package application;

import java.awt.*;import java.io.*;import java.nio.file.*;
import javax.swing.*;import javax.xml.stream.XMLStreamException;
import device.*;
import file.*;import gui.*;
import static java.lang.ClassLoader.getSystemResourceAsStream;import module.XGModuleType;import parm.*;import value.*;import xml.*;

public class JXG implements XGLoggable, XGUI
{
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}
	public static XMLNode config;
	private static File configFile;

/**
* returniert das angegebene File aus dem internen Pfad (*.jar)
*/
	public static InputStream getResourceStream(String fName) throws IOException //Merke: SAX scheint mit mac-Aliases nicht zurecht zu kommen, daher bei Bedarf Softlinks erzeugen (ln -s Quelle Ziel)
	{	return JXG.class.getResourceAsStream(fName);
	}

	public static void main(String[] args)
	{	XGSplashScreen splash = new XGSplashScreen();

		configFile = new File(APPPATH + FILESEPERATOR + XML_CONFIG);
		try
		{	config = XMLNode.parse(new FileInputStream(configFile));
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
			config = new XMLNode(TAG_CONFIG);
		}

		XGTable.init();
		XGDrumNames.init();
		XGDefaultsTable.init();
		XGParameterTable.init();
		XGSysexFile.init();
		XGMidi.init();
		XGDevice.init();
		XGModuleType.init();
		XGValueStore.init();
		XGUI.init();
		XGMainWindow.init();

		splash.dispose();

		LOG.info(APPNAME + " initialized from " + configFile);
	}

	public static void quit()
	{	LOG.info("exiting application");
		try
		{	config.save(configFile);
		}
		catch(IOException | XMLStreamException e)
		{	LOG.warning(e.getMessage());
		}

		XGDevice.getDevice().exit();
		System.exit(0);
	}
}