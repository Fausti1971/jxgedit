package gui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import obj.XGObject;
import obj.XGObjectType;
import value.XGValue;

public class XGObjectTableView extends JTable implements TableModelListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/****************************************************************************************************/

	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public XGObjectTableView(XGAdress adr)
	{	try
		{	TableModel tm = new XGObjectTableModel(adr);
			this.setModel(tm);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionModel selectionModel = this.getSelectionModel();
			selectionModel.addListSelectionListener(this);
			tm.addTableModelListener(this);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	public void registerObjectSelectionListener(XGObjectSelectionListener listener)
	{	listeners.add(listener);}

	@Override public void valueChanged(ListSelectionEvent e)
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		XGValue v = (XGValue)this.getModel().getValueAt(this.getSelectedRow(), this.getSelectedColumn());
		XGObject o = XGObjectType.getObjectInstance(v.getAdress());
		for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(o);
	}
}
