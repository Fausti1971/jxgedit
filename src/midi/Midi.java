//@format:off
package midi;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import adress.InvalidXGAdressException;
import application.Configuration;
import application.ConfigurationChangeListener;
import application.ConfigurationConstants;
import msg.XGMessage;
import msg.XGRequest;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;
/**
 *ein Singleton eines MidiOut-/Input des MidiSystems zum Senden und Empfangen
 */
public class Midi implements Receiver, ConfigurationConstants, CoreMidiNotification
{	private static Logger log = Logger.getAnonymousLogger();

	public static Vector<MidiDevice> getInputs()
	{	Vector<MidiDevice> inputs = new Vector<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i: infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxTransmitters() == 0 || tmpDev instanceof Sequencer || tmpDev instanceof Synthesizer) continue;
				inputs.add(tmpDev);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return inputs;
	}

	public static Vector<MidiDevice> getOutputs()
	{	Vector<MidiDevice> outputs = new Vector<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i: infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxReceivers() == 0 || tmpDev instanceof Sequencer || tmpDev instanceof Synthesizer) continue;
				outputs.add(tmpDev);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return outputs;
	}

	private static MidiDevice findInput(String name)
	{	for(MidiDevice d : getInputs()) if(d.getDeviceInfo().getName().equals(name)) return d;
		return null;
	}

	private static MidiDevice findOutput(String name)
	{	for(MidiDevice d : getOutputs()) if(d.getDeviceInfo().getName().equals(name)) return d;
		return null;
	}

/*******************************************************************************************************************************************/

	private XGDevice xgDev = null;
	private Receiver transmitter;
	private MidiDevice outDev;
	private MidiDevice inDev;
	private XGRequestQueue queue;
	private Set<ConfigurationChangeListener> listeners = new HashSet<>();

	private Midi(XGDevice dev, String out, String in)
	{	this(dev, findOutput(out), findInput(in));
	}

	private Midi(XGDevice dev, MidiDevice output, MidiDevice input)
	{	this.xgDev = dev;
		this.setOutput(output);
		this.setInput(input);
		this.queue = new XGRequestQueue(this);
		this.queue.start();
		try
		{	CoreMidiDeviceProvider.addNotificationListener(this);
		}
		catch(CoreMidiException e)
		{	e.printStackTrace();
		}
		this.listeners.add(Configuration.getConfig());
		this.notifyListeners();
	}

	public XGDevice getXGDevice()
	{	return this.xgDev;}

	public void setOutput(MidiDevice dev)
	{	if(this.outDev != null && this.outDev.isOpen()) this.outDev.close();
		if(dev == null) return;
		try
		{	dev.open();
			this.outDev = dev;
			this.transmitter = dev.getReceiver();
		}
		catch (MidiUnavailableException e)
		{	log.info(e.getMessage());
			return;
		}
		log.info(getOutputName() + ": " + this.getDeviceName());
		Configuration.getConfig().set(MIDIOUTPUT, this.getOutputName());
		this.notifyListeners();
//TODO autorequest DeviceInfo an reload xml; reInit nach MidiChange
	}

	public void setInput(MidiDevice dev)
	{	if(this.inDev != null && this.inDev.isOpen()) this.inDev.close();
		if(dev == null) return;
		try
		{	dev.getTransmitter().setReceiver(this);
			dev.open();
			this.inDev = dev;
		}
		catch (MidiUnavailableException e)
		{	log.info(e.getMessage());
			return;
		}
		log.info(getInputName() + ": " + this.getDeviceName());
		Configuration.getConfig().set(MIDIINPUT, this.getInputName());
		this.notifyListeners();
//TODO autorequest DeviceInfo an reload xml, reInit nach MidiChange
	}

	public MidiDevice getInput()
	{	return this.inDev;}

	public MidiDevice getOutput()
	{	return this.outDev;}

	public String getInputName()
	{	if(getInput() == null) return "no input device";
		else return this.inDev.getDeviceInfo().getName();
	}

	public String getOutputName()
	{	if(getOutput() == null) return "no output device";
		else return this.outDev.getDeviceInfo().getName();
	}

	private String getDeviceName()
	{	return this.xgDev.getName();
	}

	public void transmit(XGMessage msg)
	{	if(this.transmitter == null || msg == null) throw new RuntimeException("no transmitter initialized!");
		try
		{	msg.setTimeStamp(System.currentTimeMillis());
			this.transmitter.send(msg.asSysexMessage(), -1L);}
		catch (InvalidMidiDataException e)
		{	log.severe(e.getMessage() + msg);}
	}

	public void request(XGRequest msg)
	{	this.queue.add(msg);}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	XGMessage msg;
		try
		{	msg = XGMessage.factory(mmsg);
			msg.storeMessage();
		}
		catch (InvalidMidiDataException | InvalidXGAdressException e)
		{	log.info(e.getMessage());
			return;
		}
		this.queue.setResponsed(msg);
	}

	@Override public void close()
	{	if(this.queue.isAlive())this.queue.interrupt();
		if(this.inDev != null && this.inDev.isOpen()) this.inDev.close();
		log.info("MidiInput closed: " + this.getInputName());
		if(this.outDev != null && this.outDev.isOpen()) this.outDev.close();
		log.info("MidiOutput closed: " + this.getOutputName());
	}

	public void addListener(ConfigurationChangeListener l)
	{	this.listeners.add(l);
	}
	
	public void notifyListeners()
	{	for(ConfigurationChangeListener l : this.listeners) l.configurationChanged(ConfigurationEvent.Midi);
	}

	public void midiSystemUpdated() throws CoreMidiException
	{	this.notifyListeners();
	}
}