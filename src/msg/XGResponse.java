package msg;

import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import opcode.NoSuchOpcodeException;
import value.XGValue;

public interface XGResponse extends XGMessage
{
	public default void transmit() throws XGMessageException
	{	if(this.getDestination() != null) this.getDestination().take(this);
		else throw new XGMessageException("message " + this + " has no destination-messenger");
	}

	public XGAdressableSet<XGValue> getValues() throws InvalidXGAdressException, NoSuchOpcodeException;
}
