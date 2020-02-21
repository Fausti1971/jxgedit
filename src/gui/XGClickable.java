package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface XGClickable extends MouseListener
{
	@Override public default void mouseClicked(MouseEvent e)
	{	System.out.println("mouse clicked: " + e.getSource());
	}

	@Override public default void mousePressed(MouseEvent e)
	{	System.out.println("mouse pressed: " + e.getSource());
	}
	
	@Override public default void mouseReleased(MouseEvent e)
	{	System.out.println("mouse released: " + e.getSource());
	}

	@Override public default void mouseEntered(MouseEvent e)
	{	System.out.println("mouse entered: " + e.getSource());
	}

	@Override public default void mouseExited(MouseEvent e)
	{	System.out.println("mouse exited: " + e.getSource());
	}
}
