package msg;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import module.XGModule;
import parm.XGOpcode;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGBulkDump implements XGAddressable, XMLNodeConstants, XGLoggable
{
	public static final Logger log = Logger.getAnonymousLogger();

	public static XGAddressableSet<XGBulkDump> init(XGModule mod)
	{	XGAddressableSet<XGBulkDump> set = new XGAddressableSet<>();
		XMLNode xml = mod.getConfig();
		for(XMLNode n : xml.getChildNodes(TAG_BULK))
			set.add(new XGBulkDump(mod, n));
		LOG.info(set.size() + " bulks initialized for " + mod.getDevice());
		return set;
	}

/******************************************************************************************************************/

	private final XGAddress address;
	private final XGModule module;
	private final XGAddressableSet<XGOpcode> opcodes = new XGAddressableSet<>();

	public XGBulkDump(XGModule mod, XMLNode n)
	{	this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS));
		this.module = mod;
		for(XMLNode o : n.getChildNodes(TAG_OPCODE))
		{	this.opcodes.add(new XGOpcode(this, o));
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