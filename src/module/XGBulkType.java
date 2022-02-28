package module;

import adress.InvalidXGAddressException;import adress.XGAddress;
import adress.XGAddressable;
import static application.XGLoggable.LOG;import config.XGConfigurable;import parm.XGOpcode;import tag.XGTagable;import tag.XGTagableAddressableSet;import xml.XGProperty;import xml.XMLNode;import static xml.XMLNodeConstants.*;

public class XGBulkType implements XGTagable, XGAddressable, XGConfigurable
{
	public final XMLNode config;
	public final String tag;
	public final XGAddress address;
	public final XGModuleType moduleType;
	private final XGTagableAddressableSet<XGOpcode> opcodes = new XGTagableAddressableSet<>();


	public XGBulkType(XGModuleType mt, XMLNode x)
	{	this.config = x;
		this.tag = x.getStringAttribute(ATTR_ID);
		this.address = new XGAddress(x.getStringAttribute(ATTR_ADDRESS), mt.getAddress());
		this.moduleType = mt;

		for(XMLNode o : x.getChildNodes(TAG_OPCODE))
		{	try
			{	this.opcodes.add(new XGOpcode(mt, this.address, o));
			}
			catch(InvalidXGAddressException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}

	public XGTagableAddressableSet<XGOpcode> getOpcodes(){	return this.opcodes;}

	public String getTag(){	return this.tag;}

	public XGAddress getAddress(){	return this.address;}

	public XMLNode getConfig(){	return this.config;}

	public void propertyChanged(XGProperty p){}
}