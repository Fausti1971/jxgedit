package device;

import application.XGLoggable;import javax.sound.midi.MidiMessage;import javax.sound.midi.Receiver;

public class XGNoneMidiReceiver implements Receiver
{
	public void send(MidiMessage message,long l)
	{	XGLoggable.LOG.warning("this device can't sending...");
	}

	public void close()
	{	XGLoggable.LOG.info("close");
	}
}
