package module;

import adress.XGAddress;import adress.XGAddressable;import parm.XGOpcode;import tag.XGTagableSet;

public class XGBulk implements XGAddressable
{	private final XGAddress address;
	private final XGTagableSet<XGOpcode> opcodes;


	public XGBulk(XGAddress address, XGTagableSet<XGOpcode> opcodes)
	{	this.address=address;
		this.opcodes=opcodes;
	}

	public XGAddress getAddress(){	return this.address;}
}
