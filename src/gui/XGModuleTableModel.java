package gui;

import adress.*;import static application.XGLoggable.LOG;import module.*;import parm.*;import tag.*;import value.*;import xml.*;import javax.swing.*;import javax.swing.event.*;import javax.swing.table.*;import java.util.*;

public class XGModuleTableModel  implements TableModel, XGValueChangeListener
{
	private final XGModuleType type;
	private final Vector<XGModule> rows;
	private final Vector<String> cols;
	private Set<TableModelListener> listeners = new HashSet<>();

	XGModuleTableModel(XGModuleType t)
	{	this.type = t;
		this.rows = new Vector<>(this.type.getModules());
		this.cols = new Vector<>(this.type.getInfoOpcodes());
		this.cols.add(0, "id");
	}

	public Vector<XGModule> getRows()
	{	return this.rows;
	}

	private int getColumn(String tag)
	{	for(String s : this.cols) if(s.equals(tag)) return this.cols.indexOf(s);
		return -1;
	}

	public int getRowCount()
	{	return this.rows.size();
	}

	public int getColumnCount()
	{	return this.cols.size();
	}

	public String getColumnName(int i)
	{	if(i == 0) return ("ID");
		return ((XGValue)this.getValueAt(0, i)).getParameter().getName();
	}

	public Class<?> getColumnClass(int i)
	{	if(i == 0) return XGModule.class;
		return XGValue.class;
	}

	public boolean isCellEditable(int r,int c)
	{	return false;
	}

	public Object getValueAt(int r,int c)
	{	if(c == 0)
		{	XGModule m = this.rows.get(r);
			if(m.getType() instanceof XGDrumsetModuleType)
			{	XGDrumsetModuleType drm = (XGDrumsetModuleType)m.getType();
				drm.getProgramListener().getValueListeners().add((XGValue->
				{	TableModelEvent e = new TableModelEvent(this, r, r, c, TableModelEvent.UPDATE);
					for(TableModelListener l : this.listeners) l.tableChanged(e);
				}));
			}
			return m;
		}
		String s = this.cols.get(c);
		XGValue v = this.rows.get(r).getValues().get(s);
		v.getValueListeners().add(this);
		return v;
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
	{	int row = -1;
		int col = 0;
		try
		{	col = this.getColumn(v.getTag());
			row = v.getAddress().getMid().getValue();
			if(col > 0)
			{	TableModelEvent e = new TableModelEvent(this, row, row, col, TableModelEvent.UPDATE);
				for(TableModelListener l : this.listeners) l.tableChanged(e);
			}
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}
	}
}

