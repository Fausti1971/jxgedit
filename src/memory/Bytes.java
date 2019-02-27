package memory;

import java.util.Arrays;
import parm.Opcode;

public interface Bytes
{	static enum ByteType{MIDIBYTE, NIBBLE};

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));}

	byte[] getByteArray();

	default void decodeOpcode(int offset, Opcode opc)	//dekodiert die Anzahl opcode.byteCount opcode.byteType/s am/ab offset des byteArray und setzt opcode.value
	{	switch(opc.getByteType())
		{	default:
			case MIDIBYTE:	opc.setValue(decodeMidiBytes(offset, opc.getByteCount())); break;
			case NIBBLE:	opc.setValue(decodeLowerNibbles(offset, opc.getByteCount())); break;
		}
	}

	default void encodeOpcode(int offset, Opcode opc)
	{	switch(opc.getByteType())
		{	default:
			case MIDIBYTE:	encodeMidiBytes(offset, opc.getByteCount(), opc.getValue()); break;
			case NIBBLE:	encodeLowerNibbles(offset, opc.getByteCount(), opc.getValue()); break;
		}
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
		{	res |= decodeLowerNibble(index + i);
			res <<= 4;
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
