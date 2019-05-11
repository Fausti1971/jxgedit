package parm;

import java.util.logging.Logger;
import msg.Bytes;
import msg.Bytes.ByteType;

public class XGParameterOpcode implements XGParameterConstants
{	private static Logger log =Logger.getAnonymousLogger();

/*****************************************************************************************/

	private final Bytes.ByteType byteType;
	private final ValueType valueType;
	private final int offset, byteCount;

	public XGParameterOpcode(int offs)
	{	this.offset = offs;
		this.byteCount = DEF_BYTECOUNT;
		this.valueType = DEF_VALUE_TYPE;
		this.byteType = DEF_BYTE_TYPE;
	}

	public XGParameterOpcode(int offs, int bCount, String vType, String bType)
	{	this.offset = offs;
		this.byteCount = bCount;
		this.valueType = ValueType.valueOf(vType);
		this.byteType = ByteType.valueOf(bType);
		log.info("parameter-opcode added: " + this.getOffset());
	}

	public int getOffset()
	{	return offset;}

	public int getByteCount()
	{	return byteCount;}

	public ValueType getValueType()
	{	return valueType;}

	public Bytes.ByteType getByteType()
	{	return byteType;}
}
