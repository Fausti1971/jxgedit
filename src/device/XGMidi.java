package device;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import adress.InvalidXGAddressException;
import application.Configurable;
import msg.XGMessage;
import msg.XGMessageBuffer;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;
import xml.XMLNode;

public class XGMidi implements XGMidiConstants, XGMessenger, CoreMidiNotification, Configurable, Receiver, AutoCloseable
{	static Logger log = Logger.getAnonymousLogger();

	public static Set<Info> getInputs()
	{	Set<Info> inputs = new LinkedHashSet<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)	// i == i.getName() == dev.getDeviceInfo()
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxTransmitters() == 0) continue;
				inputs.add(i);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return inputs;
	}
	
	public static Set<Info> getOutputs()
	{	Set<Info> outputs = new LinkedHashSet<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxReceivers() == 0) continue;
				outputs.add(i);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return outputs;
	}

/******************************************************************************************************************/

	private final Thread thread;
	private final XGDevice device;
	private final XMLNode config;
	private Receiver transmitter;
	private MidiDevice midiOutput = null;
	private MidiDevice midiInput = null;
	private XGRequest request = null;
	private int timeout = DEF_MIDITIMEOUT;
//	private final Set<ConfigurationChangeListener> configurationListeners = new HashSet<>();
	private final XGMessageBuffer buffer;

	public XGMidi(XGDevice dev)
	{	this.device = dev;
		this.config = this.device.getConfig().getChildNodeOrNew(TAG_MIDI);
		this.setInput(this.config.getChildNodeOrNew(TAG_MIDIINPUT).getTextContent());
		this.setOutput(this.config.getChildNodeOrNew(TAG_MIDIOUTPUT).getTextContent());
		this.timeout = this.config.parseChildNodeIntegerContent(TAG_MIDITIMEOUT, DEF_MIDITIMEOUT);
		this.buffer = new XGMessageBuffer(this);

		try
		{	CoreMidiDeviceProvider.addNotificationListener(this);
		}
		catch(CoreMidiException e)
		{	e.printStackTrace();
		}
		this.thread = new Thread(this);
		this.thread.run();
	}

	private void setOutput(String s)
	{	for(Info i : getOutputs())
		{	if(i.getName().equals(s))
				try
				{	this.setOutput(MidiSystem.getMidiDevice(i));
				}
				catch(MidiUnavailableException e)
				{	e.printStackTrace();
				}
		}
	}

	public void setOutput(MidiDevice dev)
	{	if(this.transmitter != null) this.transmitter.close();
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		this.midiOutput = dev;
		if(dev != null)
		{	try
			{	dev.open();
				this.transmitter = dev.getReceiver();
			}
			catch(MidiUnavailableException e)
			{	e.printStackTrace();
				try
				{	this.transmitter = MidiSystem.getReceiver();
				}
				catch(MidiUnavailableException e1)
				{	e1.printStackTrace();
				}
			}
		}
		log.info(this.getOutputName());
		this.config.getChildNodeOrNew(TAG_MIDIOUTPUT).setTextContent(this.getOutputName());
//		this.notifyConfigurationListeners();
		return;
	}

	private void setInput(String s)
	{	for(Info i : getInputs())
		{	if(i.getName().equals(s))
				try
				{	this.setInput(MidiSystem.getMidiDevice(i));
				}
				catch(MidiUnavailableException e)
				{	e.printStackTrace();
				}
		}
	}

	public void setInput(MidiDevice dev)
	{	if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		this.midiInput = dev;
		if(dev != null)
		{	try
			{	dev.getTransmitter().setReceiver(this);
				dev.open();
			}
			catch(MidiUnavailableException e)
			{	e.printStackTrace();
				try
				{	MidiSystem.getTransmitter().setReceiver(this);
				}
				catch(MidiUnavailableException e1)
				{	e1.printStackTrace();
				}
			}
		}
		log.info(this.getInputName());
		this.config.getChildNodeOrNew(TAG_MIDIINPUT).setTextContent(this.getInputName());
//		this.notifyConfigurationListeners();
		return;
	}

	public MidiDevice getInput()
	{	return this.midiInput;
	}

	public MidiDevice getOutput()
	{	return this.midiOutput;
	}

	public String getInputName()
	{	if(getInput() == null) return "no input device";
		else return this.midiInput.getDeviceInfo().getName();
	}

	public String getOutputName()
	{	if(getOutput() == null) return "no output device";
		else return this.midiOutput.getDeviceInfo().getName();
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	synchronized(this)
		{	try
			{	XGMessage m = XGMessage.newMessage(this, mmsg);
				if(this.request != null && this.request.setResponsedBy((XGResponse)m))
				{	this.notify();
					return;
				}
				if(m.getDestination() == null) m.setDestination(this.buffer);
				m.getDestination().submit((XGResponse)m);
			}
			catch(InvalidMidiDataException|InvalidXGAddressException e)
			{	log.info(e.getMessage());
			}
		}
	}

	@Override public void close()
	{//	if(this.queue.isAlive())this.queue.interrupt();
		if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		log.info("MidiInput closed: " + this.getInputName());
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		log.info("MidiOutput closed: " + this.getOutputName());
	}

	@Override public void midiSystemUpdated() throws CoreMidiException
	{	log.info("CoreMidiSystem updated!");
		//	this.notifyConfigurationListeners();
	}

	private boolean transmit(XGMessage m)
	{	if(this.transmitter == null)
		{	log.info("no transmitter initialized!");
			return false;
		}
		if(m == null)
		{	log.info("message was null");
			return false;
		}
		m.setTimeStamp();
		this.transmitter.send((MidiMessage)m, -1L);
		return true;
	}

	@Override public void submit(XGMessage msg)
	{	this.transmit(msg);
	}
/*
	@Override public XGResponse pull(XGRequest msg) throws TimeoutException	//TODO:
	{	synchronized(this)
		{	if(this.transmit(msg))
			{	this.request = msg;
				try
				{	this.wait(this.timeout);
					if(!this.request.isResponsed()) throw new TimeoutException("midi timeout: " + this.getInputName() + " after " + (System.currentTimeMillis() - msg.getTimeStamp()) + " ms"); //Thread läuft nach notify() ganz normal weiter
				}
				catch(InterruptedException e)
				{	System.out.println("interrupted");
				}
			}
			this.request = null;
			return msg.getResponse();
		}
	}
*/
	@Override public int hashCode()
	{	if(this.midiInput == null || this.midiOutput == null) return HASH;
		return HASH * this.midiInput.hashCode() + HASH * this.midiOutput.hashCode();
	}

	@Override public boolean equals(Object o)
	{	if(this == o) return true;
		if(!(o instanceof XGMidi)) return false;
		return this.hashCode() == o.hashCode();
	}

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	public int getTimeout()
	{	return this.timeout;
	}

	public void setTimeout(int t)
	{	this.timeout = t;
		this.config.getChildNodeOrNew(TAG_MIDITIMEOUT).setTextContent(t);
		log.info("timeout set to " + t);
	}

	@Override public String getMessengerName()
	{	return this.getInputName();
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void run()
	{	
	}

}
