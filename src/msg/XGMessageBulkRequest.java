package msg;

import javax.sound.midi.InvalidMidiDataException;
import adress.XGAddress;
import bulk.XGBulk;

public class XGMessageBulkRequest extends XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20, OVERHAED = 8;

/********************************************************************************/

	public XGMessageBulkRequest(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
		XGAddress adr = new XGAddress(array[HI_OFFS], array[MID_OFFS], array[LO_OFFS]);
		this.setAddress(adr);
	}

	public XGMessageBulkRequest(XGMessenger src, XGAddress adr) throws InvalidMidiDataException
	{	this(src, new byte[OVERHAED], true);
		this.setAddress(adr);
	}

	public XGMessageBulkRequest(XGMessenger src, XGBulk blk) throws InvalidMidiDataException
	{	this(src, blk.getAddress());
	}

	@Override public int getHi(){ return decodeLSB(HI_OFFS);}

	@Override public int getMid(){ return decodeLSB(MID_OFFS);}

	@Override public int getLo(){ return decodeLSB(LO_OFFS);}

	@Override public void setHi(int hi){ encodeLSB(HI_OFFS, hi);}

	@Override public void setMid(int mid){ encodeLSB(MID_OFFS, mid);}

	@Override public void setLo(int lo){ encodeLSB(LO_OFFS, lo);}

	@Override public void setMessageID(){ this.setMessageID(MSG);}
}