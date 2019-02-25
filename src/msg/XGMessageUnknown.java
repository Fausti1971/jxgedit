package msg;

import javax.sound.midi.SysexMessage;

public class XGMessageUnknown extends XGMessage
{
	protected XGMessageUnknown(SysexMessage msg)
	{	super(msg);}

	protected XGMessageUnknown(byte[] msg)
	{	super(msg);}

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
	{	encodeHigherNibble(MSG_OFFS, 0);}

	public void processXGMessage()
	{	log.info("unknown message received: " + getMessageId());}
}
