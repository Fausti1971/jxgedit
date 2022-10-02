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

/**
* fordert den Messenger, die übergebene Antwort zu verarbeiten
* @param res Antwort zur Verarbeitung
* @exception XGMessengerException falls der Messenger die Anwort nicht verarbeiten kann oder allgemeine Fehler auftraten
*/
	void submit(XGMessageParameterChange res) throws XGMessengerException;

	void submit(XGMessageBulkDump res) throws XGMessengerException;

/**
* übergibt fordert diesen Messenger zur Beantwortung des übergebenen Requests auf und muss die eventuell eingetroffene Antwort an die erfragende Source (req.getSource()) zurückliefern
* @param req Anfrage zur Beantwortung
* @exception XGMessengerException falls der Messenger nicht imstande ist, den Request zu beantworten oder allgemeine Fehler auftraten
*/
	void submit(XGMessageParameterRequest req) throws XGMessengerException;

	void submit(XGMessageBulkRequest req) throws XGMessengerException;

	@Override String toString();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	void close();
}