package gui;

import java.util.Set;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import value.ChangeableContent;
import xml.XMLNode;

public class XGList<E extends Object> extends JList<E> implements ListSelectionListener, XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/******************************************************************************************************/

	private ChangeableContent<E> value;
	private final XMLNode config = new XMLNode("list", null);

	public XGList(String name, Set<E> list, ChangeableContent<E> s)
	{	super(new Vector<E>(list));
		this.setSelectedValue(s.getContent(), true);
		this.setName(name);
		this.setSizes(5,  5);
		this.borderize();
		this.value = s;
		this.addListSelectionListener(this);
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{	if(e.getValueIsAdjusting()) return;
		this.value.setContent(this.getSelectedValue());
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
