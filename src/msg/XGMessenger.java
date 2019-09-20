package msg;

import device.TimeoutException;
import device.XGDevice;

/**
 * qualifiziert das implementierende Object als XGMessage-Source und -Destination
 */
public interface XGMessenger
{
	public static enum XGMessengerType{Memory, Midi, File}

	public XGDevice getDevice();
/**
 * @return returniert den MessengerType des implementierende Objects
 */
	public XGMessengerType getMessengerType();

/**
 * @return returniert den Namen des XGMessengers
 */
	public String getMessengerName();

/**
 * sendet eine Nachricht (msg) an einen entfernten Empfänger (transmit)
 * @param m zu sendende XGMessage
 */
	public void take(XGMessage m);

/**
 *  erfragt eine Nachricht (msg) von einem entfernten Sender (request)
 */
	public XGResponse pull(XGRequest msg) throws TimeoutException;
}