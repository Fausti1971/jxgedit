package msg;

import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import value.XGValue;

public class XGMessageParameterRequest extends XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

/*******************************************************************************************/

	public XGMessageParameterRequest(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
//		if(init) this.response = new XGMessageParameterChange(src, array.clone(), true);
	}

//	public XGMessageParameterRequest(XGMessenger src, XGMessenger dest, SysexMessage msg) throws InvalidMidiDataException, InvalidXGAddressException, TimeoutException
//	{	super(src, dest, msg);
//		this.getSource().submit(this.getDestination().request(this));
//	}

	public XGMessageParameterRequest(XGMessenger src, XGValue val) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, new byte[8], true);
		XGAddress adr = val.getAddress();
		this.setMessageID(MSG);
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
		this.setEOX();
	}
/*
	public XGMessageParameterRequest(XGMessenger src, XGMessenger dest, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, dest, new byte[8], true);
		this.setMessageID(MSG);
		this.setHi(adr.getHi().getValue());
		this.setMid(adr.getMid().getValue());
		this.setLo(adr.getLo().getValue());
		this.setEOX(7);
		this.response = new XGMessageParameterChange(dest, src, new XGValue(src, null, adr));
	}
*/
	@Override public int getHi(){ return decodeLSB(HI_OFFS);}

	@Override public int getMid(){ return decodeLSB(MID_OFFS);}

	@Override public int getLo(){ return decodeLSB(LO_OFFS);}

	@Override public void setHi(int hi){ encodeLSB(HI_OFFS, hi);}

	@Override public void setMid(int mid){ encodeLSB(MID_OFFS, mid);}

	@Override public void setLo(int lo){ encodeLSB(LO_OFFS, lo);}

	@Override public void setMessageID(){ this.setMessageID(MSG);}
}
