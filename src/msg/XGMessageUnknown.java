package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class XGMessageUnknown extends XGSuperMessage implements XGResponse
{
	protected XGMessageUnknown(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, dest, msg);
	}

	protected XGMessageUnknown(XGMessenger src, XGMessenger dest, byte[] msg, boolean init) throws InvalidMidiDataException
	{	super(src, dest, msg, init);
	}

	@Override public int getHi()
	{	return 0;
	}

	@Override public int getMid()
	{	return 0;
	}

	@Override public int getLo()
	{	return 0;
	}

	@Override public void setHi(int hi)
	{
	}

	@Override public void setMid(int mid)
	{
	}

	@Override public void setLo(int lo)
	{
	}

	@Override public void setMessageID()
	{	encodeMSN(MSG_OFFS, 0);
	}

	public void storeMessage()
	{	log.info("unknown message received: " + getMessageID());
	}

	@Override public int getBulkSize()
	{	return 1;
	}

	@Override public void setBulkSize(int size)
	{
	}

	@Override public int getBaseOffset()
	{	return 0;
	}

	@Override public void checkSum() throws InvalidMidiDataException
	{
	}

	@Override public int setChecksum()
	{	return 0;
	}
}
