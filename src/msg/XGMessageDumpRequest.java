package msg;

import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGMessageDumpRequest extends XGSuperMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20;

/********************************************************************************/

	private Set<XGResponseListener> responseListeners = new HashSet<>();
	private boolean responsed = false;

	XGResponse response = null;

	public XGMessageDumpRequest(XGMessenger src, byte[] array, boolean init) throws InvalidMidiDataException
	{	super(src, array, init);
	}

	public XGMessageDumpRequest(XGMessenger src, XGAddress adr) throws InvalidXGAddressException, InvalidMidiDataException
	{	super(src, new byte[8], true);
		this.setMessageID();
		this.setHi(adr.getHi());
		this.setMid(adr.getMid());
		this.setLo(adr.getLo());
		this.setEOX(7);
		this.response = new XGMessageBulkDump(src, adr);
		this.response.setDestination(src);
	}

	public XGMessageDumpRequest(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
	}

	@Override public boolean setResponsedBy(XGResponse msg)
	{	if(msg == null ||
			!(msg instanceof XGMessageBulkDump) ||
			msg.getSysexID() != this.response.getSysexID() ||
			!(msg.getAdress().equals(this.response.getAdress())))
				return this.responsed = false;
		this.response = msg;
		this.response.setDestination(this.getSource());
		this.notifyResponseListeners();
		return this.responsed = true;
	}

	@Override public boolean isResponsed()
	{	return this.responsed;
	}

	@Override public XGResponse getResponse()
	{	return this.response;
	}

	@Override public int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);
	}

	@Override public int getMid()
	{return decodeMidiByteToInteger(MID_OFFS);
	}

	@Override public int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);
	}

	@Override public void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);
	}

	@Override public void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);
	}

	@Override public void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);
	}

	@Override public String toString()
	{	return this.getAdress().toString();
	}

	@Override public void setMessageID()
	{	encodeHigherNibbleFromInteger(MSG_OFFS, MSG);
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