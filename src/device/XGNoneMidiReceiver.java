package device;

import static application.XGLoggable.LOG;import javax.sound.midi.MidiMessage;import javax.sound.midi.Receiver;

public class XGNoneMidiReceiver implements Receiver
{
	public void send(MidiMessage message,long l)
	{	LOG.warning("this device can't sending...");
	}

	public void close()
	{	LOG.info("close");
	}
}
