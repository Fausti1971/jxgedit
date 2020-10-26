package gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public interface XGResizeable extends ComponentListener
{
	void componentResized();

	@Override public default void componentResized(ComponentEvent e)
	{	this.componentResized();
	}

	@Override public default void componentMoved(ComponentEvent e)
	{
	}

	@Override public default void componentShown(ComponentEvent e)
	{
	}

	@Override public default void componentHidden(ComponentEvent e)
	{
	}

}
