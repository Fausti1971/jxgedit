package msg;

import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.XGAdress;
import adress.XGAdressable;

abstract class XGSuperMessage implements XGMessage, XGAdressable
{	protected static final Logger log = Logger.getAnonymousLogger();

/****************************************************************************************************************************************/

	private final XGMessenger source;
	private XGMessenger destination;
	private long transmissionTimeStamp;
	private byte[] data;

	protected XGSuperMessage(XGMessenger src, byte[] array)	// für manuell erzeugte
	{	this.source = src;
		this.data = array;
		setSOX();
		setSysexID(src.getSysexID());
		setVendorID();
		setModelID();
		this.setTimeStamp(System.currentTimeMillis());
	}

	protected XGSuperMessage(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException	//für Midi und File
	{	this.source = src;
		this.setTimeStamp(System.currentTimeMillis());
		this.data = msg.getMessage();
		this.validate();
	}

	public byte[] getByteArray()
	{	return this.data;}

	public XGMessenger getSource()
	{	return this.source;
	}

	public XGMessenger getDestination()
	{	return this.destination;
	}

	public void setDestination(XGMessenger dest)
	{	this.destination = dest;
	}
	
	public long getTimeStamp()
	{	return this.transmissionTimeStamp;
	}

	public void setTimeStamp(long time)
	{	this.transmissionTimeStamp = time;
	}

	public void validate() throws InvalidMidiDataException
	{	if(!(this.getVendorID() == VENDOR && this.getModelID() == MODEL)) throw new InvalidMidiDataException("no xg data");
	}

	public SysexMessage asSysexMessage() throws InvalidMidiDataException
	{	return new SysexMessage(this.data, this.data.length);
	}

	public XGAdress getAdress()
	{	return new XGAdress(getHi(), getMid(), getLo());
	}

	public String getInfo()
	{	return this.toString();
	}

	@Override public String toString()
	{	return this.getClass().getSimpleName() + " " + this.getAdress();
	}

	protected abstract int getHi();
	protected abstract int getMid();
	protected abstract int getLo();
	protected abstract void setHi(int hi);
	protected abstract void setMid(int mid);
	protected abstract void setLo(int lo);
	protected abstract void setMessageID();
}