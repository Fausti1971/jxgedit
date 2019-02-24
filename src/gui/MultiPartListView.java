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

	public static XGObjectMultiPart selectedMultiPart()
	{	if(instance.getSelectedValue() == null) instance.setSelectedIndex(0);
		return instance.getSelectedValue();
	}

/***************************************************************************************************************************/

	Set<XGObjectChangeListener> listeners = new HashSet<>();

	public MultiPartListView()
	{	super(XGObjectMultiPart.multiparts);
		super.addListSelectionListener(this);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		instance = this;
	}

	public void registerXGObjectChangeListener(XGObjectChangeListener listener)
		{	listeners.add(listener);
		}

	public void valueChanged(ListSelectionEvent e)
	{	for(XGObjectChangeListener l : listeners) l.objectChanged(selectedMultiPart());
	}
}
