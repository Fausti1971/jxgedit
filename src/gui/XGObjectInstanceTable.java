package gui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import adress.XGAdress;
import adress.XGInstanceSelectionListener;
import value.XGValue;

public class XGObjectInstanceTable extends JTable implements TableModelListener, ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/****************************************************************************************************/

	private XGObjectInstanceTableModel tm;
	private Set<XGInstanceSelectionListener> isl = new HashSet<XGInstanceSelectionListener>();

	public XGObjectInstanceTable(String type)
	{	this.tm = new XGObjectInstanceTableModel(type);
		this.tm.addTableModelListener(this);
		this.setModel(tm);
		this.setAutoCreateColumnsFromModel(false);

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getSelectionModel().addListSelectionListener(this);
	}

	@Override public void valueChanged(ListSelectionEvent e)// wird vom DefaultListSelectionModel über Änderungen der Selection informiert 
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		notifyXGInstanceSelectionListeners(((XGValue)this.tm.getValueAt(this.getSelectedRow(), 0)).getInstance().getAdress());
	}

	@Override public void tableChanged(TableModelEvent e) // wird vom TableModel über Änderungen am Model informiert
	{	super.tableChanged(e);
//		this.repaint();
	}

	public void addXGInstanceSelectionListener(XGInstanceSelectionListener l)
	{	this.isl.add(l);}

	public void removeXGInstanceSelectionListener(XGInstanceSelectionListener l)
	{	this.isl.remove(l);}

	private void notifyXGInstanceSelectionListeners(XGAdress adr)
	{	for(XGInstanceSelectionListener l : this.isl) l.instanceSelected(adr);}
}
