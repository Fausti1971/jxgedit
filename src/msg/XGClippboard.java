package msg;
import adress.XGAddressableSet;
import module.XGModuleType;

public class XGClippboard extends XGAddressableSet<XGMessageBulkDump> implements XGMessenger
{
	private XGModuleType type;

	@Override public String toString()
	{	return "Clippboard";
	}

	public void submit(XGMessageParameterChange res) throws XGMessengerException
	{
	}

	public void submit(XGMessageBulkDump res) throws XGMessengerException
	{	XGModuleType t = XGModuleType.getModuleType(res.getAddress());
		if(t == null) throw new XGMessengerException("no matching module found for " + res);
		if(this.type == null || !this.type.equals(t)) this.clear();
		this.type = t;
		this.add(res);
	}

	public void submit(XGMessageParameterRequest req) throws XGMessengerException
	{
	}

	public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{
	}

	public void close()
	{
	}

	@Override public void clear()
	{	super.clear();
		this.type = null;
	}
}
