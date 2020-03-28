	package msg;

import java.util.Arrays;

public interface XGByteArray
{	byte[] getByteArray();

	default int decodeMidiByteToInteger(int index)
	{	return (this.getByteArray()[index]) & 0x7F;
	}

	default void encodeMidiByteFromInteger(int index, int i)
	{	this.getByteArray()[index] = (byte)(i & 0xFF);
	}

	default int decodeMidiBytesToInteger(int index, int size)
	{	int temp = 0;
		for(int i = 0; i < size; i++)
		{	temp <<= 7;
			temp |= this.decodeMidiByteToInteger(index + i);
		}
		return temp;
	}

	default void encodeMidiBytesFromInteger(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	this.encodeMidiByteFromInteger(index + size, value & 0x7F);
			size--;
			value >>= 7;
		}
	}

	default int decodeLowerNibble(int index)
	{	return getByteArray()[index] & 0xF;}

	default void encodeLowerNibble(int index, int value)
	{	encodeMidiByteFromInteger(index, decodeHigherNibbleToInteger(index) | (value & 0xF));}

	default int decodeHigherNibbleToInteger(int index)
	{	return getByteArray()[index] & 0xF0;}

	default void encodeHigherNibbleFromInteger(int index, int value)
	{	encodeMidiByteFromInteger(index, decodeLowerNibble(index) | (value & 0XF0));}

	default int decodeLowerNibbles(int index, int size)
	{	int res = 0;
		for(int i = 0; i < size; i++)
		{	res <<= 4;
			res |= decodeLowerNibble(index + i);
		}
		return res;
	}

	default void encodeLowerNibblesFromInteger(int index, int size, int value)
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
		{	c = (char)this.decodeMidiByteToInteger(from);
			if(c == 0 || from == to) break;
			sb.append(c);
			from++;}
		return(sb.toString());
	}

	default void setString(int offset, int bc, String s)//TODO
	{
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
