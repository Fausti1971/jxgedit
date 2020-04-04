package adress;
import java.util.logging.Logger;
import module.XGModule;
import opcode.XGOpcode;
import tag.XGTagableAddressableSet;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGBulkDump implements XGAddressable, XMLNodeConstants
{
	public static final Logger log = Logger.getAnonymousLogger();

	public static final XGAddressableSet<XGBulkDump> init(XGModule mod, XMLNode n)
	{
		XGAddressableSet<XGBulkDump> set = new XGAddressableSet<XGBulkDump>();

		for(XMLNode x : n.getChildNodes())
		{	if(x.getTag().equals(TAG_BULK))
			{	set.add(new XGBulkDump(mod, x));
			}
		}
		log.info(set.size() + " bulks initialized in module " + mod);
		return set;
	}

/******************************************************************************************************************/

	private final XGAddress address;
	private final XGModule module;
	private final XGTagableAddressableSet<XGOpcode> opcodes;

	public XGBulkDump(XGModule mod, XMLNode n)
	{	this.module = mod;
		this.address = new XGAddress(mod.getAddress(), n);
		this.opcodes = XGOpcode.init(this, n);
		log.info("bulk initialized: " + this);
	}

	public XGModule getModule()
	{	return this.module;
	}

	public XGTagableAddressableSet<XGOpcode> getOpcodes()
	{	return this.opcodes;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String toString()
	{	return this.getModule().toString();
	}
}