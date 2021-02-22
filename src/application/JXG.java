package application;

import java.awt.*;import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;
import adress.InvalidXGAddressException;
import device.XGDevice;
import gui.XGContext;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGUI;
import gui.XGWindow;
import xml.XMLNode;

public class JXG implements XGLoggable, XGUI
{
	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}

	private final static Set<String> ACTIONS = new LinkedHashSet<>();

	public static Rectangle mainWindow = new Rectangle();

	public static int
		sysexID = 0,
		midiTimeout = 150;

	public static String
		midiInput = "",
		midiOutput = "";

	public static void loadConfig()
	{
	}

	public static void saveConfig()
	{
	}

	public static void main(String[] args)
	{	
		if(HOMEPATH.toFile().exists()) this.configPath = HOMEPATH;
		else this.configPath = CWD;
		LOG.info("path for configuration: " + this.configPath);
		this.configFile = this.configPath.resolve(XML_CONFIG);

		File f = configFile.toFile();
		if(f.exists()) this.config = XMLNode.parse(f);
		else this.config = new XMLNode(APPNAME);

		LOG.info("JXG config initialized");

		XGUI.init(APP.getConfig());
		XGWindow.getRootWindow();
		new Thread(() -> {	XGDevice.init(APP.getConfig());}).start();
//		quit();
	}
	public static void quit()
	{	LOG.info("exiting application");
		try
		{	APP.getConfig().save(configFile.toFile());
			for(XGDevice d : XGDevice.getDevices()) d.exit();
		}
		catch(IOException|XMLStreamException e)
		{	e.printStackTrace();
		}
		System.exit(0);
	}
}