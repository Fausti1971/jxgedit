package gui;

import java.awt.Color;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import application.XGLoggable;
import value.ChangeableContent;

public class XGList<E extends Object> extends JList<E> implements ListSelectionListener, XGUI, XGLoggable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/******************************************************************************************************/

	private ChangeableContent<E> value;

	public XGList(String name, Set<E> list, ChangeableContent<E> s)
	{	super(new Vector<E>(list));
		this.setSelectedValue(s.getContent(), true);
		this.setName(name);

		Color c = this.getBackground().darker();
		this.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, SMALL_FONT, c));
		this.setVisibleRowCount(list.size());

		this.value = s;
		this.addListSelectionListener(this);

//		this.logInitSuccess();
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{	if(e.getValueIsAdjusting()) return;
		this.value.setContent(this.getSelectedValue());
	}
}
