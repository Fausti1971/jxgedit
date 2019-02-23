package memory;

import java.util.Arrays;
import parm.XGParameter;

public interface Bytes
{	static enum ByteType{MIDIBYTE, NIBBLE};

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));
	}

	default int getInt(XGParameter p)
	{	switch(p.getByteType())
		{	default:
			case MIDIBYTE:	return decodeMidiBytes(p.offset, p.size);
			case NIBBLE:	return decodeLowerNibbles(p.offset, p.size);
		}
	}

	default int setInt(XGParameter p, int v)
	{	switch(p.getByteType())
		{	default:
			case MIDIBYTE:	return encodeMidiBytes(p.offset, p.size, v);
			case NIBBLE:	return encodeLowerNibbles(p.offset, p.size, v);
		}
	}

	default byte[] getParameterBytes(XGParameter p)
	{	switch(p.getByteType())
		{	default:
			case MIDIBYTE:	return copyByteArray(p.offset, p.size);
			case NIBBLE:	return copyByteArray(p.offset, p.size);
		}
	}

	byte[] getByteArray();

	default int decodeMidiByte(int index)
	{	return getByteArray()[index];
	}

	default int encodeMidiByte(int index, int i)
	{	getByteArray()[index] = (byte)i;
		return decodeMidiByte(index);
	}

	default int decodeMidiBytes(int index, int size)
	{	int temp = 0;
		for(int i = 0; i < size; i++)
		{	temp <<= 7;
			temp |= (this.decodeMidiByte(index + i) & 0x7F);
		}
		return temp;
	}

	default int encodeMidiBytes(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	this.encodeMidiByte(index + size, value);
			size--;
			value >>= 7;
		}
		return decodeMidiBytes(index, size);
	}

	default int decodeLowerNibble(int index)
	{	return getByteArray()[index] & 0xF;
	}

	default void encodeLowerNibble(int index, int value)
	{	encodeMidiByte(index, decodeHigherNibble(index) | (value & 0xF));
	}

	default int decodeHigherNibble(int index)
	{	return getByteArray()[index] & 0xF0;
	}

	default void encodeHigherNibble(int index, int value)
	{	encodeMidiByte(index, decodeLowerNibble(index) | (value & 0XF0));
	}

	default int decodeLowerNibbles(int index, int size)
	{	int res = 0;
		for(int i = 0; i < size; i++)
		{	res |= decodeLowerNibble(index + i);
			res <<= 4;
		}
		return res;
	}

	default int encodeLowerNibbles(int index, int size, int value)
	{	for(int i = 0; i < size; i++)
		{	encodeLowerNibble(index + i, value & 0xF);
			value >>= 4;
		}
		return decodeLowerNibbles(index, size);
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
		Math.min(array.length, to);
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
