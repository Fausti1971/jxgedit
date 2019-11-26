package msg;

import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import adress.InvalidXGAdressException;
import adress.XGAdress;

public class XGMessageDumpRequest extends XGSuperMessage implements XGRequest
{	private static final int HI_OFFS = 4, MID_OFFS = 5, LO_OFFS = 6, MSG = 0x20;

/********************************************************************************/

	private Set<XGResponseListener> responseListeners = new HashSet<>();
	private boolean responsed = false;

	XGResponse response = null;

	public XGMessageDumpRequest(XGMessenger src, byte[] array, long time) throws InvalidMidiDataException
	{	super(src, array);
	}

	public XGMessageDumpRequest(XGMessenger src, XGAdress adr) throws InvalidXGAdressException
	{	super(src, new byte[8]);
		setSysexID(src.getDevice().getSysexID());
		setMessageID();
		encodeMidiByteFromInteger(HI_OFFS, adr.getHi());
		encodeMidiByteFromInteger(MID_OFFS, adr.getMid());
		encodeMidiByteFromInteger(LO_OFFS, adr.getLo());
		setEOX(7);
		this.response = new XGMessageBulkDump(src, adr);
		this.response.setDestination(src);
	}

	public XGMessageDumpRequest(XGMessenger src, SysexMessage msg) throws InvalidMidiDataException
	{	super(src, msg);
	}

	public boolean setResponsedBy(XGResponse msg)
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

	public boolean isResponsed()
	{	return this.responsed;
	}

	public XGResponse getResponse()
	{	return this.response;
	}

	protected int getHi()
	{	return decodeMidiByteToInteger(HI_OFFS);
	}

	protected int getMid()
	{return decodeMidiByteToInteger(MID_OFFS);
	}

	protected int getLo()
	{	return decodeMidiByteToInteger(LO_OFFS);
	}

	protected void setHi(int hi)
	{	encodeMidiByteFromInteger(HI_OFFS, hi);
	}

	protected void setMid(int mid)
	{	encodeMidiByteFromInteger(MID_OFFS, mid);
	}

	protected void setLo(int lo)
	{	encodeMidiByteFromInteger(LO_OFFS, lo);
	}

	@Override public String toString()
	{	return this.getAdress().toString();
	}

	protected void setMessageID()
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