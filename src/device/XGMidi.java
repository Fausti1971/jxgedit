package device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.InvalidXGAddressException;
import application.*;
import msg.*;
import xml.*;

public class XGMidi implements  XGLoggable, XGMessenger, Receiver, AutoCloseable, XMLNodeConstants
{	private static final int DEF_TIMEOUT = 300;
	private static XGMidi MIDI = null;
	private static XMLNode config = null;
	private static final Object lock = new Object();
	private static Thread reqThr;

	public static XGMidi getMidi()
	{	if(MIDI == null) XGMidi.init();
		return MIDI;
	}

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(TAG_MIDI);
		MIDI = new XGMidi(config);
		}

	public static Set<Info> INPUTS = new LinkedHashSet<>();
	public static Set<Info> OUTPUTS = new LinkedHashSet<>();

	static
	{	synchronized(lock)
		{	initInputs();
		}
		synchronized(lock)
		{	initOutputs();
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
			{	LOG.info(e.getMessage());
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
			{	LOG.info(e.getMessage());
			}
		}
	}

/******************************************************************************************************************/

	private Receiver transmitter;
	private MidiDevice midiOutput = null;
	private MidiDevice midiInput = null;
	private volatile XGRequest request = null;
	private int timeoutValue;
//	private final XGMessageBuffer buffer;

	public XGMidi(xml.XMLNode cfg)
	{	this.setInput(cfg.getStringAttribute(ATTR_MIDIINPUT));
		this.setOutput(cfg.getStringAttribute(ATTR_MIDIOUTPUT));
		this.setTimeout(cfg.getIntegerAttribute(ATTR_MIDITIMEOUT, DEF_TIMEOUT));
	}

	private void setOutput(String s)
	{	for(Info i : OUTPUTS) if(i.getName().equals(s)) this.setOutput(i);
	}

	public void setOutput(Info i)
	{	try
		{	this.setOutput(MidiSystem.getMidiDevice(i));
		}
		catch(MidiUnavailableException e)
		{	LOG.warning(e.getMessage());
		}
	}

	private void setOutput(MidiDevice dev)
	{	if(this.transmitter != null) this.transmitter.close();
//		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		if(dev != null)
		{	try
			{	if(!dev.isOpen()) dev.open();
				this.midiOutput = dev;
				this.transmitter = dev.getReceiver();
			}
			catch(MidiUnavailableException e)
			{	javax.swing.JOptionPane.showMessageDialog(null, e.getMessage() + ": " + dev.getDeviceInfo().getName(), "MIDI Output", javax.swing.JOptionPane.WARNING_MESSAGE);
				LOG.severe(e.getMessage());
			}
		}
		LOG.info(this.getOutputName());
		config.setStringAttribute(ATTR_MIDIOUTPUT, this.getOutputName());
	}

	private void setInput(String s)
	{	for(Info i : INPUTS) if(i.getName().equals(s)) this.setInput(i);
	}

	public void setInput(Info i)
	{	try
		{	this.setInput(MidiSystem.getMidiDevice(i));
		}
		catch(MidiUnavailableException e)
		{	LOG.warning(e.getMessage());
		}
	}

	private void setInput(MidiDevice dev)
	{	if(dev != null)
		{	try
			{	if(!dev.isOpen()) dev.open();
				dev.getTransmitter().setReceiver(this);
				this.midiInput = dev;
			}
			catch(MidiUnavailableException e)
			{	javax.swing.JOptionPane.showMessageDialog(null, e.getMessage() + ": " + dev.getDeviceInfo().getName(), "MIDI Input", javax.swing.JOptionPane.WARNING_MESSAGE);
				LOG.severe(e.getMessage());
			}
		}
		LOG.info(this.getInputName());
		config.setStringAttribute(ATTR_MIDIINPUT, this.getInputName());
	}

	public MidiDevice getInput()
	{	return this.midiInput;
	}

	public MidiDevice getOutput()
	{	return this.midiOutput;
	}

	private String getInputName()
	{	if(this.midiInput == null) return "no input device";
		else return this.midiInput.getDeviceInfo().getName();
	}

	private String getOutputName()
	{	if(this.midiOutput == null) return "no output device";
		else return this.midiOutput.getDeviceInfo().getName();
	}

	@Override public void close()
	{	if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		LOG.info("MidiInput closed: " + this.getInputName());
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		LOG.info("MidiOutput closed: " + this.getOutputName());
	}

	public void transmit(MidiMessage mm)
	{	this.transmitter.send(mm, -1L);
	}

	@Override public void submit(XGResponse m) throws XGMessengerException
	{	if(this.transmitter == null) throw new XGMessengerException("no transmitter initialized!");
		if(m == null)throw new XGMessengerException("message was null");
		int timeout = 0;
		m.setTimeStamp();
//Note to XG Data Writers: If sending consecutive bulk dumps, leave an interval of about 10ms between the F7 and the next F0.
		if(m instanceof XGMessageBulkDump) timeout = 10;
		this.transmitter.send((MidiMessage)m, -1L);
		try{	Thread.sleep(timeout);}
		catch(InterruptedException ignored){}
	}

	@Override public void submit(XGRequest req) throws XGMessengerException
	{	if(this.transmitter == null) throw new XGMessengerException("no transmitter initialized!");
		if(req == null)throw new XGMessengerException("message was null");
		req.setTimeStamp();
		this.request = req;
		reqThr = Thread.currentThread();
		this.transmitter.send(req, -1L);
		try{	Thread.sleep(this.timeoutValue);}
		catch(InterruptedException ignored){}
		this.request = null;
		reqThr = null;
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	try
		{	XGMessage m = XGMessage.newMessage(this, mmsg);
			if(this.request != null && m instanceof XGResponse && this.request.setResponsedBy((XGResponse)m))
			{	this.request.getSource().submit((XGResponse)m);
				reqThr.interrupt();
			}
//			else XGMessageStore.STORE.submit(m);
		}
		catch(InvalidMidiDataException | InvalidXGAddressException | XGMessengerException e)
		{	LOG.info(e.getMessage());
		}
	}

	@Override public boolean equals(Object o)
	{	if(this == o) return true;
		if(!(o instanceof XGMidi)) return false;
		return this.hashCode() == o.hashCode();
	}

	public int getTimeout()
	{	return this.timeoutValue;
	}

	public void setTimeout(int t)
	{	this.timeoutValue = t;
		config.setIntegerAttribute(ATTR_MIDITIMEOUT, t);
		LOG.info("" + t);
	}

	@Override public String toString()
	{	return this.getMessengerName();
	}

	@Override public String getMessengerName()
	{	return "MIDI" + " (" + this.getInputName() + ")";
	}

	public JComponent getConfigComponent()
	{	GridBagConstraints gbc = new GridBagConstraints();
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());

//		c = new XGSpinner("timeout", this.timeout, 30, 1000, 10);
//		gbc.gridx = 0;
//		gbc.gridy = GridBagConstraints.RELATIVE;
////		gbc.gridwidth = 1;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		root.add(c, gbc);

		return root;
	}
}