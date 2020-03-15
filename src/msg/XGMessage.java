package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;

public interface XGMessage extends XGMessageConstants, XGAddressable
{
	public static XGMessage newMessage(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException, InvalidXGAddressException
	{	XGMessage x = new XGMessageUnknown(src, array, init);
		switch(x.getMessageID())
		{	case MSG_BD:	return new XGMessageBulkDump(src, array, init);
			case MSG_PC:	return new XGMessageParameterChange(src, array, init);
			case MSG_DR:	return new XGMessageDumpRequest(src, array, init);
			case MSG_PR:	return new XGMessageParameterRequest(src, array, init);
		}
		return x;
	}

	public static XGMessage newMessage(XGMessenger src, MidiMessage msg) throws InvalidMidiDataException, InvalidXGAddressException
	{	if(!(msg instanceof SysexMessage)) throw new InvalidMidiDataException("no sysex message");
		return newMessage(src, msg.getMessage(), false);
	}

/***************************************************************************************************/

	default void setSOX()
	{	this.encodeMidiByteFromInteger(SOX_OFFS, SOX);
	}

	default void setEOX(int index)
	{	this.encodeMidiByteFromInteger(index, EOX);
	}

	default int getSysexID()
	{	return this.decodeLowerNibble(SYSEX_OFFS);
	}

	default void setSysexID(int id)
	{	this.encodeLowerNibble(SYSEX_OFFS, id);
	}

	default int getMessageID()
	{	return decodeHigherNibbleToInteger(MSG_OFFS);
	}

	default void setMessageID(int id)
	{	this.encodeHigherNibbleFromInteger(MSG_OFFS, id);
	}

	default int getVendorID()
	{	return this.decodeMidiByteToInteger(VENDOR_OFFS);
	}

	default void setVendorID()
	{	this.encodeMidiByteFromInteger(VENDOR_OFFS, VENDOR);
	}

	default int getModelID()
	{	return decodeMidiByteToInteger(MODEL_OFFS);
	}

	default void setModelID()
	{	this.encodeMidiByteFromInteger(MODEL_OFFS, MODEL);
	}

	default public void setTimeStamp()
	{	this.setTimeStamp(System.currentTimeMillis());
	}

	@Override public default XGAddress getAdress()
	{	return new XGAddress(getHi(), getMid(), getLo());
	}

/**
 * überprüft anhand vendorID und modelID, ob es sich um eine XG-Message handelt und wirft bei Fehlschlag eine Exception
 */
	public default void validate() throws InvalidMidiDataException
	{	if(this.getVendorID() != VENDOR || this.getModelID() != MODEL) throw new InvalidMidiDataException("no xg message");
	}

/**
 * Testet das übergebene Object o auf null, MessageID, sysexID und XGAddress;
 * @param o
 * @return true, wenn o nicht null, MessageID, SysexID und XGAddress übereinstimmt;
 */
	public default boolean isEqual(XGMessage o)
	{	if(o != null)
		{	if(o.getMessageID() == this.getMessageID() &&
				o.getSysexID() == this.getSysexID() &&
				o.getAdress().equals(this.getAdress())) return true;
		}
		return false;
	}

	public void init();
	public XGMessenger getDestination();
	public void setDestination(XGMessenger dest);
	public XGMessenger getSource();
	public long getTimeStamp();
	public void setTimeStamp(long time);
	public int getHi();
	public int getMid();
	public int getLo();
	public void setHi(int hi);
	public void setMid(int mid);
	public void setLo(int lo);
	public void setMessageID();
}
