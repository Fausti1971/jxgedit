package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import adress.XGAddress;import adress.XGAddressable;
import application.XGLoggable;import application.XGStrings;

public interface XGMessage extends XGMessageConstants, XGAddressable, XGLoggable
{
	static XGMessage newMessage(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	checkMessage(array);
		switch(array[MSG_OFFS] & 0xF0)
		{	case MSG_BD:	return new XGMessageBulkDump(src, array, init);
			case MSG_PC:	return new XGMessageParameterChange(src, array, init);
			case MSG_DR:	return new XGMessageBulkRequest(src, array, init);
			case MSG_PR:	return new XGMessageParameterRequest(src, array, init);
			default:		throw new InvalidMidiDataException("unknown xg message: " + XGStrings.toHexString(array));
		}
	}

	static XGMessage newMessage(XGMessenger src, MidiMessage msg) throws InvalidMidiDataException
	{	if(msg instanceof SysexMessage) return newMessage(src, msg.getMessage(), false);
		else throw new InvalidMidiDataException("no sysex message: " + XGStrings.toHexString(msg.getMessage()));
	}

	static void checkMessage(byte[] array) throws InvalidMidiDataException
	{	if(array[VENDOR_OFFS] == VENDOR && array[MODEL_OFFS] == MODEL) return;
		throw new InvalidMidiDataException("no xg message: " + XGStrings.toHexString(array));
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

	default void setTimeStamp(){ this.setTimeStamp(System.currentTimeMillis());}

	default void setAddress(XGAddress adr)
	{	this.setHi(adr.getHiValue());
		this.setMid(adr.getMidValue());
		this.setLo(adr.getLoValue());
	}

	@Override default XGAddress getAddress(){	return new XGAddress(this.getHi(), this.getMid(), this.getLo());}


/**
 * überprüft anhand vendorID und modelID, ob es sich um eine XG-Message handelt und wirft bei Fehlschlag eine Exception
 */
	default void validate() throws InvalidMidiDataException
	{	if(this.getVendorID() != VENDOR || this.getModelID() != MODEL) throw new InvalidMidiDataException("no xg message: " + this.toHexString());
	}

/**
 * Testet das übergebene Object o auf null, MessageID, sysexID, XGAddress (Destinationvergleich ist sinnfrei, da die empfangene Message defaultmäßig den Puffer als solche hat);
 * @param o
 * @return true, wenn o nicht null, MessageID, SysexID, XGAddress übereinstimmt;
 */
	default boolean isEqual(XGMessage o)
	{	if(o != null)
		{	return
				o.getMessageID() == this.getMessageID() &&
				o.getSysexID() == this.getSysexID() &&
				o.getAddress().equals(this.getAddress());
		}
		return false;
	}

	void init();
	XGMessenger getSource();
	void setEOX();
	void setMessageID();
	void setSysexID();
	long getTimeStamp();
	void setTimeStamp(long time);
	int getHi();
	int getMid();
	int getLo();
	void setHi(int hi);
	void setMid(int mid);
	void setLo(int lo);
}
