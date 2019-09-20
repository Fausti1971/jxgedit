package msg;

import device.TimeoutException;

public interface XGRequest extends XGMessage
{	
	boolean setResponsedBy(XGResponse msg);
	boolean isResponsed();
	XGResponse getResponse();

	public default XGResponse request() throws TimeoutException
	{	this.getDestination().pull(this);//and wait for respose
		return null;
	}}
