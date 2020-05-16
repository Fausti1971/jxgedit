package msg;

import adress.InvalidXGAddressException;
import device.TimeoutException;

public interface XGRequest extends XGMessage
{
/**
 * überprüft, ob die übergebene Message eine Antwort auf diesen XGRequest ist, setzt und returniert das Ergebnis;
 * @param msg
 * @return true, wenn dieser XGRequest mit der übergebenen XGResponse beantwortet ist;
 */
	public default boolean setResponsed(XGResponse msg)
	{	this.setResponsed(this.getResponse().isEqual(msg));
		if(this.isResponsed()) this.setResponse(msg);
		return this.isResponsed();
	}

	public default XGResponse request() throws InvalidXGAddressException, TimeoutException
	{	return this.getDestination().request(this);
	}

/**
 * wurde dieser XGRequest schon beantwortet?
 * @return true, falls ja;
 */
	boolean isResponsed();

/**
 * setzt den Responsed-Status eines Requests auf s
 * @param s neuer Status
 */
	void setResponsed(boolean s);

/**
 * returniert den Prototyp einer erwarteten Antwort (MessageID, SysexID und XGAddress) auf diesen XGRequest;
 * @return s.o.
 */
	XGResponse getResponse();

	void setResponse(XGMessage m);
}
