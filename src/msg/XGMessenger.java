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
	default String getMessengerName()
	{	return this.getClass().getSimpleName();
	}

/**
 * übergibt dem XGMessenger (file, module, bulk, value, device, midi) eine XGMessage zur weiteren Be- oder Verarbeitung, im Falle eines Requests auch die übermittlung request.getSource().submit(request.getResponse());
 * @param msg XGMessage
 * @throws InvalidXGAddressException
 */
	default void submit(XGMessage msg) throws InvalidXGAddressException, XGMessengerException
	{	if(msg instanceof XGResponse) this.submit((XGResponse)msg);
		if(msg instanceof XGRequest) this.submit((XGRequest)msg);
	}

	void submit(XGResponse res) throws InvalidXGAddressException, XGMessengerException;

	void submit(XGRequest req) throws InvalidXGAddressException, XGMessengerException;

	@Override String toString();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	public void close();
}