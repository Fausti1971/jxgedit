package gui;
import static application.XGLoggable.LOG;import module.*;import value.XGValue;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;import javax.swing.event.ListSelectionListener;import javax.swing.event.TableModelEvent;import javax.swing.event.TableModelListener;import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;import java.util.HashSet;import java.util.Set;

public class XGModuleTable extends JTable
{
	private static final int GAP = 12;

/***********************************************************************************************************************/

	public XGModuleTable(XGModuleType type)
	{	super(new XGModuleTableModel(type));
		this.getModel().addTableModelListener(this);
		this.setCellSelectionEnabled(true);
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.getSelectionModel().addListSelectionListener(this);
		this.setAutoCreateRowSorter(true);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setRowHeight(this.getFont().getSize() + GAP);
		this.setShowGrid(true);//wird vom L&F wieder überschrieben
		this.setAutoscrolls(true);
		((DefaultTableCellRenderer)this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);//wird von Nimbus und GTK+ überschrieben
	}

	@Override public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column)
	{	Component c = super.prepareRenderer(renderer, row, column);
		if(c instanceof JLabel)
		{	JLabel l = (JLabel)c;
			l.setHorizontalAlignment(JLabel.CENTER);
		}
		return c;
	}

	@Override public void valueChanged(ListSelectionEvent e)//ListSelectionListener
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		String valueString = "";
		boolean answered = false;

		for(int col : this.getSelectedColumns())
		{	for(int r : this.getSelectedRows())
			{	int selRow = this.getRowSorter().convertRowIndexToModel(r);
				Object o = this.getModel().getValueAt(selRow, col);
				if(o instanceof XGModule)
				{	XGEditWindow.getEditWindow((XGModule)o).setVisible(true);
					this.clearSelection();
					continue;
				}
				if(o instanceof XGValue)
				{	if(!answered)
					{	valueString = JOptionPane.showInputDialog(this, this.getColumnName(col), 0);
						answered = true;
					}
					try
					{	((XGValue)o).setValue(valueString, true);
					}
					catch(NumberFormatException ex)
					{	LOG.warning(ex.getMessage());
					}
				}
			}
		}
		this.clearSelection();
	}
}
