package gui;

import java.awt.Component;
import java.awt.event.WindowListener;

/**
 * Qualifiziert das implementierende Objekt als Ursprungskomonente eines XGWindows, sodass auf Änderungen (öffnen, schließen, iconifizieren...) reagiert werden kann (selektieren, checken, deselektieren...)
 * @author thomas
 *
 */
public interface XGWindowSource extends WindowListener
{
	public XGWindow getWindow();
	public void setWindow(XGWindow win);
	public Component getWindowContent();
}
