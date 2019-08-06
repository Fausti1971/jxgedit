package msg;

import application.MU80;

public interface XGRequest
{	boolean setResponsedBy(XGMessage msg);

	boolean isResponsed();

	default void request()
	{	MU80.device.getMidi().request(this);}
}
