package parm;

import memory.Bytes;

public class Opcode implements XGParameterConstants
{	private static final Bytes.ByteType DEF_BYTE_TYPE = Bytes.ByteType.MIDIBYTE;
	private static final int DEF_BYTECOUNT = 1;

/*************************************************************************************/

	private int value;
	private final int offset, byteCount;
	private final Bytes.ByteType byteType;

	public Opcode(int o)
	{	this(o, DEF_BYTECOUNT, DEF_BYTE_TYPE);
	}

	public Opcode(int o, int c, Bytes.ByteType t)
	{	this.offset = o;
		this.byteCount = c;
		this.byteType = t;
	}

	public int getValue()
	{	return this.value;}

	public boolean setValue(int v)
	{	boolean changed = this.value != v;
		this.value = v;
		return changed;
	}

	public int getOffset()
	{	return this.offset;}

	public int getByteCount()
	{	return this.byteCount;}

	public Bytes.ByteType getByteType()
	{	return this.byteType;}
}
