package midi;

import java.util.logging.Logger;
import application.MU80;
import application.Setting;
import msg.XGMessage;

public class XGDevice
{	private static final Logger log = Logger.getAnonymousLogger();

/***************************************************************************************************************************/

	private Midi midi = null;

	public XGDevice(Setting setting)	//für Initialisation via Setting (file)
	{	this.midi = new Midi(this, setting.get(Setting.MIDIOUTPUT), setting.get(Setting.MIDIINPUT));
	}

	public int getSysexId()
	{	return getSetting().getInt(Setting.SYSEXID, 0);
	}

	void setSysexId(int id)
	{	getSetting().setInt(Setting.SYSEXID, id & 0xF);
	}

	public Setting getSetting()
	{	return MU80.getSetting();
	}

	public String getName()
	{	return getSetting().get(Setting.DEVICENAME, "unknown device");
	}

	public void setName(String name)
	{	getSetting().set(Setting.DEVICENAME, name);
	}

	public Midi getMidi()
	{	return this.midi;
	}

	public void transmit(XGMessage msg)
	{	this.midi.transmit(msg);
	}

	public void close()
	{	if(midi != null) midi.close();
	}

	@Override public String toString()
	{	return(this.getName() + " (" + this.getSysexId() + ")");
	}

	@Override public int hashCode()
	{	return this.getSysexId() + this.getName().hashCode() + this.midi.getOutputName().hashCode() + this.midi.getInputName().hashCode();
	}
}
