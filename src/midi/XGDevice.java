package midi;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import application.Configuration;
import application.ConfigurationChangeListener;
import application.ConfigurationConstants;
import msg.XGMessage;
/**
 * 
 *Singleton eines XG-Instruments
 */
public class XGDevice implements ConfigurationConstants
{	private static final int DEF_MIDITIMEOUT = 150;
	private static XGDevice DEVICE;
	private Logger log = Logger.getAnonymousLogger();

	public static XGDevice getDevice()
	{	return DEVICE;
	}

	public static void initDevice() throws MidiUnavailableException
	{	DEVICE = new XGDevice();}

	private static XGDevice detectDevices()	//scannt alle MdidDevices nach XGDevices
	{	return null;
	}

	private static XGDevice detectDevices(Midi midi)	//scannt angegebenes Midi nach XGDevices
	{	XGDevice d = new XGDevice(midi);
		return d;
	}

/***************************************************************************************************************************/

	private String name = "unknown device";
	private final Midi midi;
	private int midiTimeout;
	private final Set<ConfigurationChangeListener> listeners = new HashSet<>();

	private XGDevice() throws MidiUnavailableException	//für Initialisation via Setting (file)
	{	this.midi = Midi.factory(Configuration.getConfig().getProperty(MIDIOUTPUT), Configuration.getConfig().getProperty(MIDIINPUT));
		this.midiTimeout = Configuration.getConfig().getInt(MIDITIMEOUT, DEF_MIDITIMEOUT);
		this.listeners.add(Configuration.getConfig());
		this.notifyListeners();
	}

	public XGDevice(Midi midi)
	{	this.midi = midi;
		this.midiTimeout = Configuration.getConfig().getInt(MIDITIMEOUT, DEF_MIDITIMEOUT);
	}

	public int getSysexId()
	{	return Configuration.getConfig().getInt(SYSEXID, 0);
	}

	void setSysexId(int id)
	{	Configuration.getConfig().setInt(SYSEXID, id & 0xF);
		this.notifyListeners();
	}

	public String getName()
	{	return this.name;
	}

	public void setName(String name)
	{	this.name = name;
		this.notifyListeners();
	}

	public Midi getMidi()
	{	return this.midi;
	}

	public int getTimeout()
	{	return this.midiTimeout;
	}

	public void transmit(XGMessage msg)
	{	this.midi.transmit(msg);
	}

	public void close()
	{	if(midi != null) midi.close();
		this.notifyListeners();
	}

	private void notifyListeners()
	{	for(ConfigurationChangeListener l : this.listeners) l.configurationChanged(ConfigurationEvent.Device);
	}

	@Override public String toString()
	{	return(this.getName() + " (" + this.getSysexId() + ")");
	}
}
