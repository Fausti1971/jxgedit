package gui;
/**
 * Qualifiziert das implementierende Objekt als Ursprungskomonente eines XGWindows, sodass auf Änderungen (öffnen, schließen, iconifizieren...) reagiert werden kann (selektieren, checken, deselektieren...)
 * @author thomas
 *
 */
public interface XGWindowSource
{
	public XGWindow getWindow();
	public void setWindow(XGWindow win);
	public void windowOpened();
	public void windowClosed();
}
