package msg;

import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import value.XGValue;

public interface XGResponse extends XGMessage
{
	public default void transmit() throws XGMessageException, MidiUnavailableException
	{	if(this.getDestination() != null) this.getDestination().transmit(this);
		else throw new XGMessageException("message " + this + " has no destination-messenger");
	}

	public XGAdressableSet<XGValue> getValues() throws InvalidXGAdressException;
}
