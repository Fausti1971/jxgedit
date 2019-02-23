package midi;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import msg.XGMessage;
import msg.XGMessageBulkDump;
import msg.XGMessageDumpRequest;
import obj.XGAdress;

public class XGDeviceDetector implements Receiver
{	private static Logger log = Logger.getAnonymousLogger();
	private static MidiDevice oDev = null;
	private static Receiver trans = null;
	private static XGMessageDumpRequest request = null;
	private static Set<XGDeviceDetector> instances = new HashSet<>();
	private static int timer = 100;

	private static void openAllInputs()
	{	for(MidiDevice d : Midi.getInputs())
		{	new XGDeviceDetector(d);
		}
	}

	public static Set<XGDeviceDetector> detectXGDevices()		//TODO eine response auf den ersten request wird bei einem timeout bis etwa 200 chronisch ignoriert (?)
	{	openAllInputs();
		for(MidiDevice d : Midi.getOutputs())
		{	oDev = d;
			try
			{	oDev.open();
				trans = oDev.getReceiver();
			}
			catch (MidiUnavailableException e)
			{	e.printStackTrace();
			}
			for(int i = 0; i < 16; i++)
			{	request = new XGMessageDumpRequest(new XGAdress(1, 0, 0));	// immer neuen request instanziieren, damit die dabei automatisch erstellte response die richtige sysexID hat
				request.setSysexId(i);
				request.getResponse().setSysexId(i);
				request.setOutput(oDev);
				try
				{	trans.send(request.asSysexMessage(), -1L);
				}
				catch (InvalidMidiDataException e)
				{	log.severe(e.getMessage());
				}
				try
				{	Thread.sleep(timer);
				}
				catch (InterruptedException e)
				{	e.printStackTrace();
				}
			}
			oDev.close();
		}
		return instances;
	}

/****************************************************************************************************************************/

	MidiDevice input = null, output = null;
	int sysexId = 0;
	int timeout = 150;
	String name = "unknown";

	public XGDeviceDetector(MidiDevice d)
	{	this.input = d;
		try
		{	d.getTransmitter().setReceiver(this);
			d.open();
		}
		catch (MidiUnavailableException e)
		{	log.info(e.getMessage());
		}
	}

	@Override public void send(MidiMessage message, long timeStamp)
	{	try
		{	XGMessageBulkDump x = (XGMessageBulkDump) XGMessage.factory(message);
			if(request.isResponsed(x))
			{	this.output = request.getOutput();
				this.sysexId = x.getSysexId();
//				this.name = x.getDump().getString(0, 0xE).trim();
				instances.add(this);
			}
		}
		catch (InvalidMidiDataException e)
		{	log.info(e.getMessage());
		}
	}

	@Override public void close()
	{	if(this.input != null && this.input.isOpen()) this.input.close();
		instances.remove(this);
	}
}
