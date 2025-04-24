package device;

import java.util.*;
import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;import javax.swing.*;
import application.*;
import msg.*;
import xml.*;

public class XGMidi implements  XGLoggable, XGMessenger, Receiver, AutoCloseable, XMLNodeConstants
{	private static final int DEF_TIMEOUT = 300;
	private static XGMidi MIDI = null;
	private static XMLNode CONFIG = null;
	private static final Object LOCK = new Object();
	private static volatile XGRequest request = null;
	private static volatile Thread requestThread = null;
	public static final Set<Info> INPUTS = new LinkedHashSet<>();
	public static final Set<Info> OUTPUTS = new LinkedHashSet<>();

	public static XGMidi getMidi()
	{	if(MIDI == null) XGMidi.init();
		return MIDI;
	}

	public static void init()
	{	CONFIG = JXG.config.getChildNodeOrNew(TAG_MIDI);
		synchronized(LOCK){	initInputs();}
		synchronized(LOCK){	initOutputs();}
		try
		{	MIDI = new XGMidi(CONFIG);
		}
		catch(MidiUnavailableException e)
		{	JOptionPane.showMessageDialog(null, e.getMessage(), "MIDI", JOptionPane.WARNING_MESSAGE);
			LOG.severe(e.getMessage() + " I don't know, what is to do...");
			MIDI = new XGMidi(INPUTS.iterator().next(), OUTPUTS.iterator().next(), DEF_TIMEOUT);
		}
	}

	private static void initInputs()
	{	INPUTS.clear();
		LOG.info("Requesting MIDI Devices...");
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		LOG.info(infos.length + " MIDI-Devices detected");
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)	// i == i.getName() == dev.getDeviceInfo()
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxTransmitters() == 0) continue;
				INPUTS.add(i);
				LOG.info("MIDI-Input detected: " + i);
			}
			catch (MidiUnavailableException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}
	
	private static void initOutputs()
	{	OUTPUTS.clear();
		LOG.info("Requesting MIDI Devices...");
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		LOG.info(infos.length + " MIDI-Devices detected");
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxReceivers() == 0) continue;
				OUTPUTS.add(i);
				LOG.info("MIDI-Output detected: " + i);
			}
			catch (MidiUnavailableException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}

/******************************************************************************************************************/

	private Receiver transmitter;
	private MidiDevice output = null;
	private MidiDevice input = null;
	private int timeoutValue;
//	private final XGAddressableSet<XGResponse> buffer = new XGAddressableSet<>();

	private XGMidi(MidiDevice.Info input, MidiDevice.Info output, int timeout)
	{	try
		{	this.setInput(input);
			this.setOutput(output);
		}
		catch(MidiUnavailableException e)
		{	JOptionPane.showMessageDialog(null, e.getMessage(), "MIDI", JOptionPane.WARNING_MESSAGE);
		}
		this.setTimeout(timeout);
	}

	private XGMidi(XMLNode cfg)throws MidiUnavailableException
	{	this.setInput(cfg.getStringAttribute(ATTR_MIDIINPUT));
		this.setOutput(cfg.getStringAttribute(ATTR_MIDIOUTPUT));
		this.setTimeout(cfg.getIntegerAttribute(ATTR_MIDITIMEOUT, DEF_TIMEOUT));
	}

	public void setOutput(String s)throws MidiUnavailableException
	{	for(Info i : OUTPUTS)
		{	if(i.getName().equals(s))
			{	this.setOutput(i);
				return;
			}
		}
		throw new MidiUnavailableException(s + " does not exist!");
	}

	public void setOutput(Info i) throws MidiUnavailableException
	{	this.setOutput(MidiSystem.getMidiDevice(i));
	}

	public void setOutput(MidiDevice dev)throws MidiUnavailableException
	{	if(this.transmitter != null) this.transmitter.close();
		if(dev != null)
		{	if(!dev.isOpen()) dev.open();
			this.output = dev;
			this.transmitter = dev.getReceiver();
			LOG.info(this.getOutputName());
			CONFIG.setStringAttribute(ATTR_MIDIOUTPUT, this.getOutputName());
		}
	}

	public void setInput(String s)throws MidiUnavailableException
	{	for(Info i : INPUTS)
		{	if(i.getName().equals(s))
			{	this.setInput(i);
				return;
			}
		}
		throw new MidiUnavailableException(s + " does not exist!");
	}

	public void setInput(Info i)throws MidiUnavailableException
	{	this.setInput(MidiSystem.getMidiDevice(i));
	}

	public void setInput(MidiDevice dev) throws MidiUnavailableException
	{	if(dev != null)
		{	if(!dev.isOpen()) dev.open();
			dev.getTransmitter().setReceiver(this);
			this.input = dev;
			LOG.info(this.getInputName());
			CONFIG.setStringAttribute(ATTR_MIDIINPUT, this.getInputName());
		}
	}

	public MidiDevice getInput(){	return this.input;}

	public MidiDevice getOutput(){	return this.output;}

	private String getInputName()
	{	if(this.input == null) return "no input device";
		else return this.input.getDeviceInfo().getName();
	}

	private String getOutputName()
	{	if(this.output == null) return "no output device";
		else return this.output.getDeviceInfo().getName();
	}

	@Override public void close()
	{	if(this.input != null && this.input.isOpen()) this.input.close();
		LOG.info("MidiInput closed: " + this.getInputName());
		if(this.output != null && this.output.isOpen()) this.output.close();
		LOG.info("MidiOutput closed: " + this.getOutputName());
	}

	public void transmit(MidiMessage mm){	this.transmitter.send(mm, -1L);}

	@Override public void submit(XGMessageBulkDump m) throws XGMessengerException
	{	this.checkMessage(m);
		this.transmitter.send(m, -1L);
		try//Note to XG Data Writers: If sending consecutive bulk dumps, leave an interval of about 10ms between the F7 and the next F0.
		{	Thread.sleep(10);
		}
		catch(InterruptedException ignored){}
	}

	@Override public void submit(XGMessageParameterChange m) throws XGMessengerException
	{	this.checkMessage(m);
		this.transmitter.send(m, -1L);
	}

	@Override public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{	this.request(req);
	}

	@Override public void submit(XGMessageParameterRequest req) throws XGMessengerException
	{	this.request(req);
	}

	private void checkMessage(XGMessage msg) throws XGMessengerException
	{	if(this.transmitter == null) throw new XGMessengerException("no transmitter initialized!");
		if(msg == null) throw new XGMessengerException("message was null");
		msg.setTimeStamp();
	}

	private void request(XGRequest req) throws XGMessengerException
	{	this.checkMessage(req);
		request = req;
		requestThread = Thread.currentThread();
		this.transmitter.send(req, -1L);
		try{	Thread.sleep(this.timeoutValue);}
		catch(InterruptedException ignored){}
		request = null;
		requestThread = null;
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	try
		{	XGMessage m = XGMessage.newMessage(this, mmsg);
			if(m instanceof XGMessageBulkDump)//TODO: die anderen Messages noch bearbeiten
			{	XGMessageBulkDump r = (XGMessageBulkDump)m;
				if(request != null && request.setResponsedBy(r))
				{	request.getSource().submit(r);
					JXG.CURRENT_CONTENT.setValue("received from " + this);
					requestThread.interrupt();
				}
				else
				{
					LOG.info("unrequested message: " + r);
					XGDevice.DEVICE.submit(r);
					JXG.CURRENT_CONTENT.setValue("received from " + this);
				}
			}
			else LOG.info("unexpected message :" + m.toHexString());
		}
		catch(InvalidMidiDataException | XGMessengerException e)
		{	LOG.info(e.getMessage());
		}
	}

	@Override public boolean equals(Object o)
	{	if(this == o) return true;
		if(!(o instanceof XGMidi)) return false;
		return this.hashCode() == o.hashCode();
	}

	public int getTimeout(){	return this.timeoutValue;}

	public void setTimeout(int t)
	{	this.timeoutValue = t;
		CONFIG.setIntegerAttribute(ATTR_MIDITIMEOUT, t);
		LOG.info("" + t);
	}

	@Override public String toString(){	return this.getMessengerName();}

	@Override public String getMessengerName(){	return "MIDI" + " (" + this.getInputName() + ")";}
}