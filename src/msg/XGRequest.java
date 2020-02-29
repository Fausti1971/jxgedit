package msg;

public interface XGRequest extends XGMessage
{
/**
 * überprüft, ob die übergebene Message eine Antwort auf diesen XGRequest ist, setzt und returniert das Ergebnis;
 * @param msg
 * @return true, wenn dieser XGRequest mit der übergebenen XGResponse beantwortet ist;
 */
	boolean setResponsedBy(XGResponse msg);

/**
 * wurde dieser XGRequest schon beantwortet?
 * @return true, falls ja;
 */
	boolean isResponsed();

/**
 * returniert den Prototyp einer erwarteten Antwort (MessageID, SysexID und XGAddress) auf diesen XGRequest;
 * @return s.o.
 */
	XGResponse getResponse();
}
