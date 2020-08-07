package msg;

import javax.sound.midi.InvalidMidiDataException;

public class XGMessageNew extends XGSuperMessage
{
	protected XGMessageNew(XGMessenger src, XGMessenger dest, byte[] array, boolean init) throws InvalidMidiDataException
	{
		super(src, dest, array, init);
	}

	@Override public void setMessageID()
	{
	}

	@Override public int getHi()
	{
		return 0;
	}

	@Override public int getMid()
	{
		return 0;
	}

	@Override public int getLo()
	{
		return 0;
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
}
