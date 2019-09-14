package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
//TODO: Umbau nach XML
//homePath/config.xml: Version, Date, Time, WindowX, WindowY, WindowW, WindowH, lastConfig
//homePath/config.xml: MidiInput, MidiOutput, MIidiTimeout, SysexID, LastDumpFile

public class Configuration extends Properties implements ConfigurationChangeListener
{	/**
	 * 
	 */
	private static Set<Configuration> CONFIGS = new HashSet<>();
	private static Configuration CURRENTCONFIG;
	private static Configuration MAINCONFIG;
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getAnonymousLogger();

	public static Configuration getCurrentConfig()
	{	return CURRENTCONFIG;
	}

	public static void initConfig()
	{	MAINCONFIG = new Configuration(CONFIGFILEPATH);
		File[] files = HOMEPATH.toFile().listFiles(new FilenameFilter()
		{	public boolean accept(File dir, String name)
			{	if(!dir.equals(HOMEPATH.toFile())) return false;
				if(!name.equalsIgnoreCase(".xml")) return false;
				return true;
			}
		});
		for(File f : files) log.info(f.toString());
	}

	public static void close()
	{	MAINCONFIG.save();
		for(Configuration c : CONFIGS) c.save();
	}

/***********************************************************************************************/

	private File file;

	public Configuration(Path filePath)				//Konstruktor für Settingsfile als File
	{	this.file = filePath.toFile();
		this.load();
	}

	private void load()
	{	if(this.file.exists())
		{	try(FileInputStream fis = new FileInputStream(this.file))
			{	this.loadFromXML(fis);
			}
			catch (IOException e)
			{	log.info(e.getMessage());
				return;
			}
			log.info("properties loaded: " + this.file);
			return;
		}
		log.info("file not found: " + this.file.toString());
	}

	private void save()
	{	if(!this.file.exists())
		{	try
			{	this.file.createNewFile();
			}
			catch (IOException e)
			{	log.info(e.getMessage());
				return;
			}
		}
		try(FileOutputStream fos = new FileOutputStream(this.file))
		{	this.storeToXML(fos, ConfigurationConstants.getAppName());
		}
		catch (IOException e)
		{	e.printStackTrace();
			return;
		}
		log.info("properties stored: " + this.file);
	}

	public void setInt(String key, int value)
	{	this.put(key, Integer.toString(value));
	}

	public void set(String key, String value)
	{	this.put(key, value);
	}

	public int getInt(String key, int def)
	{	if(!this.containsKey(key))
		{	this.setInt(key, def);
			return def;
		}
		return Integer.parseInt((String) this.getProperty(key));
	}

	public String getOrDefault(String key, String def)
	{	return(this.getProperty(key, def));
	}

	public void configurationChanged(ConfigurationEvent e)
	{	log.info(e.name());
	}
}