package device;

import static application.XGLoggable.LOG;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import java.util.ArrayList;
import java.util.List;

public class XGNoneMidiDevice implements MidiDevice
{
	MidiDevice.Info info = new XGNoneMidiDeviceInfo();
	XGNoneMidiReceiver receiver = new XGNoneMidiReceiver();
	ArrayList<Receiver> receivers = new ArrayList<>(){};
	XGNoneMidiTransmitter transmitter = new XGNoneMidiTransmitter();
	ArrayList<Transmitter> transmitters = new ArrayList<>(){};

	public Info getDeviceInfo(){ return info;}

	public void open() throws MidiUnavailableException
	{	LOG.info("open");
	}

	public void close()
	{	LOG.info("close");
	}

	public boolean isOpen()
	{	LOG.info("isOpen");
		return false;
	}

	public long getMicrosecondPosition()
	{	LOG.info("getMicrosecundPosition");
		return 0;
	}

	public int getMaxReceivers()
	{	LOG.info("getMaxReceivers");
		return 1;
	}

	public int getMaxTransmitters()
	{	LOG.info("getMaxTransmitters");
		return 1;
	}

	public Receiver getReceiver() throws MidiUnavailableException
	{	return this.receiver;
	}

	public List<Receiver> getReceivers()
	{	this.receivers.add(0, this.receiver);
		return receivers;
	}

	public Transmitter getTransmitter() throws MidiUnavailableException
	{	return this.transmitter;
	}

	public List<Transmitter> getTransmitters()
	{	transmitters.add(0, this.transmitter);
		return transmitters;
	}
}
