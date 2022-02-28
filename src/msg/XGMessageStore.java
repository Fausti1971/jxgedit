package msg;

import adress.InvalidXGAddressException;import adress.XGAddressableSet;

public class XGMessageStore extends XGAddressableSet<XGMessageBulkDump> implements XGMessenger
{
	public static final XGMessageStore STORE = new XGMessageStore();

/******************************************************************************************************/

	public void submit(XGMessage msg) throws InvalidXGAddressException, XGMessengerException
	{	if(msg instanceof XGMessageBulkDump) this.add((XGMessageBulkDump)msg);
	}

	public void request(XGRequest req) throws InvalidXGAddressException, XGMessengerException
	{	if(req instanceof XGMessageBulkRequest)
			req.setResponsedBy(this.get(req.getAddress()));
	}

	public void close()
	{
	}
}
