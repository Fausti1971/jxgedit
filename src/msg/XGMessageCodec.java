package msg;

import value.XGValueType;

public interface XGMessageCodec 
{
	static XGMessageCodec getCodec(XGValueType.ValueDataType type)
	{	switch(type)
		{	case LSB:	return LSB_CODEC;
			case LSN:	return LSN_CODEC;
			case MSB:	return MSB_CODEC;
			case MSN:	return MSN_CODEC;
			default:	return LSB_CODEC;
		}
	}

	XGMessageCodec LSN_CODEC = new XGMessageCodec()
	{	public void encode(XGMessage msg, int offset,int size,int value){	msg.encodeLSN(offset, size, value);}
		public int decode(XGMessage msg, int offset,int size){	return msg.decodeLSN(offset, size);}
	};

	XGMessageCodec LSB_CODEC = new XGMessageCodec()
	{	public void encode(XGMessage msg, int offset,int size,int value){	msg.encodeLSB(offset, size, value);}
		public int decode(XGMessage msg, int offset,int size){	return msg.decodeLSB(offset, size);}
	};

	XGMessageCodec MSN_CODEC = new XGMessageCodec()
	{	public void encode(XGMessage msg, int offset,int size,int value){	msg.encodeMSN(offset, value);}
		public int decode(XGMessage msg, int offset,int size){	return msg.decodeMSN(offset);}
	};

	XGMessageCodec MSB_CODEC = new XGMessageCodec()
	{	public void encode(XGMessage msg, int offset,int size,int value){	msg.encodeMSB(offset, value);}
		public int decode(XGMessage msg, int offset,int size){	return msg.decodeMSB(offset);}
	};

	void encode(XGMessage msg, int offset, int size, int value);
	int decode(XGMessage msg, int offset, int size);
}
