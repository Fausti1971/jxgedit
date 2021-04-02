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
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGMessengerException;
import msg.XGRequest;
import msg.XGResponse;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;
import static value.XGValueStore.STORE;import xml.*;

public class XGMidi implements  XGLoggable, XGMessenger, CoreMidiNotification, Receiver, AutoCloseable, XMLNodeConstants
{	private static final int DEF_TIMEOUT = 100;
	private static XGMidi MIDI = null;
	private static XMLNode config = null;

	public static XGMidi getMidi()
	{	if(MIDI == null) XGMidi.init();
		return MIDI;
	}

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(TAG_MIDI);
		try
{
MIDI = new device.XGMidi(config);}catch(uk.co.xfactorylibrarians.coremidi4j.CoreMidiException e)
{
	e.printStackTrace();
}
	}

	public static Set<Info> INPUTS = new LinkedHashSet<>();
	public static Set<Info> OUTPUTS = new LinkedHashSet<>();

	static
	{	synchronized(INPUTS)
		{	initInputs();
		}
		synchronized(OUTPUTS)
		{	initOutputs();
		}
	}

	private static void initInputs()
	{	INPUTS.clear();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)	// i == i.getName() == dev.getDeviceInfo()
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxTransmitters() == 0) continue;
				INPUTS.add(i);
			}
			catch (MidiUnavailableException e)
			{	LOG.info(e.getMessage());
			}
		}
	}
	
	private static void initOutputs()
	{	OUTPUTS.clear();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxReceivers() == 0) continue;
				OUTPUTS.add(i);
			}
			catch (MidiUnavailableException e)
			{	LOG.info(e.getMessage());
			}
		}
	}

/******************************************************************************************************************/

	//public final ChangeableContent<Info> input = new ChangeableContent<Info>()
	//	{	@Override public Info getContent()
	//		{	if(getInput() != null) return getInput().getDeviceInfo();
	//			else return null;
	//		}
	//		@Override public boolean setContent(Info s)
	//		{	setInput(s);
	//			return true;
	//		}
	//	};
	//public final ChangeableContent<Info> output = new ChangeableContent<Info>()
	//	{	@Override public Info getContent()
	//		{	if(getOutput() != null) return getOutput().getDeviceInfo();
	//			else return null;
	//		}
	//		@Override public boolean setContent(Info s)
	//		{	setOutput(s);
	//			return true;
	//		}
	//	};
	//public final ChangeableContent<Integer> timeout = new ChangeableContent<Integer>()
	//	{	@Override public Integer getContent()
	//		{	return timeoutValue;
	//		}
	//		@Override public boolean setContent(Integer s)
	//		{	int old = getContent();
	//			setTimeout(s);
	//			return old != getContent();
	//		}
	//	};

	private Receiver transmitter;
	private MidiDevice midiOutput = null;
	private MidiDevice midiInput = null;
	private XGRequest request = null;
	private int timeoutValue;
//	private final XGMessageBuffer buffer;

	public XGMidi(xml.XMLNode cfg)throws uk.co.xfactorylibrarians.coremidi4j.CoreMidiException
	{
		this.setInput(cfg.getStringAttribute(ATTR_MIDIINPUT));
		this.setOutput(cfg.getStringAttribute(ATTR_MIDIOUTPUT));
		this.setTimeout(cfg.getIntegerAttribute(ATTR_MIDITIMEOUT, DEF_TIMEOUT));

		try
		{	uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider.addNotificationListener(this);
		}
		catch(uk.co.xfactorylibrarians.coremidi4j.CoreMidiException e)
		{	LOG.warning(e.getMessage());
		}
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
	{	//if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		if(dev != null)
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
	{//	if(this.queue.isAlive())this.queue.interrupt();
		if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		LOG.info("MidiInput closed: " + this.getInputName());
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		LOG.info("MidiOutput closed: " + this.getOutputName());
	}

	@Override public void midiSystemUpdated() throws CoreMidiException
	{	initInputs();
		initOutputs();
		this.setInput(config.getStringAttribute(ATTR_MIDIINPUT));
		this.setOutput(config.getStringAttribute(ATTR_MIDIOUTPUT));
		LOG.info("CoreMidiSystem updated, " + this.midiInput.getDeviceInfo() + "=" + this.midiInput.isOpen() + ", " + this.midiOutput.getDeviceInfo() + "=" + this.midiOutput.isOpen());
	}

	public void transmit(MidiMessage mm)
	{	this.transmitter.send(mm, -1L);
	}

	@Override public void submit(XGMessage m) throws XGMessengerException
	{	if(this.transmitter == null) throw new XGMessengerException(this + ": no transmitter initialized!");
		if(m == null)throw new XGMessengerException(this + ": message was null");
		m.setTimeStamp();
		this.transmitter.send((MidiMessage)m, -1L);
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	try
		{	XGMessage m = XGMessage.newMessage(this, STORE, mmsg);
			if(this.request != null && this.request.setResponsed((XGResponse)m))
			{	synchronized(this.request)
				{	this.request.notify();
				}
				return;
			}
			else STORE.submit(m);
		}
		catch(InvalidMidiDataException | InvalidXGAddressException | XGMessengerException e)
		{	LOG.info(e.getMessage());
		}
	}

	@Override public void request(XGRequest msg) throws XGMessengerException
	{	this.submit(msg);
		{	try
			{	this.request = msg;
				synchronized(this.request)
				{	this.request.wait(this.timeoutValue);
				}
			}
			catch (InterruptedException e)
			{	LOG.info(e.getMessage());
			}
		}
		this.request = null;
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