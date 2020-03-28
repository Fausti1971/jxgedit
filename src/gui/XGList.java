package gui;

import java.util.Set;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import value.ChangeableContent;

public class XGList<E extends Object> extends JList<E> implements ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/******************************************************************************************************/

	private ChangeableContent<E> value;

	public XGList(Set<E> list, ChangeableContent<E> s)
	{	super(new Vector<E>(list));
		this.setSelectedValue(s.get(), true);
		this.value = s;
		this.addListSelectionListener(this);
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{	if(e.getValueIsAdjusting()) return;
		this.value.set(this.getSelectedValue());
	}
}
