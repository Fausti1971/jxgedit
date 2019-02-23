//@format:off
package midi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import application.Setting;
import application.MU80;
import msg.XGMessage;
import msg.XGRequest;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;

public class Midi implements Receiver
{	private static Logger log = Logger.getAnonymousLogger();

	public static List<MidiDevice> getInputs()
	{	List<MidiDevice> inputs = new ArrayList<>();
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

	public static List<MidiDevice> getOutputs()
	{	List<MidiDevice> outputs = new ArrayList<>();
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

	private static MidiDevice getInput(String name)
	{	for(MidiDevice d : getInputs()) if(d.getDeviceInfo().getName().equals(name)) return d;
		return null;
	}

	private static MidiDevice getOutput(String name)
	{	for(MidiDevice d : getOutputs()) if(d.getDeviceInfo().getName().equals(name)) return d;
		return null;
	}

/*******************************************************************************************************************************************/

	private XGDevice xgDev = null;
	private Receiver transmitter;
	private MidiDevice outDev;
	private MidiDevice inDev;
	private XGRequestQueue queue;

	public Midi(XGDevice dev, String out, String in)
	{	this(dev, getOutput(out), getInput(in));
	}

	public Midi(XGDevice dev, MidiDevice output, MidiDevice input)
	{	this.xgDev = dev;
		this.setOutput(output);
		this.setInput(input);
		this.queue = new XGRequestQueue(this, MU80.getSetting().getInt(Setting.MIDITIMEOUT, 150));
		this.queue.start();
	}

	public void setOutput(MidiDevice dev)
	{	if(dev == null) return;
		if(this.outDev != null && this.outDev.isOpen()) this.outDev.close();
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
		xgDev.getSetting().set(Setting.MIDIOUTPUT, this.getOutputName());
	}

	public void setInput(MidiDevice dev)
	{	if(dev == null) return;
		if(this.inDev != null && this.inDev.isOpen()) this.inDev.close();
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
		xgDev.getSetting().set(Setting.MIDIINPUT, this.getInputName());
	}

	public String getInputName()
	{	if(this.inDev == null) return "no input device";
		else return this.inDev.getDeviceInfo().getName();
	}

	public String getOutputName()
	{	if(this.outDev == null) return "no output device";
		else return this.outDev.getDeviceInfo().getName();
	}

	private String getDeviceName()
	{	return this.xgDev.getName();
	}

	public void transmit(XGMessage msg)
	{	if(this.transmitter == null) return;
		try
		{	this.transmitter.send(msg.asSysexMessage(), -1L);
		}
		catch (InvalidMidiDataException e)
		{	log.severe(e.getMessage());
		}
	}

	public void request(XGRequest msg)
	{	this.queue.add(msg);
	}

	@Override public void send(MidiMessage mmsg, long timeStamp)	//send-methode des receivers (this); also eigentlich meine receive-methode
	{	XGMessage msg;
		try
		{	msg = XGMessage.factory(mmsg);
		}
		catch (InvalidMidiDataException e)
		{	log.info(e.getMessage());
			return;
		}
		this.queue.setResponsed(msg);
	}

	@Override public void close()
	{	if(this.queue.isAlive())this.queue.cancel();
		if(this.inDev != null && this.inDev.isOpen()) this.inDev.close();
		log.info("MidiInput closed: " + getInputName());
		if(this.outDev != null && this.outDev.isOpen()) this.outDev.close();
		log.info("MidiOutput closed: " + getOutputName());
	}
}