package msg;

import javax.sound.midi.InvalidMidiDataException;

public abstract class XGRequest extends XGSuperMessage
{	XGResponse response;
	boolean responsed;

	protected XGRequest(XGMessenger src,XGMessenger dest,byte[] array,boolean init) throws InvalidMidiDataException
	{	super(src,dest,array,init);
	}

/**
 * überprüft, ob die übergebene Message eine Antwort auf diesen XGRequest ist, setzt und returniert das Ergebnis;
 * @param msg
 * @return true, wenn dieser XGRequest mit der übergebenen XGResponse beantwortet ist;
 */
	public boolean setResponsedBy(XGResponse msg)
	{	this.responsed = msg!=null && this.getResponse().isEqual(msg);
		if(this.responsed)
		{	msg.setDestination(this.getResponse().getDestination());
			this.response = msg;
		}
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
