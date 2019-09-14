package application;

import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAdressException;
import device.XGDevice;
import file.XGSysexFile;
import gui.XGMainFrame;
import obj.XGObjectType;
import parm.XGParameter;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;

public class XG implements ConfigurationConstants
{	private static final Logger log = Logger.getAnonymousLogger(); 

	static
	{	HOMEPATH.toFile().mkdirs();}

	public static void main(String[] args)
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
//		%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg

//		Runtime.getRuntime().addShutdownHook
//		(	new Thread()
//			{	@Override public void run()
//				{	log.info("application exited");
//				}
//			}
//		);
/*TODO	config laden,
			midi in config vorhanden:
				initialisieren,
 			midi in config nicht vorhanden:
				ConfigFrame fragen,
				initialisieren,
			initialisiertes midi nach xg devices abfragen,
				sysexID in config vorhanden:
					vorhandene abfragen,
				sysexID in config nicht vorhanden:
					alle sysexIDs abfragen,
			ordner namens "erkanntes xg-device" (devicePath) in homePath öffnen; fragen, falls mehrere,
				ordner vorhanden:
					*.xml laden und initialisieren,
					"default.syx" laden,
				ordner nicht vorhanden:
					xml aus rsc laden, nach devicePath kopieren und initialisieren,
					dumps abfragen (complete XG, dauert), dumps nach devicePath/default.syx speichern (fragen);
		Messengerkonzept überarbeiten: nur XGValue und XGBulkdump ist zum request und transmit imstande, Ziele dafür könnten aber weiterhin XGMessenger sein;
*/		
		
		Configuration.initConfig();
		quit();
//		try
//		{	XGDevice.initDevice();
//		}
//		catch(MidiUnavailableException | InvalidXGAdressException | CoreMidiException e)
//		{	e.printStackTrace();
//			quit();
//		}
//		XGObjectType.initObjectTypeMap();
//		XGParameter.initParameterSet();
//		XGMainFrame.initMainFrame();

//		mainFrame = new TreeFrame();
//		new XGTextReader().run();
//		SysexFile.getDefaultDump();
	}

	public static void quit()
	{	log.info("exiting application");
		Configuration.close();
//		XGDevice.getDevice().close();
		System.exit(0);
	}

}