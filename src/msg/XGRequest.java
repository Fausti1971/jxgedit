package msg;

import javax.sound.midi.InvalidMidiDataException;

public abstract class XGRequest extends XGSuperMessage
{	XGResponse response = null;
	boolean responsed = false;

	protected XGRequest(XGMessenger src, byte[] array,boolean init) throws InvalidMidiDataException
	{	super(src,array,init);
	}

/**
 * überprüft, ob die übergebene Message eine Antwort auf diesen XGRequest ist, setzt und returniert das Ergebnis;
 * @param res vermeintliche Antwort
 * @return true, wenn dieser XGRequest mit der übergebenen XGResponse beantwortet ist;
 */
	public boolean setResponsedBy(XGResponse res)
	{	this.responsed = res != null && this.getAddress().equals(res.getAddress()) && this.getSysexID() == res.getSysexID();
		if(this instanceof XGMessageBulkRequest) this.responsed &= res instanceof XGMessageBulkDump;
		if(this instanceof XGMessageParameterRequest) this.responsed &= res instanceof XGMessageParameterChange;
		if(this.responsed) this.response = res;
		return this.responsed;
	}

/**
 * wurde dieser XGRequest schon beantwortet?
 * @return true, falls ja;
 */
	public boolean isResponsed(){	return this.responsed;}

/**
 * returniert den Prototyp einer auf diesen XGRequest erwarteten oder erfolgten XGResponse (MessageID, SysexID und XGAddress);
 * @return s.o.
 */
	public XGResponse getResponse(){	return this.response;}
}
