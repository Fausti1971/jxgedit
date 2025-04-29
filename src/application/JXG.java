package application;
/*
 author: Thomas Faustmann
 mail: thomas@konferenzprofi.de
*/
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;import javax.xml.stream.XMLStreamException;
import device.*;
import file.*;
import gui.*;
import xml.*;

public class JXG implements XGLoggable, XGUI, XMLNodeConstants
{	public static boolean QUIT = false;
	public static XGLogWindow LOGWINDOW;
	public static final XGProperty CURRENT_CONTENT = new XGProperty(TAG_ITEM, "default");//der momentane Speicherinhalt (Dump, File o.ä.)
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}
	public static Path appFilePath;
	public static Path appPath;
	public static String appName;
	private static File configFile;
	public static XMLNode config;

	public final static String FILESEPARATOR = FileSystems.getDefault().getSeparator();
	public final static String DEVICEXMLPATH = "xml" + FILESEPARATOR + "devices" + FILESEPARATOR;
	public final static String DEF_DEVICEXMLPATH = DEVICEXMLPATH + "XG" + FILESEPARATOR;
	public final static String XML_SCHEMAPATH = "xml" + FILESEPARATOR + "schemas" + FILESEPARATOR;

	private static void setPaths()throws URISyntaxException
	{	appFilePath = Paths.get(JXG.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		LOG.info("AppFilePath: " + appFilePath.toString());

		appPath = appFilePath.getParent();
		LOG.info("AppPath: " + appPath.toString());

		String file =  appFilePath.getFileName().toString();
		if(!file.contains(".")) appName = file;
		else appName =  file.substring(0, file.lastIndexOf("."));
		LOG.info("AppName: " + appName);
	}

	private static void setConfig(String[] args)
	{	Path filepath = appPath.resolve(appName + ".xml");
		if(args.length > 0)//falls ein String übergeben wurde, interpretiere diesen als config-file-path
		{	filepath = Paths.get(args[0]);
		}

		configFile = new File(filepath.toUri());
		LOG.info("ConfigFilePath: " + configFile);

		try
		{	config = XMLNode.parse(configFile);
		}
		catch(IOException e)
		{	LOG.info(e.getMessage() + ", create new configuration.");
			config = new XMLNode(TAG_CONFIG);
		}
	}

	private static void init(String[] args)//TODO: evtl. Schalter einbauen für --config (-c), --device (-d), --midiinput(-i), --midioutput (-o), --file (-f)...
	{	try
		{	setPaths();
			setConfig(args);
		}
		catch(URISyntaxException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public static void main(String[] args)
	{	LOGWINDOW = new XGLogWindow();

		JXG.init(args);

		XGMidi.init();
		XGDatafile.init();
		XGUI.init();

		new XGDevice(JXG.config.getChildNodeOrNew(TAG_DEVICE));
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
			JOptionPane.showConfirmDialog(null, e.getMessage());
		}
	}
}