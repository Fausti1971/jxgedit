package msg;

import adress.InvalidXGAddressException;
import device.TimeoutException;
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
 * übermittelt den übergebenen XGRequest an req.getDestination(), lässt den aufrufenden XGMessenger für max. #timeout warten bzw. weckt ihn im Falle einer
 * empfangenen und validierten XGResponse, die dann mittels getResponse() abgefragt werden kann;
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