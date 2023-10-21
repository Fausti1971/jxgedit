package gui;

import xml.XMLNode;import javax.swing.*;import javax.swing.event.PopupMenuEvent;import javax.swing.event.PopupMenuListener;
import java.awt.*;import java.awt.event.*;
import java.util.Collection;

public class XGPopup<T> extends JPopupMenu implements PopupMenuListener
{
	private final Collection<? extends T>list;
	private final XGPopupListener<T> listener;

	XGPopup(Collection<? extends T>list, XGPopupListener<T> handler)
	{	this.listener = handler;
		this.list = list;
		this.addPopupMenuListener(this);
	}

	@Override public void popupMenuWillBecomeVisible(PopupMenuEvent event)
	{	for(T n : this.list)
		{	JMenuItem i = new JMenuItem();
			if(n instanceof XMLNode) i.setText(((XMLNode)n).getTextContent().toString());
			else i.setText(n.toString());
			i.addActionListener((ActionEvent)->this.listener.popupSelected(n));
			this.add(i);
		}
	}

	@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent event)
	{	this.removeAll();
	}

	@Override public void popupMenuCanceled(PopupMenuEvent event)
	{
	}
}