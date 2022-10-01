package msg;

public class XGMessengerException extends Exception
{
	public XGMessengerException(String msg)
	{	super(msg);
	}

/**
* eine Messenger-Can't-Handle-Exception
* @param messenger der die eigehende nachricht nicht verarbeiten kann
* @param message welche vom angegebenen messenger nicht verarbeitet werden kann
*/
	public XGMessengerException(XGMessenger messenger, XGMessage message)
	{	this(messenger + " can't handle " + message);
	}
}
