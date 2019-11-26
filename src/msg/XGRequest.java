package msg;

import javax.sound.midi.MidiUnavailableException;
import device.TimeoutException;

public interface XGRequest extends XGMessage
{	
	boolean setResponsedBy(XGResponse msg);
	boolean isResponsed();
	XGResponse getResponse();

	public default XGResponse request() throws TimeoutException, MidiUnavailableException
	{	return this.getDestination().request(this);//and wait for respose
	}
}
