package msg;

import device.TimeoutException;

public interface XGRequest extends XGMessage
{	
	boolean setResponsedBy(XGResponse msg);
	boolean isResponsed();
	XGResponse getResponse();

	XGResponse request() throws TimeoutException;
}
