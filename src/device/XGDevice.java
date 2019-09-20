package device;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import application.Configuration;
import application.ConfigurationChangeListener;
import application.ConfigurationConstants;
import file.XGSysexFile;
import msg.XGMessage;
import msg.XGMessageDumpRequest;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import obj.XGObjectType;
import parm.XGParameter;
import parm.XGParameterConstants;
import parm.XGParameterStorage;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;
import value.XGValue;
import value.XGValueStorage;

public class XGDevice implements ConfigurationConstants
{	private static Logger log = Logger.getAnonymousLogger();

/***************************************************************************************************************************/

	private final Configuration config;
	private XGValue name, info1, info2;
	private final XGValueStorage values;
	private final XGParameterStorage parameters;
	private final XGObjectTypeStorage objectTypes;
	private XGSysexFile file;
	private XGMidi midi;
	private int sysexID;

	private XGDevice(Configuration cfg) throws MidiUnavailableException, InvalidXGAdressException, CoreMidiException	//für Initialisation via Config
	{	this.config = cfg;
		this.sysexID = cfg.getInt(SYSEXID, DEF_SYSEXID);
		this.midi = new XGMidi(this);
		this.parameters = new XGParameterStorage(this);
		this.values = new XGValueStorage(this);
		this.notifyConfigurationListeners();
	}

	public Configuration getConfig()
	{	return this.config;
	}

	public Path getTemplatePath()
	{	return HOMEPATH.resolve(this.getName());
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		this.config.setInt(SYSEXID, this.sysexID);
		this.notifyConfigurationListeners();
	}

	public void requestInfo() throws InvalidXGAdressException, TimeoutException        //SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m = new XGMessageDumpRequest(this.values, XGParameterConstants.XGMODELNAMEADRESS);
		m.setDestination(this.midi);
		XGResponse r = m.request();
		this.values.take(r);
//		r.decodeXGValue(0, this.name);
		log.info(this.name.toString());
	}
/*
	public XGMidi getMidi()
	{	return this.midi;
	}
*/
	public XGValueStorage getValueStore()
	{	return this.values;
	}

	public String getName()
	{	return this.name.toString();
	}

	private void notifyConfigurationListeners()
	{	for(ConfigurationChangeListener l : this.configurationListeners) l.configurationChanged(ConfigurationEvent.Device);
	}

	@Override public String toString()
	{	return(this.getName() + " (" + this.sysexID + ")");
	}
}