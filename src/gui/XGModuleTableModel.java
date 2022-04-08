package gui;

import module.*;import value.*;import static xml.XMLNodeConstants.ATTR_ID;import javax.swing.event.*;import javax.swing.table.*;import java.util.*;

public class XGModuleTableModel  implements TableModel
{
	private final Vector<XGModule> rows;
	private final Vector<String> cols;
	private final Set<TableModelListener> listeners = new HashSet<>();

	XGModuleTableModel(XGModuleType t)
	{	this.rows = new Vector<>(t.getModules());
		this.cols = new Vector<>(t.getInfoTags());
		this.cols.add(0, ATTR_ID);
		for(XGModule m : this.rows)
			for(String tag : this.cols)
			{	XGValue v;
				if(tag.equals(ATTR_ID))
				{	if(m.getType() instanceof XGDrumsetModuleType)
					{	v = ((XGDrumsetModuleType)m.getType()).getProgramListener();
					}
					else continue;//falls ID eines XGModule: weitergehen, hier gibt es nichts zu sehen...
				}
				else v = m.getValues().get(tag);
				v.getValueListeners().add((XGValue)->notifyListener(this.rows.indexOf(m), this.cols.indexOf(tag)));
			}
//		LOG.info(this.getClass().getSimpleName() + " " + this.type + " initialized with " + this.rows.size() + " rows");
	}

	public Vector<XGModule> getRows()
	{	return this.rows;
	}

	public int getRowCount()
	{	return this.rows.size();
	}

	public int getColumnCount()
	{	return this.cols.size();
	}

	public String getColumnName(int i)
	{	if(i == 0) return ("ID");
		else return ((XGValue)this.getValueAt(0, i)).getParameter().getName();
	}

	public Class<?> getColumnClass(int i)
	{	if(i == 0) return XGModule.class;
		return XGValue.class;
	}

	public boolean isCellEditable(int r,int c)
	{	return false;
	}

	public Object getValueAt(int r, int c)
	{	if(c == 0) return this.rows.get(r);
		else return this.rows.get(r).getValues().get(this.cols.get(c));
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

	private void notifyListener(int row, int col)
	{	TableModelEvent e = new TableModelEvent(this, row, row, col, TableModelEvent.UPDATE);
		for(TableModelListener l : this.listeners) l.tableChanged(e);
	}
}

