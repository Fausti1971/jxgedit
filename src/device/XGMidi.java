package device;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import gui.GuiConfigurable;
import gui.XGFrame;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;
import xml.XMLNode;

public class XGMidi implements XGDeviceConstants, XGMessenger, CoreMidiNotification, GuiConfigurable, Receiver, AutoCloseable
{	static Logger log = Logger.getAnonymousLogger();

	public static Map<String, Info> getInputs()
	{	Map<String, Info> inputs = new LinkedHashMap<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxTransmitters() == 0 || tmpDev instanceof Sequencer || tmpDev instanceof Synthesizer) continue;
				inputs.put(i.getName(), i);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return inputs;
	}
	
	public static Map<String, Info> getOutputs()
	{	Map<String, Info> outputs = new LinkedHashMap<>();
		MidiDevice.Info[] infos = CoreMidiDeviceProvider.getMidiDeviceInfo();
		MidiDevice tmpDev = null;
		for (MidiDevice.Info i : infos)
		{	try
			{	tmpDev = MidiSystem.getMidiDevice(i);
				if(tmpDev.getMaxReceivers() == 0 || tmpDev instanceof Sequencer || tmpDev instanceof Synthesizer) continue;
				outputs.put(i.getName(), i);
			}
			catch (MidiUnavailableException e)
			{	log.info(e.getMessage());
			}
		}
		return outputs;
	}

/******************************************************************************************************************/

	private final XGDevice device;
	private final XMLNode config;
	private Receiver transmitter;
	private MidiDevice midiOutput;
	private MidiDevice midiInput;
	private XGRequest request = null;
	private int timeout = DEF_MIDITIMEOUT;
//	private final Set<ConfigurationChangeListener> configurationListeners = new HashSet<>();
	private XGAdressableSet<XGMessage> buffer = new XGAdressableSet<>();

	public XGMidi(XGDevice dev) throws MidiUnavailableException
	{	this.device = dev;
		this.config = this.device.getConfig().getChildNodeOrNew(TAG_MIDI);
		this.setInput(this.config.getChildNodeOrNew(TAG_MIDIINPUT).getTextContent());
		this.setOutput(this.config.getChildNodeOrNew(TAG_MIDIOUTPUT).getTextContent());
		this.timeout = this.config.parseChildNodeIntegerContent(TAG_MIDITIMEOUT, DEF_MIDITIMEOUT);

		try
		{	CoreMidiDeviceProvider.addNotificationListener(this);
		}
		catch(CoreMidiException e)
		{	e.printStackTrace();
		}
	}

	private void setOutput(String s) throws MidiUnavailableException
	{	if(s == null || !getOutputs().containsKey(s)) return;
		setOutput(MidiSystem.getMidiDevice(getOutputs().get(s)));
	}

	public boolean setOutput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.transmitter != null) this.transmitter.close();
		if(this.midiOutput != null && this.midiOutput.isOpen()) this.midiOutput.close();
		if(dev == null) return false;
		try
		{	dev.open();
			this.midiOutput = dev;
			this.transmitter = dev.getReceiver();
		}
		catch(MidiUnavailableException e)
		{	e.printStackTrace();
			this.transmitter = MidiSystem.getReceiver();
		}
		log.info(getOutputName());
		this.config.getChildNodeOrNew(TAG_MIDIOUTPUT).setTextContent(this.getOutputName());
//		this.notifyConfigurationListeners();
		return true;
	}

	private void setInput(String s) throws MidiUnavailableException
	{	if(s == null || !getInputs().containsKey(s)) return;
		setInput(MidiSystem.getMidiDevice(getInputs().get(s)));
	}

	public boolean setInput(MidiDevice dev) throws MidiUnavailableException
	{	if(this.midiInput != null && this.midiInput.isOpen()) this.midiInput.close();
		
		if(dev == null) return false;
		try
		{	dev.getTransmitter().setReceiver(this);
			dev.open();
			this.midiInput = dev;
		}
		catch(MidiUnavailableException e)
		{	e.printStackTrace();
			MidiSystem.getTransmitter().setReceiver(this);
		}
		log.info(getInputName());
		this.config.getChildNodeOrNew(TAG_MIDIINPUT).setTextContent(this.getInputName());
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
	{	synchronized(this)
		{	try
			{	XGMessage m = XGMessage.newMessage(this, mmsg);
				if(this.request != null && this.request.setResponsedBy((XGResponse)m))
				{	this.notify();
					return;
				}
				if(m.getDestination() == null)
				{	this.buffer.add(m);
					log.info("added to buffer: " + m.getInfo());
				}
				else m.getDestination().transmit(m);
			}
			catch(InvalidMidiDataException|InvalidXGAdressException | MidiUnavailableException e)
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

	public void midiSystemUpdated() throws CoreMidiException
	{//	this.notifyConfigurationListeners();
	}

	public void transmit(XGMessage msg) throws MidiUnavailableException
	{	if(this.transmitter == null) throw new MidiUnavailableException("no transmitter initialized!");
		if(msg == null) return;
		try
		{	msg.setTimeStamp();
			this.transmitter.send(msg.asSysexMessage(), -1L);
		}
		catch (InvalidMidiDataException e)
		{	log.severe(e.getMessage() + msg);
		}
	}

	public XGResponse request(XGRequest msg) throws TimeoutException, MidiUnavailableException//vom MIDI-Eingang
	{	synchronized(this)
		{	this.transmit(msg);
			this.request = msg;
			try
			{	this.wait(this.timeout);
//				throw new TimeoutException("MIDI-Timeout: " + this.getInputName()); 
			}
			catch(InterruptedException e)
			{	System.out.println("interrupted");
			}
			this.request = null;
			return msg.getResponse();
		}
	}

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

	public String getMessengerName()
	{	return "midi";
	}

	public XMLNode getConfig()
	{	return this.config;
	}

	public Component getConfigurationGuiComponents()
	{	XGFrame root = new XGFrame("midi");
//		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

		JList<String> inp = new JList<>(new Vector<>(XGMidi.getInputs().keySet()));
		inp.setBackground(root.getBackground());
		inp.setAlignmentX(0.5f);
		inp.setAlignmentY(0.5f);
//		inp.setBorder(getDefaultBorder("input"));
		inp.setSelectedValue(this.getInputName(), true);
		inp.addListSelectionListener(new ListSelectionListener()
		{	public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				JList<String> l = (JList<String>)e.getSource();
				try
				{	setInput(l.getSelectedValue());
				}
				catch(MidiUnavailableException e1)
				{	e1.printStackTrace();
				}
			}
		});
		root.add(inp);
		
		JList<String> out = new JList<>(new Vector<>(XGMidi.getOutputs().keySet()));
		out.setBackground(root.getBackground());
		out.setAlignmentX(0.5f);
		out.setAlignmentY(0.5f);
//		out.setBorder(getDefaultBorder("output"));
		out.setSelectedValue(this.getOutputName(), true);
		out.addListSelectionListener(new ListSelectionListener()
		{	public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				JList<String> l = (JList<String>)e.getSource();
				try
				{	setOutput(l.getSelectedValue());
				}
				catch(MidiUnavailableException e1)
				{	e1.printStackTrace();
				}
			}
		});
		root.add(out);

		JSpinner timeout = new JSpinner();
		timeout.setAlignmentX(0.5f);
		timeout.setAlignmentY(0.5f);
//		timeout.setBorder(getDefaultBorder("timeout"));
		timeout.setModel(new SpinnerNumberModel(this.getTimeout(), 30, 1000, 10));
		timeout.addChangeListener(new ChangeListener()
		{	public void stateChanged(ChangeEvent e)
			{	JSpinner s = (JSpinner)e.getSource();
				setTimeout((int)s.getModel().getValue());
			}
		});
		root.add(timeout);

		return root;
	}
}
