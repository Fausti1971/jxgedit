package gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import obj.XGObjectType;
import value.XGValue;

public class XGObjectTableModel implements TableModel
{	XGObjectType ot;
	Integer[] col, row;

	public XGObjectTableModel(XGAdress adr) throws InvalidXGAdressException
	{	ot = XGObjectType.getObjectType(adr);
		col = ot.getParameterMap().keySet().toArray(Integer[]::new);
		row = ot.getObjects().keySet().toArray(Integer[]::new);
	}
	
	public void setValueAt(Object aValue,int rowIndex,int columnIndex)
	{}
	
	public void removeTableModelListener(TableModelListener l)
	{}
	
	public boolean isCellEditable(int rowIndex,int columnIndex)
	{	return false;}
	
	public Object getValueAt(int rowIndex,int columnIndex)
	{	XGValue v = ot.getObjects().get(row[rowIndex]).getXGValue(col[columnIndex]);
		return v;
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
	{	}
}
