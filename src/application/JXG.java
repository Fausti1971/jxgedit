package application;

import java.awt.*;import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;
import adress.InvalidXGAddressException;
import device.XGDevice;
import gui.*;
import xml.*;

public class JXG implements XGLoggable, XGUI
{
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}

	private static XMLNode configXML;
	private static File configFile;
	private static XGDevice device;
	public static Rectangle mainWindowBounds = new Rectangle();
	public static XGMainWindow mainWindow;

	public static int
		sysexID = 0,
		midiTimeout = 150;

	public static String
		midiInput = "",
		midiOutput = "";

	public static void main(String[] args)
	{
		configFile = new File(APPPATH + FILESEPERATOR + XML_CONFIG);
		try
		{	configXML = XMLNode.parse(configFile);
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
			configXML = new XMLNode(TAG_CONFIG);
		}

		LOG.info(APPNAME + " initialized from " + configFile);

		try
		{	device = new XGDevice(configXML.getChildNodeOrNew(TAG_DEVICE));
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}

		XGUI.init(configXML.getChildNodeOrNew(TAG_UI));
	}

	public static void quit()
	{	LOG.info("exiting application");
		try
		{	configXML.save(configFile);
		}
		catch(IOException | XMLStreamException e)
		{	LOG.warning(e.getMessage());
		}

		device.exit();
		System.exit(0);
	}
}