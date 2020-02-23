package msg;

import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import value.XGIntegerValue;

public class XGMessageParameterRequest extends XGSuperMessage implements XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

/*******************************************************************************************/

	private Set<XGResponseListener> responseListeners = new HashSet<>();
	private XGResponse response;
	private boolean responsed;

	protected XGMessageParameterRequest(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
	}

	public XGMessageParameterRequest(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
	}

	public XGMessageParameterRequest(XGMessenger src, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, new byte[8], true);
		this.setMessageID(MSG);
		this.setHi(adr.getHi());
		this.setMid(adr.getMid());
		this.setLo(adr.getLo());
		this.setEOX(7);
		this.response = new XGMessageParameterChange(src, new XGIntegerValue(src, adr));
		this.response.setDestination(src);
	}

	@Override public int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);}

	@Override public int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);}

	@Override public int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);}

	@Override public void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);}

	@Override public void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);}

	@Override public void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);}

	@Override public boolean setResponsedBy(XGResponse msg)
	{	if(msg == null ||
			!(msg instanceof XGMessageParameterChange) ||
			msg.getSysexID() != response.getSysexID() ||
			!msg.getAdress().equals(response.getAdress())) return this.responsed = false;
		this.response = msg;
		this.response.setDestination(this.getSource());
		return this.responsed = true;
	}

	@Override public void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	@Override public boolean isResponsed()
	{	return this.responsed;
	}

	@Override public XGResponse getResponse()
	{	return this.response;
	}

	public void addResponseListener(XGResponseListener l)
	{	this.responseListeners.add(l);
	}

	public void removeResponseListener(XGResponseListener l)
	{	this.responseListeners.remove(l);
	}

	public void notifyResponseListeners()
	{	for(XGResponseListener l : this.responseListeners) l.requestResponsed();
	}
}
