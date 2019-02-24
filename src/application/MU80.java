package application;

import java.awt.Desktop;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import file.SysexFile;
import gui.MainFrame;
import midi.XGDevice;

public class MU80
{	private static final Logger log = Logger.getAnonymousLogger(); 
	private static final String
		APPNAME = "MU80",
		SYSFILESEP = System.getProperty("file.separator");

	private static Path homePath = Paths.get(System.getProperties().getProperty("user.home"), APPNAME);
	private static Setting setting;
	private static MainFrame mainFrame;
	public static XGDevice device;

	static
	{	homePath.toFile().mkdirs();
	}

	public static Setting getSetting()
	{	return setting;
	}

	public static Path getHomePath()
	{	return homePath;
	}

	public static String getAppName()
	{	return APPNAME;
	}

	public static String getSeparator()
	{	return SYSFILESEP;
	}

	public static MainFrame getMainFrame()
	{	return mainFrame;
	}

	public static void main(String[] args)
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
//		%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg

		Desktop.getDesktop().setQuitHandler(new QuitHandler()
		{	public void handleQuitRequestWith(QuitEvent e, QuitResponse response)
			{	response.performQuit();
				MU80.exit();
			}
		});
		setting = new Setting(getHomePath().resolve("setting").toFile());
		device = new XGDevice(setting);
		SysexFile.getDefaultDump();
		mainFrame = new MainFrame();
	}

	public static void exit()
	{	setting.save();
		device.close();
		System.exit(0);
	}
}