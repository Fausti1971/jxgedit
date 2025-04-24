package msg;

import javax.sound.midi.InvalidMidiDataException;
import adress.XGInvalidAddressException;
import value.XGValue;

public class XGMessageParameterRequest extends XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

/*******************************************************************************************/

	public XGMessageParameterRequest(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
	}

	public XGMessageParameterRequest(XGMessenger src, XGValue val) throws  InvalidMidiDataException
	{	super(src, new byte[8], true);
		this.setMessageID(MSG);
		this.setAddress(val.getAddress());
		this.setEOX();
	}

	@Override public int getHi(){ return decodeLSB(HI_OFFS);}

	@Override public int getMid(){ return decodeLSB(MID_OFFS);}

	@Override public int getLo(){ return decodeLSB(LO_OFFS);}

	@Override public void setHi(int hi){ encodeLSB(HI_OFFS, hi);}

	@Override public void setMid(int mid){ encodeLSB(MID_OFFS, mid);}

	@Override public void setLo(int lo){ encodeLSB(LO_OFFS, lo);}

	@Override public void setMessageID(){ this.setMessageID(MSG);}
}
