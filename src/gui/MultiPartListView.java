package gui;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import obj.XGAdress;
import obj.XGObject;
import obj.XGObjectConstants;

public class MultiPartListView extends JList<XGObject> implements ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static MultiPartListView instance = null;

	public static MultiPartListView getInstance()
	{	return instance;}

	public static void setInstance(MultiPartListView instance)
	{	MultiPartListView.instance = instance;}

/***************************************************************************************************************************/

	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public MultiPartListView()
	{	super(new Vector<>(XGObject.getXGObjectInstances(new XGAdress(XGObjectConstants.MULTI,0,0)).values()));
		super.addListSelectionListener(this);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setInstance(this);
	}

	public void registerXGObjectSelectionListener(XGObjectSelectionListener listener)
		{	listeners.add(listener);}

	public void valueChanged(ListSelectionEvent e)
	{	XGObject m = getSelectedValue();
		for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(m);
	}
}
