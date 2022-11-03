package msg;
import adress.XGAddressableSet;import java.util.HashSet;import java.util.Set;

public class XGClippboard extends XGAddressableSet<XGMessageBulkDump> implements XGMessenger
{

	@Override public String toString(){	return "Clippboard";}

	public void submit(XGMessageParameterChange res) throws XGMessengerException
	{	LOG.warning("später...");
	}

	public void submit(XGMessageBulkDump res) throws XGMessengerException
	{	this.add(res);
	}

	public void submit(XGMessageParameterRequest req) throws XGMessengerException
	{	LOG.warning("später...");
	}

	public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{	LOG.warning("später...");
	}

	public void close(){	LOG.warning("unnötig...");}
}
