package gui;

import adress.*;import static application.XGLoggable.LOG;import module.*;import parm.*;import value.*;import xml.*;import javax.swing.event.*;import javax.swing.table.*;import java.util.*;

public class XGModuleTableModel  implements TableModel, XGValueChangeListener
{
	private final XGModuleType type;
	private final Vector<XGModule> rows;
	private final Vector<XGAddress> cols;
	private Set<TableModelListener> listeners = new HashSet<>();

	XGModuleTableModel(XGModuleType t)
	{	this.type = t;
		this.rows = new Vector<>(XGModule.INSTANCES.getAllIncluded(type.getAddress()));
		this.cols = new Vector<>(this.type.getInfoAddresses());
		this.cols.add(0, null);
	}

	public int getRowCount()
	{	return this.rows.size();
	}

	public int getColumnCount()
	{	return this.cols.size();
	}

	public String getColumnName(int i)
	{	if(i == 0) return ("ID");
		try
		{	return XGValueStore.STORE.getFirstIncluded(this.cols.get(i)).getParameter().getName();
		}
		catch(XGMemberNotFoundException e)
		{	LOG.info(e.getMessage());
			return "" + i;
		}
	}

	public Class<?> getColumnClass(int i)
	{	return XGParameter.class;
	}

	public boolean isCellEditable(int i,int i1)
	{	return false;
	}

	public Object getValueAt(int r,int c)
	{	if(c == 0) return this.rows.get(r).getTranslatedID();
		try
		{	XGAddress adr = this.rows.get(r).getAddress().complement(this.cols.get(c));
			XGValue v = XGValueStore.STORE.get(adr);
			v.addValueListener(this);
			return v;
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}
		return null;
	}

	public void setValueAt(Object o,int i,int i1)
	{
	}

	public void addTableModelListener(TableModelListener listener)
	{	this.listeners.add(listener);
	}

	public void removeTableModelListener(TableModelListener listener)
	{	this.listeners.remove(listener);
	}

	public void contentChanged(XGValue v)
	{	int firstRow = -1;
		try
		{	firstRow = v.getAddress().getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}

		TableModelEvent e = new TableModelEvent(this, firstRow, firstRow, -1, TableModelEvent.UPDATE);
		for(TableModelListener l : this.listeners) l.tableChanged(e);
	}
}

