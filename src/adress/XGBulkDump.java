package adress;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import module.XGModule;
import msg.XGMessageDumpRequest;
import msg.XGRequest;
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
			XGValue val = new XGValue(mod.getDevice().getValues(), opc, this);
			mod.getDevice().getValues().add(val);
		}
	}

	public XGRequest getRequest()
	{	try
		{	return new XGMessageDumpRequest(this.module.getDevice().getValues(), this.module.getDevice().getMidi(), this.address);
		}
		catch(InvalidXGAddressException|InvalidMidiDataException e)
		{	e.printStackTrace();
			return null;
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