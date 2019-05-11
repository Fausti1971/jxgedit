package msg;

import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import application.InvalidXGAdressException;
import application.MU80;
import obj.XGAdress;

public abstract class XGMessage implements XGMessageConstants, Bytes
{	protected static final Logger log = Logger.getAnonymousLogger();

	public static XGMessage factory(MidiMessage msg) throws InvalidMidiDataException
	{	if(!(msg instanceof SysexMessage)) throw new InvalidMidiDataException("no sysex message");
		XGMessage x = new XGMessageUnknown((SysexMessage)msg);
		if(!(x.getVendorId() == VENDOR && x.getModelId() == MODEL)) throw new InvalidMidiDataException("no xg data");
		switch(x.getMessageId())
		{	case BD:	return new XGMessageBulkDump((SysexMessage)msg);
			case PC:	return new XGMessageParameterChange((SysexMessage)msg);
			case DR:	return new XGMessageDumpRequest((SysexMessage)msg);
			case PR:	return new XGMessageParameterRequest((SysexMessage)msg);
		}
		return x;
	}

/****************************************************************************************************************************************/

	private MidiDevice output;				//nur für XGDeviceDetector von Relevanz
	private byte[] data;

	public XGMessage(byte[] array)	// für manuell erzeugte
	{	this.data = array;
		setSOX();
		setSysexId(MU80.device.getSysexId());
		setVendorId();
		setModelId();
	}

	public XGMessage(SysexMessage msg)	//für Midi und File
	{	this.data = msg.getMessage();}

	public void setOutput(MidiDevice out)
	{	this.output = out;}
	
	public MidiDevice getOutput()
	{	return this.output;}

	public byte[] getByteArray()
	{	return this.data;}

	public SysexMessage asSysexMessage() throws InvalidMidiDataException
	{	return new SysexMessage(this.data, this.data.length);}

	public XGAdress getAdress()
	{	return new XGAdress(getHi(), getMid(), getLo());}

	protected abstract int getHi();
	protected abstract int getMid();
	protected abstract int getLo();
	protected abstract void setHi(int hi);
	protected abstract void setMid(int mid);
	protected abstract void setLo(int lo);
	protected abstract void setMessageID();
	public abstract void processXGMessage() throws InvalidXGAdressException;
}