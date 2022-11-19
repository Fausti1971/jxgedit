package msg;
import adress.XGAddress;import adress.XGAddressRange;import adress.XGAddressableSet;import module.XGDrumsetModuleType;import module.XGModuleType;import javax.swing.*;

public class XGClippboard extends XGAddressableSet<XGMessageBulkDump> implements XGMessenger
{

	@Override public String toString(){	return "Clippboard";}

	@Override public void submit(XGMessageBulkDump res){	this.add(res);}

	@Override public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{	if(this.isEmpty()) throw new XGMessengerException("clippboard is empty!");

		if(XGModuleType.getModuleType(req.getAddress()) instanceof XGDrumsetModuleType)
		{	XGMessageBulkDump m = this.getIgnoreHi(req.getAddress());
			if(m != null)
			{	m.setHi(req.getHi());
				m.setMid(req.getMid());
				req.setResponsedBy(m);
				req.getSource().submit(m);
			}
		}
		else
		{	XGMessageBulkDump m = this.getIgnoreMid(req.getAddress());
			if(m != null)
			{	m.setMid(req.getMid());
				req.setResponsedBy(m);
				req.getSource().submit(m);
			}
		}
	}

	private XGMessageBulkDump getIgnoreMid(XGAddress adr)
	{	for(XGMessageBulkDump m : this) if(m.getAddress().getHiValue() == adr.getHiValue() && m.getAddress().getLoValue() == adr.getLoValue()) return m;
		return null;
	}

	private XGMessageBulkDump getIgnoreHi(XGAddress adr)
	{	for(XGMessageBulkDump m : this) if(m.getAddress().getLoValue() == adr.getLoValue()) return m;
		return null;
	}

	public void close(){	LOG.warning("unnötig...");}
}
