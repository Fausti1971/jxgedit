package msg;

import application.XGLoggable; /**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi, file, mem) dar
 */
public interface XGMessenger
{	String DEF_ERROR = " cant handle ";
/**
 * @return returniert den Namen des XGMessengers
 */
	default String getMessengerName(){	return this.getClass().getSimpleName();}

/**
* fordert den Messenger auf, die übergebene Antwort zu verarbeiten
* @param res Antwort zur Verarbeitung
* @exception XGMessengerException falls der Messenger die Anwort nicht verarbeiten kann oder allgemeine Fehler auftraten
*/
	default void submit(XGResponse res) throws XGMessengerException
	{	XGLoggable.LOG.severe(this + DEF_ERROR + res);
	}

/**
* fordert diesen Messenger zur Beantwortung des übergebenen Requests auf und muss die eventuell eingetroffene Antwort an die erfragende Source (req.getSource()) zurückliefern
* @param req Anfrage zur Beantwortung
* @exception XGMessengerException falls der Messenger nicht imstande ist, den Request zu beantworten oder allgemeine Fehler auftraten
*/
	default void request(XGRequest req) throws XGMessengerException
	{	XGLoggable.LOG.severe(this + DEF_ERROR + req);
	}

	@Override String toString();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	void close();
}