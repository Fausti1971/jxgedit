package msg;

import adress.InvalidXGAddressException;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi, file, mem) dar
 */
public interface XGMessenger
{
/**
 * @return returniert den Namen des XGMessengers
 */
	default String getMessengerName(){	return this.getClass().getSimpleName();}

	void submit(XGResponse res) throws XGMessengerException;

	void submit(XGRequest req) throws XGMessengerException;

	@Override String toString();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	void close();
}