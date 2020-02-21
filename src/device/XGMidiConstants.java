package device;

import application.Configurable;

//MidiInput, MidiOutput, MiidiTimeout

public interface XGMidiConstants extends Configurable
{
	public static final String
		TAG_MIDI = "midi",
		TAG_MIDIINPUT = "input",
		TAG_MIDIOUTPUT = "output",
		TAG_MIDITIMEOUT = "timeout";

	public final int
		DEF_TIMEOUT = 100;
}
