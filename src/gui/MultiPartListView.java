package gui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import obj.XGObjectMultiPart;

public class MultiPartListView extends JList<XGObjectMultiPart> implements ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	public static MultiPartListView instance = null;

/***************************************************************************************************************************/

	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public MultiPartListView()
	{	super(XGObjectMultiPart.multiparts);
		super.addListSelectionListener(this);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		instance = this;
	}

	public void registerXGObjectChangeListener(XGObjectSelectionListener listener)
		{	listeners.add(listener);
		}

	public void valueChanged(ListSelectionEvent e)
	{	XGObjectMultiPart m = getSelectedValue();
		for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(m);
	}
}
