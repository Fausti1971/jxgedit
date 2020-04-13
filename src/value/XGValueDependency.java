package value;

import opcode.XGOpcodeConstants;

public class XGValueDependency
{

/****************************************************************************************************/

	private final XGValue master, slave;
	private final XGOpcodeConstants.ValueDataType type;

	public XGValueDependency(XGValue master, XGValue slave, XGOpcodeConstants.ValueDataType type)
	{	this.master = master;
		this.slave = slave;
		this.type = type;
		this.master.addListener(this.slave);
	}

	public int getValue()
	{	int v = this.master.getContent();
		switch(this.type)
		{
			case MSB:	v >>= 7;
			case LSB:	return v & 0x7F;
			case MSN:	v >>= 4;
			case LSN:	return v & 0xF;
			default:	return v;
		}
	}
}
