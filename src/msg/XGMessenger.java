package msg;

import adress.InvalidXGAddressException;
import device.XGDevice;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi, file, mem) dar
 */
public interface XGMessenger extends Runnable
{
	public XGDevice getDevice();

/**
 * @return returniert den Namen des XGMessengers
 */
	public String getMessengerName();

/**
 * übergibt dem XGMessenger (file, mem, midi) eine XGMessage zur weiteren Be- oder Verarbeitung
 * im Falle eines XGRequest muss die XGResponse an den XGRequest zurückgesandt werden,
 * es muss also die Source des Requests die Destination der Response werden;
 * @param m XGResponse
 * @throws InvalidXGAddressException
 */
	void submit(XGResponse msg) throws InvalidXGAddressException;

/**
 * übermittelt den übergebenen XGRequest an req.getDestination(), lässt den aufrufenden XGMessenger req.getSource() für max. #timeout warten bzw. weckt ihn im Falle einer empfangenen und validierten XGResponse;
 * @param req
 * @return empfangene Antwort
 * @throws InvalidXGAddressException
 */
	XGResponse request(XGRequest req) throws InvalidXGAddressException;
}