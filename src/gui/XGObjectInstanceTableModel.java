package gui;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressableSetListener;
import obj.XGObjectInstance;
import obj.XGObjectType;
import parm.XGParameter;
import value.XGValue;

public class XGObjectInstanceTableModel implements TableModel, XGAdressableSetListener
{	private XGObjectType ot;
	private Vector<XGParameter> col;
	private Set<TableModelListener> tml = new HashSet<>();

//	private XGAdressableSet<XGAdressableInteger> row, col;
//	oder:
//	private XGAdressableVector<Integer> row, col;

	public XGObjectInstanceTableModel(String type)
	{	this.ot = XGObjectType.getObjectType(type);
		this.col = new Vector<XGParameter>(XGParameter.getAllValidParameterSet(type).values());
		XGValue.getValues(type).addListener(this);
	}

	public void setValueAt(Object aValue,int rowIndex,int columnIndex)
	{	System.out.println("setValue() in XGObjectTableModel wurde aufgerufen...");}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{	return false;}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{	XGAdress pa = this.col.get(columnIndex).getAdress();
		XGObjectInstance oi = ot.getXGObjectInstances().get(rowIndex);
		try
		{	return XGValue.getValue(pa.complement(oi.getAdress()));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
		return null;
	}
	
	public int getRowCount()
	{	return this.ot.getXGObjectInstances().size();}
	
	public String getColumnName(int columnIndex)
	{	return col.get(columnIndex).getLongName();}
	
	public int getColumnCount()
	{	return col.size();}
	
	public Class<?> getColumnClass(int columnIndex)
	{	return XGValue.class;}

	protected int getColumn(XGValue v)
	{	return this.col.indexOf(v.getParameter());}

	public void addTableModelListener(TableModelListener l)
	{	this.tml.add(l);}

	public void removeTableModelListener(TableModelListener l)
	{	this.tml.remove(l);}

	public void notifyTableModelListeners(TableModelEvent e)
	{	for(TableModelListener l : tml) l.tableChanged(e);}

	public void setChanged(XGAdress adr)
	{	int row = this.ot.getXGObjectInstances().indexOf(adr);
		notifyTableModelListeners(new TableModelEvent(this, row));
	}
}
