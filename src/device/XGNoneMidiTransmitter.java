package device;

import application.XGLoggable;import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class XGNoneMidiTransmitter implements Transmitter
{
	Receiver receiver = new XGNoneMidiReceiver();

	public void setReceiver(Receiver receiver)
	{	this.receiver = receiver;
	}

	public Receiver getReceiver()
	{	return this.receiver;
	}

	public void close()
	{	XGLoggable.LOG.info("close");
	}
}
