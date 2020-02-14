package msg;

import javax.sound.midi.MidiUnavailableException;
import device.TimeoutException;
import device.XGDevice;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination und stellt damit die Schnittstelle zu einem Endpunkt der Außenwelt (midi) dar oder ist selbst Endpunkt
 */
public interface XGMessenger
{
	public XGDevice getDevice();

/**
 * @return returniert den Namen des XGMessengers
 */
	public String getMessengerName();

/**
 * verarbeitet (store, file) oder transportiert eine Nachricht (msg) an einen entfernten Endpunkt (midi)
 * @param m zu sendende XGMessage
 * @throws MidiUnavailableException 
 */
	public void transmit(XGMessage m) throws MidiUnavailableException;

/**
 *  erfragt eine Nachricht (msg) von einem entfernten Endpunkt (midi) oder ist selbst Endpunkt (store, file)
 * @throws MidiUnavailableException 
 */
	public XGResponse request(XGRequest msg) throws TimeoutException, MidiUnavailableException;
}