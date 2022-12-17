package device;

import javax.sound.midi.MidiDevice;

public class XGNoneMidiDeviceInfo extends MidiDevice.Info
{
	protected XGNoneMidiDeviceInfo()
	{	super("None", "Fausti", "None MIDI Device", "0.0.1");
	}
}
