package gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public interface XGResizeable extends ComponentListener
{
	void componentResized();

	@Override  default void componentResized(ComponentEvent e)
	{	this.componentResized();
	}

	@Override  default void componentMoved(ComponentEvent e)
	{
	}

	@Override  default void componentShown(ComponentEvent e)
	{
	}

	@Override  default void componentHidden(ComponentEvent e)
	{
	}

}
