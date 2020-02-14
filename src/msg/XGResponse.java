package msg;

import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import value.XGValue;

public interface XGResponse extends XGMessage
{
	public default void transmit() throws XGMessageException, MidiUnavailableException
	{	if(this.getDestination() != null) this.getDestination().transmit(this);
		else throw new XGMessageException("message " + this + " has no destination-messenger");
	}

	public XGAddressableSet<XGValue> getValues() throws InvalidXGAddressException;
}
