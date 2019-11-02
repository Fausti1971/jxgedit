package application;

import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import device.XGDevice;
import gui.XGWindow;
import obj.XGObjectType;
import opcode.XGOpcode;
import parm.XGTranslationMap;
import parm.XGParameter;
import xml.XMLNode;

public class JXG implements ConfigurationConstants
{	private static final Logger log = Logger.getAnonymousLogger();
	private static XMLNode config;

	public static XMLNode getConfig()
	{	return config;
	}

	private static void init() throws XMLStreamException
	{	HOMEPATH.toFile().mkdirs();
		config = XMLNode.init();
		log.info("JXG config initialized");
	}

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
		try
		{	JXG.init();
		}
		catch(XMLStreamException e)
		{	e.printStackTrace();
			quit();
			return;
		}
		XGDevice dev = XGDevice.getDefaultDevice();//XG-Spezifikation
		XGOpcode.init(dev);
		XGObjectType.init(dev);
		XGTranslationMap.init(dev);
		XGParameter.init(dev);

		XGDevice.init();
		XGWindow.init();
		quit();
	}

	public static void quit()
	{	log.info("exiting application");
		DomNode.exit();
		System.exit(0);
	}
}