package device;

import application.XGLoggable;import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import java.util.ArrayList;
import java.util.List;

public class XGNoneMidiDevice implements MidiDevice
{
	final MidiDevice.Info info = new XGNoneMidiDeviceInfo();
	final XGNoneMidiReceiver receiver = new XGNoneMidiReceiver();
	final ArrayList<Receiver> receivers = new ArrayList<>(){};
	final XGNoneMidiTransmitter transmitter = new XGNoneMidiTransmitter();
	final ArrayList<Transmitter> transmitters = new ArrayList<>(){};

	public Info getDeviceInfo(){ return info;}

	public void open()
	{	XGLoggable.LOG.info("open");
	}

	public void close()
	{	XGLoggable.LOG.info("close");
	}

	public boolean isOpen()
	{	XGLoggable.LOG.info("isOpen");
		return false;
	}

	public long getMicrosecondPosition()
	{	XGLoggable.LOG.info("getMicrosecundPosition");
		return 0;
	}

	public int getMaxReceivers()
	{	XGLoggable.LOG.info("getMaxReceivers");
		return 1;
	}

	public int getMaxTransmitters()
	{	XGLoggable.LOG.info("getMaxTransmitters");
		return 1;
	}

	public Receiver getReceiver()
	{	return this.receiver;
	}

	public List<Receiver> getReceivers()
	{	this.receivers.add(0, this.receiver);
		return receivers;
	}

	public Transmitter getTransmitter()
	{	return this.transmitter;
	}

	public List<Transmitter> getTransmitters()
	{	transmitters.add(0, this.transmitter);
		return transmitters;
	}
}
