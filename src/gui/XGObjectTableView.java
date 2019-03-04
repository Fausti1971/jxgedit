package gui;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.TableView.TableRow;
import obj.XGObject;
import parm.XGParameter;

public class XGObjectTableView extends JTable implements ListSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static XGObjectTableView instance;

	public static XGObjectTableView getInstance()
	{	return instance;}

/****************************************************************************************************/

	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public XGObjectTableView(TableModel model)
	{	
		TableColumn tc = new TableColumn();
		tc.setHeaderValue("ID");
		tc.setResizable(true);
		this.addColumn(tc);
/*
		for(XGParameter p : pMap.values())
		{	tc = new TableColumn();
			tc.setHeaderValue(p.getShortName());
			tc.setResizable(true);
			this.addColumn(tc);
		}
*/		this.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		instance = this;
	}

	public void registerXGObjectSelectionListener(XGObjectSelectionListener listener)
	{	listeners.add(listener);}

	public void valueChanged(ListSelectionEvent e)
	{	//XGObject m = oMap.get(this.getSelectedRow());
		//for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(m);
	}
}
