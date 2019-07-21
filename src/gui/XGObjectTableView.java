package gui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import value.XGValue;
import value.XGValueChangeListener;

public class XGObjectTableView extends JTable implements TableModelListener, ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/****************************************************************************************************/

	private Set<XGValueChangeListener> vcl = new HashSet<>();
	private XGAdress adress;

	public XGObjectTableView(XGAdress adr)
	{	try
		{	this.adress = adr;
			TableModel tm = new XGObjectTableModel(adr);
			this.setModel(tm);
			this.setAutoCreateRowSorter(true);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.getSelectionModel().addListSelectionListener(this);
			tm.addTableModelListener(this);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		try
		{	this.adress = new XGAdress(this.adress.getHi(), this.getSelectedRow());
		}
		catch(InvalidXGAdressException e2)
		{	e2.printStackTrace();
			return;
		}
		for(XGValueChangeListener l : this.vcl)
		{	try
			{	l.valueChanged(XGValue.getValueOrNew(l.getAdress().complement(this.adress)));
			}
			catch(InvalidXGAdressException e1)
			{	e1.printStackTrace();
				return;
			}
		}
	}

	@Override public void tableChanged(TableModelEvent e)
	{	super.tableChanged(e);}

	public void addXGValueChangeListener(XGValueChangeListener l)
	{	this.vcl.add(l);}

	public void removeXGVAlueChangeListener(XGValueChangeListener l)
	{	this.vcl.remove(l);}
}
