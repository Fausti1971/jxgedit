package device;

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
import javax.swing.JOptionPane;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import application.Configuration;
import application.ConfigurationChangeListener;
import application.ConfigurationConstants;
import application.NamedVector;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import msg.XGMessenger.XGMessengerType;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;

public class XGMidi implements XGMessenger, CoreMidiNotification, ConfigurationConstants, Receiver
{	static Logger log = Logger.getAnonymousLogger();

	public static NamedVector<MidiDevice> getInputs()
	{	NamedVector<MidiDevice> inputs = new NamedVector<>();
		inputs.setName("MIDI Inputs");
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
	
	public static NamedVector<MidiDevice> getOutputs()
	{	NamedVector<MidiDevice> outputs = new NamedVector<>();
		outputs.setName("MIDI Outputs");
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
	
	static MidiDevice findMidiDevice(String name, NamedVector<MidiDevice> vec)
	{	if(vec == null) return null;
		while(name == null) name = selectMidiDevice(vec);
		for(MidiDevice d : vec) if(d.getDeviceInfo().getName().equals(name)) return d;
		return null;
	}

	static String selectMidiDevice(NamedVector<MidiDevice> vec)
	{	Vector<String> v = new Vector<String>();
		for(MidiDevice d : vec) v.add(d.getDeviceInfo().getName());
		return (String)JOptionPane.showInputDialog(null, "Select " + vec.getName(), vec.getName(), JOptionPane.QUESTION_MESSAGE, null, v.toArray(), null);
	}

/******************************************************************************************************************/

	private Receiver transmitter;
	private MidiDevice midiOutput;
	private MidiDevice midiInput;
	private int midiTimeout = DEF_MIDITIMEOUT, sysexID = 0;
	private final Set<ConfigurationChangeListener> configurationListeners = new HashSet<>();
	private XGAdressableSet<XGMessage> buffer = new XGAdressableSet<>();

	public XGMidi(Configuration cfg) throws MidiUnavailableException
	{	String input = cfg.getProperty(MIDIINPUT);
		String output = cfg.getProperty(MIDIOUTPUT);
		this.sysexID = cfg.getInt(SYSEXID, 0);
		this.midiTimeout = cfg.getInt(MIDITIMEOUT, DEF_MIDITIMEOUT);

		boolean cancel = false;
		while(cancel == true)
		{	cancel = this.setOutput(XGMidi.findMidiDevice(output, XGMidi.getOutputs()));
			cancel = this.setInput(XGMidi.findMidiDevice(input, XGMidi.getInputs()));
		}
		try
		{	CoreMidiDeviceProvider.addNotificationListener(this);
		}
		catch(CoreMidiException e)
		{	e.printStackTrace();
		}
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public boolean setOutput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		if(dev == null) return false;
		dev.open();
		this.midiOutput = dev;
		this.transmitter = dev.getReceiver();
		log.info(getOutputName());
		this.config.set(MIDIOUTPUT, this.getOutputName());
		this.notifyConfigurationListeners();
		return true;
	}

	public boolean setInput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		if(dev == null) return false;
		dev.getTransmitter().setReceiver(this);
		dev.open();
		this.midiInput = dev;
		log.info(getInputName());
		this.config.set(MIDIINPUT, this.getInputName());
		this.notifyConfigurationListeners();
		return true;
	}

	public MidiDevice getInput()
	{	return this.midiInput;}

	public MidiDevice getOutput()
	{	return this.midiOutput;}

	public String getInputName()
	{	if(getInput() == null) return "no input device";
		else return this.midiInput.getDeviceInfo().getName();
	}

	public String getOutputName()
	{	if(getOutput() == null) return "no output device";
		else return this.midiOutput.getDeviceInfo().getName();
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	try
		{	XGMessage m = XGMessage.newMessage(this, mmsg);
			if(m.getDestination() == null) this.buffer.add(m);
			else m.getDestination().take(m);
		}
		catch(InvalidMidiDataException|InvalidXGAdressException e)
		{	e.printStackTrace();
		}
	}

	@Override public void close()
	{//	if(this.queue.isAlive())this.queue.interrupt();
		if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		log.info("MidiInput closed: " + this.getInputName());
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		log.info("MidiOutput closed: " + this.getOutputName());
	}

	public void midiSystemUpdated() throws CoreMidiException
	{//	this.notifyConfigurationListeners();
	}

	public void take(XGMessage msg)
	{	if(this.transmitter == null || msg == null) throw new RuntimeException("no transmitter initialized!");
		try
		{	msg.setTimeStamp();
			this.transmitter.send(msg.asSysexMessage(), -1L);
		}
		catch (InvalidMidiDataException e)
		{	log.severe(e.getMessage() + msg);
		}
	}

	public XGResponse pull(XGRequest msg)//vom MIDI-Eingang
	{	return null;//TODO transmit, wait for response or timeout and return it or null?
	}

	public XGMessengerType getMessengerType()
	{	return XGMessengerType.Device;
	}

	public String getMessengerName()
	{	return this.getName();
	}

}
