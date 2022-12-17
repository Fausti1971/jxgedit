package device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

public class XGNoneMidiProvider extends MidiDeviceProvider
{
	final MidiDevice.Info[] infos = new MidiDevice.Info[1];
	final XGNoneMidiDevice device;

	XGNoneMidiProvider()
	{	this.device = new XGNoneMidiDevice();
		this.infos[0] = this.device.getDeviceInfo();
	}

	public MidiDevice.Info[] getDeviceInfo()
	{	return this.infos;
	}

	public MidiDevice getDevice(MidiDevice.Info info)
	{	if(this.infos[0].equals(info)) return this.device;
		throw new IllegalArgumentException(String.valueOf(info));
	}
}
