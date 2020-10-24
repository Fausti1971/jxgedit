package gui;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComponent;

/**
 * Qualifiziert das implementierende Objekt als Ursprungskomonente eines XGWindows, sodass auf Änderungen (öffnen, schließen, iconifizieren...) reagiert werden kann (selektieren, checken, deselektieren...)
 * @author thomas
 *
 */
public interface XGWindowSource extends WindowListener
{
	public XGWindow getChildWindow();
	public void setChildWindow(XGWindow win);
	public JComponent getChildWindowContent();
	public Point getSourceLocationOnScreen();

	@Override public default void windowOpened(WindowEvent e)
	{	this.setChildWindow((XGWindow)e.getWindow());
	}

	@Override public default void windowClosing(WindowEvent e)
	{
	}

	@Override public default void windowClosed(WindowEvent e)
	{	this.setChildWindow(null);
	}

	@Override public default void windowIconified(WindowEvent e)
	{
	}

	@Override public default void windowDeiconified(WindowEvent e)
	{
	}

	@Override public default void windowActivated(WindowEvent e)
	{
	}

	@Override public default void windowDeactivated(WindowEvent e)
	{
	}
}
