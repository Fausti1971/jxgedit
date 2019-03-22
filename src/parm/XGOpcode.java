package parm;

import msg.Bytes;

public class XGOpcode implements XGOpcodeConstants
{	private static final Bytes.ByteType DEF_BYTE_TYPE = Bytes.ByteType.MIDIBYTE;
	private static final int DEF_BYTECOUNT = 1;
	private static final ValueType DEF_VALUE_TYPE = ValueType.Number;

/*************************************************************************************/

	private final int offset, byteCount;
	private final Bytes.ByteType byteType;
	private final ValueType valueType;

	public XGOpcode(int o)
	{	this(o, DEF_BYTECOUNT, DEF_BYTE_TYPE, DEF_VALUE_TYPE);}

	public XGOpcode(int o, int c, Bytes.ByteType t, ValueType v)
	{	this.offset = o;
		this.byteCount = c;
		this.byteType = t;
		this.valueType = v;
	}

	public int getOffset()
	{	return this.offset;}

	public int getByteCount()
	{	return this.byteCount;}

	public Bytes.ByteType getByteType()
	{	return this.byteType;}

	public ValueType getValueType()
	{	return this.valueType;}
}
