package msg;

import adress.InvalidXGAdressException;

public interface XGResponse extends XGMessage
{
	public void storeMessage() throws InvalidXGAdressException;
}
