package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Setting extends Properties
{	/**
	 * 
	 */
	public static final String DEVICENAME = "DeviceName", MIDIINPUT = "MidiInput", MIDIOUTPUT = "MidiOutput", SYSEXID = "SysexID", MIDITIMEOUT = "MidiTimeout", LASTDUMPPATH = "LastDumpPath", LASTDUMPFILE = "LastDumpFile";
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getAnonymousLogger();

/***********************************************************************************************/

	private File file = null;
/*
	public Setting(String filename, Setting set)		//Konstruktor für Devicesettings
	{	this.file = (MU80.getDevicePath().resolve(filename)).toFile();
		for(String s : set.stringPropertyNames()) this.setProperty(s, set.getProperty(s));
	}
*/
	public Setting(File file)				//Konstruktor für Settingsfile als File
	{	this.file = file;
		loadSetting();
	}

	private boolean loadSetting()
	{	if(this.file.exists())
		{	try(FileInputStream fis = new FileInputStream(this.file))
			{	this.load(fis);
			}
			catch (IOException e)
			{	log.info(e.getMessage());
				return false;
			}
			log.info("properties loaded: " + this.file);
			return true;
		}
		log.info("file not found: " + this.file.toString());
		return false;
	}

	private boolean saveSetting()
	{	if(!this.file.exists())
		{	try
			{	this.file.createNewFile();
			}
			catch (IOException e)
			{	log.info(e.getMessage());
				return false;
			}
		}
		try(FileOutputStream fos = new FileOutputStream(this.file))
		{	this.store(fos, MU80.getAppName());
		}
		catch (IOException e)
		{	e.printStackTrace();
			return false;
		}
		log.info("properties stored: " + this.file);
		return true;
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

	public String get(String key)
	{	return(this.getProperty(key, "no value"));
	}

	public String get(String key, String def)
	{	return(this.getProperty(key, def));
	}

	public void save()
	{	saveSetting();
	}

	public void close()
	{
	}
}