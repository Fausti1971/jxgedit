package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdressable;

public interface XGMessage extends XGMessageConstants, XGAdressable
{
	public static XGMessage newMessage(XGMessenger src, MidiMessage msg) throws InvalidMidiDataException, InvalidXGAdressException
	{	if(!(msg instanceof SysexMessage)) throw new InvalidMidiDataException("no sysex message");
		XGMessage x = new XGMessageUnknown(src, (SysexMessage)msg);
		switch(x.getMessageID())
		{	case XGMessageConstants.BD:	return new XGMessageBulkDump(src, (SysexMessage)msg);
			case XGMessageConstants.PC:	return new XGMessageParameterChange(src, (SysexMessage)msg);
			case XGMessageConstants.DR:	return new XGMessageDumpRequest(src, (SysexMessage)msg);
			case XGMessageConstants.PR:	return new XGMessageParameterRequest(src, (SysexMessage)msg);
		}
		return x;
	}

/***************************************************************************************************/

	default void setSOX()
	{	encodeMidiByteFromInteger(SOX_OFFS, SOX);
	}

	default void setEOX(int index)
	{	encodeMidiByteFromInteger(index, EOX);
	}

	default int getSysexID()
	{	return decodeLowerNibble(SYSEX_OFFS);
	}

	default void setSysexID(int id)
	{	encodeLowerNibble(SYSEX_OFFS, id);
	}

	default int getMessageID()
	{	return decodeHigherNibbleToInteger(MSG_OFFS);
	}

	default void setMessageID(int id)
	{	encodeHigherNibbleFromInteger(MSG_OFFS, id);
	}

	default int getVendorID()
	{	return decodeMidiByteToInteger(VENDOR_OFFS);
	}

	default void setVendorID()
	{	encodeMidiByteFromInteger(VENDOR_OFFS, VENDOR);
	}

	default int getModelID()
	{	return decodeMidiByteToInteger(MODEL_OFFS);
	}

	default void setModelID()
	{	encodeMidiByteFromInteger(MODEL_OFFS, MODEL);
	}

	public XGMessenger getDestination();
	public void setDestination(XGMessenger dest);
	public XGMessenger getSource();
	public long getTimeStamp();
	public void setTimeStamp(long time);

	default public void setTimeStamp()
	{	this.setTimeStamp(System.currentTimeMillis());
	}

	public SysexMessage asSysexMessage() throws InvalidMidiDataException;
	void validate() throws InvalidMidiDataException;
}
