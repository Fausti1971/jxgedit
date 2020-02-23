package msg;

import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.XGAddressable;

abstract class XGSuperMessage extends SysexMessage implements XGMessage, XGAddressable
{	protected static final Logger log = Logger.getAnonymousLogger();

/****************************************************************************************************************************************/

	private final XGMessenger source;
	private XGMessenger destination;
	private long transmissionTimeStamp;
/**
 * Konstruktor für XGMessages aus ByteArrays
 * @param src Herkunft
 * @param array Daten
 * @param init initialisiert eine neu erzeugte XGMessage mit SOX, SysexID, VendorID, ModelID und TimeStamp
 * @throws InvalidMidiDataException
 */
	protected XGSuperMessage(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException	// für manuell erzeugte
	{	super(array);
		this.source = src;
		if(init) this.init();
		this.validate();
	}
/**
 * Konstruktor für über Midi empfangene SysexMessages; keine Initialisierung
 * @param src
 * @param msg
 * @throws InvalidMidiDataException
 */
	protected XGSuperMessage(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException	//für Midi
	{	super(msg.getMessage());
		this.source = src;
		this.setTimeStamp(System.currentTimeMillis());
		this.validate();
	}
/**
 * initialisiert eine neu erzeugte XGMessage mit SOX, SysexID, VendorID, ModelID und TimeStamp
 */
	@Override public void init()
	{	this.setSOX();
		this.setSysexID(this.source.getDevice().getSysexID());
		this.setVendorID();
		this.setModelID();
		this.setTimeStamp(System.currentTimeMillis());
	}

	@Override public byte[] getByteArray()
	{	return this.getMessage();
	}

	@Override public XGMessenger getSource()
	{	return this.source;
	}

	@Override public XGMessenger getDestination()
	{	return this.destination;
	}

	@Override public void setDestination(XGMessenger dest)
	{	this.destination = dest;
	}
	
	@Override public long getTimeStamp()
	{	return this.transmissionTimeStamp;
	}

	@Override public void setTimeStamp(long time)
	{	this.transmissionTimeStamp = time;
	}
/**
 * überprüft anhand vendorID und modelID, ob es sich um eine XG-Message handelt und wirft bei Fehlschlag eine Exception
 */
	@Override public void validate() throws InvalidMidiDataException
	{	if(this.getVendorID() != VENDOR || this.getModelID() != MODEL) throw new InvalidMidiDataException("no xg message");
	}

	@Override public String toString()
	{	return this.getClass().getSimpleName() + "/" + this.source.getDevice().getType(this.getAdress()) + "/" + this.getAdress();
	}

}