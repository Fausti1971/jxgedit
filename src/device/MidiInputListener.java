package device;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import adress.InvalidXGAdressException;

public interface MidiInputListener
{
	void messageReceived(XGDevice dev, MidiMessage mmsg) throws InvalidMidiDataException, InvalidXGAdressException;
}
