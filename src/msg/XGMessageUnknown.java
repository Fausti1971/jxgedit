package msg;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class XGMessageUnknown extends XGSuperMessage implements XGResponse
{
	protected XGMessageUnknown(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
	}

	protected XGMessageUnknown(XGMessenger src, byte[] msg)
	{	super(src, msg);
	}

	@Override protected int getHi()
	{	return 0;}

	@Override protected int getMid()
	{	return 0;}

	@Override protected int getLo()
	{	return 0;}

	protected void setHi(int hi)
	{}

	protected void setMid(int mid)
	{}

	protected void setLo(int lo)
	{}

	protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, 0);}

	public void storeMessage()
	{	log.info("unknown message received: " + getMessageID());}
}
