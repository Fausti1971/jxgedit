package device;

import javax.sound.midi.MidiDevice;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiNotification;

public class Example
{	public static boolean isCoreMidiLoaded() throws CoreMidiException
	{	return CoreMidiDeviceProvider.isLibraryLoaded();
	}

	public static void watchForMidiChanges() throws CoreMidiException
	{	CoreMidiDeviceProvider.addNotificationListener(new CoreMidiNotification()
		{	@Override public void midiSystemUpdated()
			{	System.out.println("The MIDI environment has changed.");
			}
		});
	}

	public static void main(String[] args) throws Exception
	{	System.out.println("Working MIDI Devices:");
		for (MidiDevice.Info device : CoreMidiDeviceProvider.getMidiDeviceInfo()) System.out.println("  " + device);
		if (Example.isCoreMidiLoaded())
		{	System.out.println("CoreMIDI4J native library (Version " + CoreMidiDeviceProvider.getLibraryVersion() + ") is running.");
			watchForMidiChanges();
			System.out.println("Watching for MIDI environment changes for thirty seconds.");
			Thread.sleep(30000);
		}
		else
		{	System.out.println("CoreMIDI4J native library is not available.");
		}
	}
}