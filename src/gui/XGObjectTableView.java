package gui;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
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

	Map<Integer, XGObject> oMap;
	Map<Integer, XGParameter> pMap;
	Set<XGObjectSelectionListener> listeners = new HashSet<>();

	public XGObjectTableView(Map<Integer, XGObject> map)
	{	oMap = map;
		pMap = oMap.get(0).getParamters();
		this.addColumn(new TableColumn());
		this.addColumn(new TableColumn());
//		setModel(createDataModel());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		instance = this;
	}

	public void registerXGObjectSelectionListener(XGObjectSelectionListener listener)
	{	listeners.add(listener);}

	public void valueChanged(ListSelectionEvent e)
	{	XGObject m = oMap.get(this.getSelectedRow());
		for(XGObjectSelectionListener l : listeners) l.xgObjectSelected(m);
	}

	private TableModel createDataModel()
	{	TableModel dataModel = new AbstractTableModel()
		{/**
		*
		*/
			private static final long serialVersionUID=1L;
			public int getColumnCount()
			{	return pMap.size();}

			public int getRowCount()
			{	return oMap.size();}

			public Object getValueAt(int row, int col)
			{	return Integer.valueOf(row*col);}
		};
		return dataModel;
	};
}
