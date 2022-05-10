package gui;

import java.awt.*;import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

public interface XGComponent extends XGUI, MouseListener
{
	enum XGOrientation{horizontal, vertical};

	default JComponent getJComponent(){	return (JComponent)this;}

	@Override default void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{
System.out.println("doubleclick detected");
		}
	}

	@Override default void mousePressed(MouseEvent e)
	{	ENVIRONMENT.mousePressed = true;
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override default void mouseReleased(MouseEvent e)
	{	ENVIRONMENT.mousePressed = false;
		ENVIRONMENT.dragEvent = e;
	}

	@Override  default void mouseEntered(MouseEvent e)
	{	if(!ENVIRONMENT.mousePressed) this.getJComponent().requestFocusInWindow();
	}

	@Override default void mouseExited(MouseEvent e)
	{
	}
}
