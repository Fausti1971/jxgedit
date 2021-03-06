package gui;
import adress.*;import module.*;import javax.swing.*;import javax.swing.event.*;import javax.swing.table.*;import java.awt.*;

public class XGModuleTable extends JTable
{
	public static final XGAddress
		MULTIPARTADDRESS = new XGAddress("8/0/0"),
		ADPARTADDRESS = new XGAddress("16/0/0"),
		INSERTIONADDRESS = new XGAddress("3/0/0"),
		PLUGINADDRESS = new XGAddress("112/0/0");

	public XGModuleTable(XGModuleType type)
	{	super(new XGModuleTableModel(type));
		this.getModel().addTableModelListener(this);
		this.setAutoCreateRowSorter(true);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
System.out.println("tableChanged: row=" + e.getFirstRow() + " col=" + e.getColumn());
	}
}
