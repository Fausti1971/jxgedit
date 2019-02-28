package parm;

import memory.Bytes;

public class XGOpcode implements XGParameterConstants
{	private static final Bytes.ByteType DEF_BYTE_TYPE = Bytes.ByteType.MIDIBYTE;
	private static final int DEF_BYTECOUNT = 1;

/*************************************************************************************/

	private final int offset, byteCount;
	private final Bytes.ByteType byteType;

	public XGOpcode(int o)
	{	this(o, DEF_BYTECOUNT, DEF_BYTE_TYPE);}

	public XGOpcode(int o, int c, Bytes.ByteType t)
	{	this.offset = o;
		this.byteCount = c;
		this.byteType = t;
	}

	public int getOffset()
	{	return this.offset;}

	public int getByteCount()
	{	return this.byteCount;}

	public Bytes.ByteType getByteType()
	{	return this.byteType;}
}
