package gui;

import java.util.Set;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import application.XGLoggable;
import value.ChangeableContent;

public class XGList<E extends Object> extends JList<E> implements ListSelectionListener, GuiConstants, XGLoggable
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
		this.setBorder(new TitledBorder(defaultLineBorder, this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, SMALL_FONT, COL_BORDER));
		this.value = s;
		this.addListSelectionListener(this);

//		this.logInitSuccess();
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{	if(e.getValueIsAdjusting()) return;
		this.value.setContent(this.getSelectedValue());
	}
}
