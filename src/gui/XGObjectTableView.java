package gui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import application.InvalidXGAdressException;
import obj.XGAdress;
import obj.XGObject;
import obj.XGObjectType;
import parm.XGValue;

public class XGObjectTableView extends JTable implements ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/****************************************************************************************************/

	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public XGObjectTableView(XGAdress adr)
	{	try
		{	TableModel tm = new TableModel()
			{	XGObjectType ot = XGObjectType.getObjectType(adr);
				Integer[] col = ot.getParameterMap().keySet().toArray(Integer[]::new);
				Integer[] row = ot.getObjects().keySet().toArray(Integer[]::new);
	
				public void setValueAt(Object aValue,int rowIndex,int columnIndex)
				{}
				
				public void removeTableModelListener(TableModelListener l)
				{}
				
				public boolean isCellEditable(int rowIndex,int columnIndex)
				{	return false;}
				
				public Object getValueAt(int rowIndex,int columnIndex)
				{	return ot.getObjects().get(row[rowIndex]).getXGValue(col[columnIndex]);
				}
				
				public int getRowCount()
				{	return row.length;}
				
				public String getColumnName(int columnIndex)
				{	return ot.getParameter(col[columnIndex]).getLongName();}
				
				public int getColumnCount()
				{	return col.length;}
				
				public Class<?> getColumnClass(int columnIndex)
				{	return XGValue.class;}
				
				public void addTableModelListener(TableModelListener l)
				{}
			};
			super.setModel(tm);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	public void registerObjectSelectionListener(XGObjectSelectionListener listener)
	{	listeners.add(listener);}

	public void valueChanged(ListSelectionEvent e)
	{	//XGObject m = oMap.get(this.getSelectedRow());
		//for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(m);
	}
}
