package adress;
import java.util.logging.Logger;
import module.XGModule;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGBulkDump implements XGAddressable, XMLNodeConstants
{
	public static final Logger log = Logger.getAnonymousLogger();

/******************************************************************************************************************/

	private final XGAddress address;
	private final XGModule module;

	public XGBulkDump(XGModule mod, XMLNode n) throws InvalidXGAddressException
	{	this(mod, new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress()));
	}

	public XGBulkDump(XGModule mod, XGAddress adr) throws InvalidXGAddressException
	{	this.module = mod;
		this.address = adr.complement(mod.getAddress());
		mod.getBulks().add(this);
		log.info("bulk initialized: " + this);
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