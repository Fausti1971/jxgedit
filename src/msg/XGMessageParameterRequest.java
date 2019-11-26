package msg;

import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import value.XGIntegerValue;

public class XGMessageParameterRequest extends XGSuperMessage implements XGRequest
{	private static final int MSG = 0x30, HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6;

/*******************************************************************************************/

	private Set<XGResponseListener> responseListeners = new HashSet<>();
	private XGResponse response;
	private boolean responsed;

	protected XGMessageParameterRequest(XGMessenger src, byte[] array, long time)
	{	super(src, array);
	}

	public XGMessageParameterRequest(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
	}

	public XGMessageParameterRequest(XGMessenger src, XGAdress adr) throws InvalidXGAdressException
	{	super(src, new byte[8]);
		setMessageID(MSG);
		setSysexID(src.getDevice().getSysexID());
		setHi(adr.getHi());
		setMid(adr.getMid());
		setLo(adr.getLo());
		setEOX(7);
		this.response = new XGMessageParameterChange(src, new XGIntegerValue(src, adr));
		this.response.setDestination(src);
	}

	protected int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);}

	protected int getMid()
	{	return decodeMidiByteToInteger(MID_OFFS);}

	protected int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);}

	protected void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);}

	protected void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);}

	protected void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);}

	public boolean setResponsedBy(XGResponse msg)
	{	if(msg == null ||
			!(msg instanceof XGMessageParameterChange) ||
			msg.getSysexID() != response.getSysexID() ||
			!msg.getAdress().equals(response.getAdress())) return this.responsed = false;
		this.response = msg;
		this.response.setDestination(this.getSource());
		return this.responsed = true;
	}

	protected void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
	}

	public boolean isResponsed()
	{	return this.responsed;
	}

	public XGResponse getResponse()
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
