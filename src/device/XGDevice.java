package device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAdressException;
import adress.XGAdressConstants;
import application.Configurable;
import application.JXG;
import file.XGSysexFile;
import msg.XGMessageDumpRequest;
import msg.XGRequest;
import msg.XGResponse;
import opcode.NoSuchOpcodeException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import xml.XMLNode;

public class XGDevice implements Configurable
{	private static Logger log = Logger.getAnonymousLogger();
	private static Set<XGDevice> DEVICES = new HashSet<>();
	private static XGDevice DEF = new XGDevice();

	public static XGDevice getDefaultDevice()
	{	if(DEF == null) DEF = new XGDevice();
		return DEF;
	}

	public static void init()
	{	XGDevice d;
		for(XMLNode n : JXG.getConfig().getChildren())
		{	if(n.getTag().equals(TAG_DEVICE))
			{	try
				{	d = new XGDevice(n);
					DEVICES.add(d);
				}
				catch(MidiUnavailableException|InvalidXGAdressException|CoreMidiException | TimeoutException | NoSuchOpcodeException e)
				{	e.printStackTrace();
				}
			}
		}
		log.info(DEVICES.size() + " devices initialized");
	}

/***************************************************************************************************************************/

	private final XMLNode config;
	private final String name;
	private final int info1, info2;
	private XGSysexFile file;
	private XGMidi midi;
	private int sysexID;

	private XGDevice()	//DefaultDevice (XG)
	{	this.config = null;
		this.name = "XG";
		this.info1 = 1;
		this.info2 = 1;
	}

	private XGDevice(XMLNode cfg) throws MidiUnavailableException, InvalidXGAdressException, CoreMidiException, TimeoutException, NoSuchOpcodeException
	{	this.config = cfg;
		this.sysexID = Integer.parseInt(this.config.getChildNode(TAG_SYSEXID).getTextContent());
		this.midi = new XGMidi(this);
		this.name = this.requestName();
		this.info1 = requestInfo1();
		this.info2 = requestInfo2();
	}

	public File getResourceFile(String fName)
	{	Path extPath = this.getTemplatePath();
		File extFile = extPath.resolve(fName).toFile();
		File intFile = RSCPATH.resolve(fName).toFile();
		if(!extFile.canRead() || !extFile.exists())
		{	try
			{	Files.createDirectories(extPath);
				Files.copy(intFile.toPath(), extFile.toPath());
			}
			catch(IOException e)
			{	log.info(e.getMessage() + ", using default");
				return intFile;
			}
			log.info("can't read file: " + extFile + "; default was copied from: " + intFile);
		}
		return extFile;
	}

	public Path getTemplatePath()
	{	return HOMEPATH.resolve(this.getName());
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	void setSysexID(int id)
	{	this.sysexID = id & 0xF;
//		this.config.setInt(TAG_SYSEXID, this.sysexID);
	}

	public String requestName() throws InvalidXGAdressException, TimeoutException, NoSuchOpcodeException        //SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m = new XGMessageDumpRequest(null, XGAdressConstants.XGMODELNAMEADRESS);
		m.setDestination(this.midi);
		XGResponse r = m.request();
		return r.getValues().get(XGAdressConstants.XGMODELNAMEADRESS).toString().strip();
	}

	public int requestInfo1()
	{	return 0;
	}

	public int requestInfo2()
	{	return 0;
	}

	public String getName()
	{	return this.name;
	}

	@Override public String toString()
	{	return(this.getName() + " (" + this.sysexID + ")");
	}

	public XMLNode getConfig()
	{	return this.config;
	}
}