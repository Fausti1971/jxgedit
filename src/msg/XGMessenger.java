package msg;

import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import device.TimeoutException;
import device.XGDevice;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi, file, mem) dar
 */
public interface XGMessenger
{
	public XGDevice getDevice();

/**
 * @return returniert den Namen des XGMessengers
 */
	public default String getMessengerName()
	{	return this.getDevice() + " " + this.getClass().getSimpleName();
	}

/**
 * übergibt dem XGMessenger (file, mem, midi) eine XGMessage zur weiteren Be- oder Verarbeitung
 * @param m XGResponse
 * @throws InvalidXGAddressException
 */
	void submit(XGResponse msg) throws InvalidXGAddressException;

/**
 * übermittelt den übergebenen XGRequest an req.getDestination(), lässt den aufrufenden XGMessenger für max. #timeout warten bzw. weckt ihn im Falle einer
 * empfangenen und validierten XGResponse, die dann mittels getResponse() abgefragt werden kann;
 * @param req
 * @throws InvalidXGAddressException
 * @throws TimeoutException 
 */
	void request(XGRequest req) throws InvalidXGAddressException, TimeoutException;

	@Override String toString();

/**
 * erfragt eine JComponent, die zur Konfiguration des Messengers dient
 * @return Root-Komponente
 */
	public JComponent getConfigComponent();

/**
 * schließt den mit dem Konstruktor automatisch geöffneten Messenger und gibt Resourcen wieder frei
 */
	public void close();
}