	package msg;

import java.util.Arrays;

public interface XGByteArray
{	byte[] getByteArray();

/**
 * dekodiert das LSB (genauer nur das MidiByte mit 7 Bit) an Offset index und liefert es als Integer zurück
 * @param index Offset
 * @return Ergebnis
 */
	default int decodeLSB(int index)
	{	return (this.getByteArray()[index]) & 0x7F;
	}

/**
 * enkodiert das LSB des übergebenen i in das Array an Offset index 
 * @param index Offset
 * @param i Wert
 */
	default void encodeLSB(int index, int i)
	{	this.getByteArray()[index] = (byte)(i & 0xFF);
	}

/**
 * dekodiert die size LSBs (7Bits) an Offset index zu int
 * @param index	Offset
 * @param size	Anzahl
 * @return	Ergebnis
 */
	default int decodeLSB(int index, int size)
	{	int temp = 0;
		for(int i = 0; i < size; i++)
		{	temp <<= 7;
			temp |= this.decodeLSB(index + i);
		}
		return temp;
	}

/**
 * enkodiert den value an die nächsten size LSBs (7Bits) ab Offset index ins Array
 * @param index	Offset
 * @param size	Anzahl
 * @param value	Wert
 */
	default void encodeLSB(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	this.encodeLSB(index + size, value & 0x7F);
			size--;
			value >>= 7;
		}
	}

/**
 * emkodiert value an das least significant nibble an das Offset index ins Array
 * @param index	Offset
 * @param value	Wert
 */
	default void encodeLSN(int index, int value)
	{	byte v = (byte)(decodeMSN(index) | (value & 0xF));
		this.encodeLSB(index, v);
	}

	default int decodeMSN(int index)
	{	return this.getByteArray()[index] & 0xF0;
	}

	default void encodeMSN(int index, int value)
	{	byte v = (byte)(decodeLSN(index) & 0x0F);
		this.encodeLSB(index, v | (value & 0XF0));
	}

/**
 * dekodiert das LSN (4Bits) an Offset index
 * @param index Offset
 * @return	int
 */
	default int decodeLSN(int index)
	{	return this.getByteArray()[index] & 0xF;
	}

	default int decodeLSN(int index, int size)
	{	int res = 0;
		for(int i = 0; i < size; i++)
		{	res <<= 4;
			res |= this.decodeLSN(index + i);
		}
		return res;
	}

	default void encodeLSN(int index, int size, int value)
	{	size--;
		while(size >= 0)
		{	this.encodeLSN(index + size, value & 0xF);
			size--;
			value >>= 4;
		}
	}

	default byte[] copyByteArray(int index, int size)
	{	int to = Math.min(getByteArray().length, index + size);
		return Arrays.copyOfRange(getByteArray(), index, to);
	}

	default void encodeByteArray(int offset, byte[] array)
	{	int
			max = Math.min(getByteArray().length,
			offset + array.length);
		for(byte b : array)
		{	this.getByteArray()[offset] = b;
			if(offset++ > max) break;
		}
	}

	default int calcChecksum(int from, int to)
	{	byte[] array = this.getByteArray();
		byte sum = 0;
		for(int i = from; i <= to; sum += array[i++]);
		return sum;
	}

	default String getString(int from, int to)
	{	StringBuilder sb = new StringBuilder();
		char c;
		while(true)
		{	c = (char)this.decodeLSB(from);
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
		{	s = s.concat(Integer.toHexString(c & 0xFF).toUpperCase() + ", ");
		}
		return s;
	}
}
