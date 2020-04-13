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

	public XGBulkDump(XGModule mod, XMLNode n)
	{	this.module = mod;
		this.address = new XGAddress(mod.getAddress(), n);
		log.info("bulk initialized: " + this);
	}

	public XGModule getModule()
	{	return this.module;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String toString()
	{	return this.getModule().toString();
	}
}