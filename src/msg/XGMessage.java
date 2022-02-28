package msg;

import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;import application.XGLoggable;

public interface XGMessage extends XGMessageConstants, XGAddressable, XGLoggable
{
	public static XGMessage newMessage(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException, InvalidXGAddressException
	{	checkMessage(array);
		switch(array[MSG_OFFS] & 0xF0)
		{	case MSG_BD:	return new XGMessageBulkDump(src, dest, array, init);
			case MSG_PC:	return new XGMessageParameterChange(src, dest, array, init);
			case MSG_DR:	return new XGMessageBulkRequest(src, dest, array, init);
			case MSG_PR:	return new XGMessageParameterRequest(src, dest, array, init);
			default:		throw new InvalidMidiDataException("unknown xg message " + application.XGStrings.toHexString(array));
		}
	}

	public static XGMessage newMessage(XGMessenger src, XGMessenger dest, MidiMessage msg) throws InvalidMidiDataException, InvalidXGAddressException
	{	if(msg instanceof SysexMessage) return newMessage(src, dest, msg.getMessage(), false);
		else throw new InvalidMidiDataException("no sysex message: " + application.XGStrings.toHexString(msg.getMessage()));
//		Arrays.toString(msg.getMessage()));
	}

	public static void checkMessage(byte[] array) throws InvalidMidiDataException
	{	if(array[VENDOR_OFFS] == VENDOR && array[MODEL_OFFS] == MODEL) return;
		throw new InvalidMidiDataException("no xg message: " + application.XGStrings.toHexString(array));
	}

/***************************************************************************************************/

	default void setSOX(){ this.encodeLSB(SOX_OFFS, SOX);}

	default void setEOX(int index){ this.encodeLSB(index, EOX);}

	default int getSysexID(){ return this.decodeLSN(SYSEX_OFFS);}

	default void setSysexID(int id){ this.encodeLSN(SYSEX_OFFS, id);}

	default int getMessageID(){ return decodeMSN(MSG_OFFS);}

	default void setMessageID(int id){ this.encodeMSN(MSG_OFFS, id);}

	default int getVendorID(){ return this.decodeLSB(VENDOR_OFFS);}

	default void setVendorID(){ this.encodeLSB(VENDOR_OFFS, VENDOR);}

	default int getModelID(){ return decodeLSB(MODEL_OFFS);}

	default void setModelID(){ this.encodeLSB(MODEL_OFFS, MODEL);}

	default public void setTimeStamp(){ this.setTimeStamp(System.currentTimeMillis());}

	default void setAddress(XGAddress adr) throws InvalidXGAddressException
	{	this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
	}

	@Override public default XGAddress getAddress(){ return new XGAddress(this.getHi(), this.getMid(), this.getLo());}

/**
 * überprüft anhand vendorID und modelID, ob es sich um eine XG-Message handelt und wirft bei Fehlschlag eine Exception
 */
	public default void validate() throws InvalidMidiDataException
	{	if(this.getVendorID() != VENDOR || this.getModelID() != MODEL) throw new InvalidMidiDataException("no xg message: " + this.toHexString());
	}

/**
 * Testet das übergebene Object o auf null, MessageID, sysexID, XGAddress (Destinationvergleich ist sinnfrei, da die empfangene Message defaultmäßig den Puffer als solche hat);
 * @param o
 * @return true, wenn o nicht null, MessageID, SysexID, XGAddress übereinstimmt;
 */
	public default boolean isEqual(XGMessage o)
	{	if(o != null)
		{	if(o.getMessageID() == this.getMessageID() &&
				o.getSysexID() == this.getSysexID() &&
				o.getAddress().equals(this.getAddress())) return true;
		}
		return false;
	}

	public void init();
	public XGMessenger getDestination();
	public void setDestination(XGMessenger dest);
	public XGMessenger getSource();
	public void setEOX();
	public void setMessageID();
	public void setSysexID();
	public long getTimeStamp();
	public void setTimeStamp(long time);
	public int getHi();
	public int getMid();
	public int getLo();
	public void setHi(int hi);
	public void setMid(int mid);
	public void setLo(int lo);
}
