package device;

import static application.XGLoggable.LOG;
import javax.sound.midi.Receiver;
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
	{	LOG.info("close");
	}
}
