package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface XGComponent extends XGColorable, XGFrameable, MouseListener
{
	public default void mouseClicked(MouseEvent e)
	{	System.out.println("mouse clicked");
	}
	
	public default void mousePressed(MouseEvent e)
	{	System.out.println("mouse pressed");
	}
	
	public default void mouseReleased(MouseEvent e)
	{	System.out.println("mouse released");
	}
	
	public default void mouseEntered(MouseEvent e)
	{	System.out.println("mouse entered");
	}
	
	public default void mouseExited(MouseEvent e)
	{	System.out.println("mouse exited");
	}

}
