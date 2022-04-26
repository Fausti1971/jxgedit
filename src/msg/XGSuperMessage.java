package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import device.*;

public abstract class XGSuperMessage extends SysexMessage implements XGMessage
{

/****************************************************************************************************************************************/

	private final XGMessenger source;
	private long transmissionTimeStamp;
/**
 * Konstruktor für XGMessages aus ByteArrays
 * @param src Herkunft
 * @param array Daten
 * @param init initialisiert eine neu erzeugte XGMessage mit SOX, SysexID/MessageID, VendorID, ModelID, EOX und TimeStamp
 * @throws InvalidMidiDataException
 */
	protected XGSuperMessage(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(array);
		this.source = src;
		this.setTimeStamp(System.currentTimeMillis());
		if(init) this.init();
		this.validate();
	}

/**
 * initialisiert eine neu erzeugte XGMessage mit SOX, SysexID, VendorID, ModelID und TimeStamp
 */
	@Override public void init()
	{	this.setSOX();
		this.setSysexID();
		this.setMessageID();
		this.setVendorID();
		this.setModelID();
		this.setEOX();
		this.setTimeStamp(System.currentTimeMillis());
	}

	@Override public void setEOX(){	this.setEOX(this.length - 1);}

	@Override public void setSysexID(){	this.setSysexID(XGDevice.device.getSysexID());}

	@Override public byte[] getByteArray(){	return this.data;}

	@Override public XGMessenger getSource(){	return this.source;}

	@Override public long getTimeStamp(){	return this.transmissionTimeStamp;}

	@Override public void setTimeStamp(long time){	this.transmissionTimeStamp = time;}

	@Override public String toString(){	return this.getClass().getSimpleName() + this.getAddress();}
}