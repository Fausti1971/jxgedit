package gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;

public class XGPopup<T> extends JPopupMenu implements ActionListener
{
	private final XGPopupListener<T> listener;
	XGPopup(Collection<? extends T>list, XGPopupListener<T> handler)
	{	this.listener = handler;
		for(T n : list)
		{	XGPopupItem<T> i = new XGPopupItem<T>(n);
			i.addActionListener(this);
			this.add(i);
		}
	}

	public void actionPerformed(ActionEvent event)
	{	Object o = event.getSource();
		if(o instanceof XGPopupItem)
		{	XGPopupItem<T> item = (XGPopupItem)o;
			listener.popupSelected(item.getItem());
		}
	}

/*********************************************************************************************************************/

	private class XGPopupItem<E> extends JMenuItem
	{	private final E item;

		XGPopupItem(E item)
		{	this.item = item;
			this.setText(item.toString());
		}

		E getItem()
		{	return this.item;
		}
	}
}