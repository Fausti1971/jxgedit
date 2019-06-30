package midi;

import java.util.Arrays;
import parm.XGParameter;
import parm.XGParameterConstants.ValueType;
import value.WrongXGValueTypeException;
import value.XGValue;

public interface Bytes
{	enum ByteType{MIDIBYTE, NIBBLE}

	byte[] getByteArray();

	default void decodeXGValue(int offset, XGValue v)	//dekodiert und setzt (in v) die Anzahl byteCount byteType/s am/ab offset des byteArray
	{	XGParameter p = v.getParameter();
		if(p == null) return;
		ByteType bt = p.getByteType();
		ValueType vt = p.getValueType();
		int bc = p.getByteCount();
		try
		{	switch(vt)
			{	case TEXT:		return;
				case BITMAP:	return;
				default:
				case NUMBER:
					switch(bt)
					{	default:
						case MIDIBYTE:	v.setValue(decodeMidiBytes(offset, bc));
						case NIBBLE:	v.setValue(decodeLowerNibbles(offset, bc));
					}
			}
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();}
	}

	default void encodeXGValue(int offset, XGValue v)
	{	XGParameter p = v.getParameter();
		if(p == null) return;
		ByteType bt = p.getByteType();
		ValueType vt = p.getValueType();
		int bc = p.getByteCount();
		try
		{	switch(vt)
			{	case TEXT:		return;
				case BITMAP:	return;
				default:
				case NUMBER:
					switch(bt)
					{	default:
						case MIDIBYTE:	encodeMidiBytes(offset, bc, v.getNumberValue()); break;
						case NIBBLE:	encodeLowerNibbles(offset, bc, v.getNumberValue()); break;
					}
			}
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();}
	}

	default int decodeMidiByte(int index)
	{	return (getByteArray()[index]) & 0x7F;}

	default void encodeMidiByte(int index, int i)
	{	getByteArray()[index] = (byte)(i & 0xFF);}

	default int decodeMidiBytes(int index, int size)
	{	int temp = 0;
		for(int i = 0; i < size; i++)
		{	temp <<= 7;
			temp |= this.decodeMidiByte(index + i);
		}
		return temp;
	}

	default void encodeMidiBytes(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	this.encodeMidiByte(index + size, value & 0x7F);
			size--;
			value >>= 7;
		}
	}

	default int decodeLowerNibble(int index)
	{	return getByteArray()[index] & 0xF;}

	default void encodeLowerNibble(int index, int value)
	{	encodeMidiByte(index, decodeHigherNibble(index) | (value & 0xF));}

	default int decodeHigherNibble(int index)
	{	return getByteArray()[index] & 0xF0;}

	default void encodeHigherNibble(int index, int value)
	{	encodeMidiByte(index, decodeLowerNibble(index) | (value & 0XF0));}

	default int decodeLowerNibbles(int index, int size)
	{	int res = 0;
		for(int i = 0; i < size; i++)
		{	res <<= 4;
			res |= decodeLowerNibble(index + i);
		}
		return res;
	}

	default void encodeLowerNibbles(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	encodeLowerNibble(index + size, value & 0xF);
			size--;
			value >>= 4;
		}
	}

	default byte[] copyByteArray(int index, int size)
	{	int to = Math.min(getByteArray().length, index + size);
		return Arrays.copyOfRange(getByteArray(), index, to);
	}

	default void encodeByteArray(int offset, byte[] array)
	{	int max = Math.min(getByteArray().length, offset + array.length);
		for(byte b : array)
		{	getByteArray()[offset] = b;
			if(offset++ > max) break;
		}
	}

	default int calcChecksum(int from, int to)
	{	byte[] array = getByteArray();
		byte sum = 0;
		to = Math.min(array.length, to);
		for(int i = from; i <= to; sum += array[i++]);
		return sum;
	}

	default String getString(int from, int to)
	{	StringBuilder sb = new StringBuilder();
		char c;
		while(true)
		{	c = (char)this.decodeMidiByte(from);
			if(c == 0 || from == to) break;
			sb.append(c);
			from++;}
		return(sb.toString());
	}

	public default String toHexString()
	{	if(getByteArray() == null) return "no data";
		String s = new String();
		for(byte c : getByteArray())
		{	s = s.concat(Integer.toHexString(c) + ", ");
		}
		return s;
	}
}
