package msg;

import adress.InvalidXGAddressException;
import device.XGDevice;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi, file, mem) dar
 */
public interface XGMessenger
{
/**
 * @return returniert den Namen des XGMessengers
 */
	public default String getMessengerName()
	{	return this.getClass().getSimpleName();
	}

/**
 * übergibt dem XGMessenger (file, mem, midi) eine XGMessage zur weiteren Be- oder Verarbeitung
 * @param msg XGResponse
 * @throws InvalidXGAddressException
 */
	void submit(XGMessage msg) throws InvalidXGAddressException, XGMessengerException;

/**
 * übermittelt den übergebenen XGRequest an req.getDestination() und übermittelt die Response an die Source des Requests
 * @param req
 * @throws InvalidXGAddressException 
 */
	void request(XGRequest req) throws InvalidXGAddressException, XGMessengerException;

	@Override String toString();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	public void close();
}