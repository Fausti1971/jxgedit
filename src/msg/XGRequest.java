package msg;

import midi.XGDevice;

public interface XGRequest extends XGMessageConstants
{	boolean setResponsedBy(XGMessage msg);
	boolean isResponsed();
	XGMessage getResponse();

	default void request()
	{	XGDevice.getDevice().getMidi().request(this);}
}
