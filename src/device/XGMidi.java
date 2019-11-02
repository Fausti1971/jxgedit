package device;

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
import application.ConfigurationConstants;
import application.NamedVector;
import application.Rest;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
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

	private final XGDevice device;
	private Receiver transmitter;
	private MidiDevice midiOutput;
	private MidiDevice midiInput;
	private XGRequest request = null;
	private int midiTimeout = DEF_MIDITIMEOUT;
//	private final Set<ConfigurationChangeListener> configurationListeners = new HashSet<>();
	private XGAdressableSet<XGMessage> buffer = new XGAdressableSet<>();

	public XGMidi(XGDevice dev) throws MidiUnavailableException
	{	this.device = dev;
		String input = dev.getConfig().getChildNode(TAG_MIDIINPUT).getTextContent();
		String output = dev.getConfig().getChildNode(TAG_MIDIOUTPUT).getTextContent();
		this.midiTimeout = Rest.parseIntOrDefault(dev.getConfig().getChildNode(TAG_MIDITIMEOUT).getTextContent(), DEF_MIDITIMEOUT);

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

	public boolean setOutput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		if(dev == null) return false;
		dev.open();
		this.midiOutput = dev;
		this.transmitter = dev.getReceiver();
		log.info(getOutputName());
		this.device.getConfig().getChildNode(TAG_MIDIOUTPUT).setTextContent(this.getOutputName());
//		this.notifyConfigurationListeners();
		return true;
	}

	public boolean setInput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		if(dev == null) return false;
		dev.getTransmitter().setReceiver(this);
		dev.open();
		this.midiInput = dev;
		log.info(getInputName());
		this.device.getConfig().getChildNode(TAG_MIDIINPUT).setTextContent(this.getInputName());
//		this.notifyConfigurationListeners();
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
			if(this.request != null && this.request.setResponsedBy((XGResponse)m))
			{	this.notify();
				return;
			}
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

	public XGResponse pull(XGRequest msg) throws TimeoutException//vom MIDI-Eingang
	{	this.take(msg);
		this.request = msg;
		try
		{	this.wait(this.midiTimeout);
		}
		catch(InterruptedException e)
		{	e.printStackTrace();
			this.request = null;
			return msg.getResponse();
		}
		this.request = null;
		throw new TimeoutException("MIDI-Timeout: " + this.getInputName());
	}

	@Override
	public XGDevice getDevice()
	{	return this.device;
	}

	public XGMessengerType getMessengerType()
	{	return XGMessengerType.Midi;
	}

	public String getMessengerName()
	{	return this.getMessengerType().name();
	}
}
