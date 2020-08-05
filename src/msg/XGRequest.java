package msg;

import adress.InvalidXGAddressException;
import device.TimeoutException;

public interface XGRequest extends XGMessage
{
	default void request() throws InvalidXGAddressException, TimeoutException
	{	this.getDestination().request(this);
	}
/**
 * überprüft, ob die übergebene Message eine Antwort auf diesen XGRequest ist, setzt und returniert das Ergebnis;
 * @param msg
 * @return true, wenn dieser XGRequest mit der übergebenen XGResponse beantwortet ist;
 */
	default boolean setResponsed(XGResponse msg)
	{	boolean is = msg instanceof XGResponse && this.getResponse().isEqual(msg);
		this.setResponsed(is);
		if(is)
		{	msg.setDestination(this.getResponse().getDestination());
			this.setResponse(msg);
		}
		return is;
	}

	void setResponsed(boolean r);

/**
 * wurde dieser XGRequest schon beantwortet?
 * @return true, falls ja;
 */
	boolean isResponsed();

/**
 * returniert den Prototyp einer erwarteten Antwort (MessageID, SysexID und XGAddress) auf diesen XGRequest;
 * @return s.o.
 */
	XGResponse getResponse();
	void setResponse(XGResponse m);
}
