package adress;
import java.util.logging.Logger;
import module.XGModule;
import parm.XGOpcode;
import value.XGValue;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGBulkDump implements XGAddressable, XMLNodeConstants
{
	public static final Logger log = Logger.getAnonymousLogger();

	public static XGAddressableSet<XGBulkDump> init(XGModule mod, XMLNode m)
	{	XGAddressableSet<XGBulkDump> set = new XGAddressableSet<>();
		for(XMLNode b : m.getChildNodes(TAG_BULK))
		try
		{	set.add(new XGBulkDump(mod, b));
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
		return set;
	}

/******************************************************************************************************************/

	private final XGAddress address;
	private final XGModule module;

	public XGBulkDump(XGModule mod, XMLNode n) throws InvalidXGAddressException
	{	this.module = mod;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress()).complement(mod.getAddress());
		for(XMLNode o : n.getChildNodes(TAG_OPCODE))
		{	XGOpcode opc = new XGOpcode(mod.getDevice(), this, o);
			XGValue val = new XGValue(mod.getDevice(), opc, this);
			mod.getDevice().getValues().add(val);
		}
	}

	public XGModule getModule()
	{	return this.module;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String toString()
	{	return this.getAddress().toString();
	}
}