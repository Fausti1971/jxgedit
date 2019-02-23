package midi;

import java.util.logging.Logger;
import application.MU80;
import application.Setting;
import msg.XGMessage;

public class XGDevice
{	private static final Logger log = Logger.getAnonymousLogger();

/*	public static void init()
	{	File[] files = MU80.getDevicePath().toFile().listFiles();
		for(File f : files)
		{	if(f == null) return;
			if(f.isDirectory()) continue;
			if(f.isHidden()) continue;
			if(f.getName().equals(MU80.getAppName())) continue;
			new XGDevice(new Setting(f));
		}
		if(instances.isEmpty())
		{	Set<XGDeviceDetector> devs = XGDeviceDetector.detectXGDevices();
			if(devs.isEmpty()) log.info("no XG-Devices detected!");
			else for(XGDeviceDetector d : devs) new XGDevice(d);
		}
	}
*/

/***************************************************************************************************************************/

	private Midi midi = null;

	public XGDevice(Setting setting)	//für Initialisation via Setting (file)
	{	this.midi = new Midi(this, setting.get(Setting.MIDIOUTPUT), setting.get(Setting.MIDIINPUT));
	}
/*
	private XGDevice(XGDeviceDetector d)		//	nur für Initialisiation via XGDeviceDetector
	{	this.setting = new Setting();	//temporär
		this.setName(d.name);
		this.setSysexId(d.sysexId);
		this.midi = new Midi(this, d.output, d.input);	//schreibt in setting, daher temporär
		this.setting = new Setting(Integer.toString(hashCode()), this.setting);	//endgültig
		instances.put(this.hashCode(), this);
		listener.deviceAdded(this);
	}
*/

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
