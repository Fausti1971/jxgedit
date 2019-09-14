package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdressable;
//TODO zum Annehmen und Versenden von Messages bedarf es immer eines XGMessenger; folglich alle diesbezüglichen Methoden entfernen;
//Gegenentwurf: sobald der DestinationMessenger gesetzt ist, kann die Message autark versendet werden (msg.getDestination().take(msg);

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

	public XGMessenger getDestination();
	public void setDestination(XGMessenger dest);
	public XGMessenger getSource();
	public long getTimeStamp();
	public void setTimeStamp(long currentTimeMillis);

	default public void setTimeStamp()
	{	this.setTimeStamp(System.currentTimeMillis());
	}

	public SysexMessage asSysexMessage() throws InvalidMidiDataException;
	void validate() throws InvalidMidiDataException;
	public void transmit();

}
