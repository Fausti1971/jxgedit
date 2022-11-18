package gui;
import static application.XGLoggable.LOG;import application.XGMath;import static gui.XGUI.*;import module.*;import value.XGValue;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;import javax.swing.event.TableModelEvent;import javax.swing.event.TableModelListener;import javax.swing.table.*;import javax.swing.text.TableView;
import java.awt.*;
import java.util.*;

public class XGModuleTable extends JTable
{
	private static final int GAP = 12;
	private static final JButton DEF_BUTTON = new JButton();
	private static final JLabel DEF_LABEL = new JLabel();
	static
	{	DEF_LABEL.setHorizontalAlignment(JLabel.CENTER);
		DEF_LABEL.setOpaque(true);
	}
	private static final TableCellRenderer DEF_LABEL_RENDERER = (table,o,isSelected,hasFocus,row,col)->
	{	if(o instanceof XGValue)
		{	XGValue v = (XGValue)o;
			String s = v.toString();

			Color bg = COL_BAR_BACK;
			if(v.getValue() != v.getDefaultValue()) bg = COL_BAR_BACK_CHANGED;
			if(isSelected) bg = COL_BAR_BACK_DARK;
			DEF_LABEL.setBackground(bg);

			DEF_LABEL.setText(s);

			XGModuleTable t = (XGModuleTable)table;
			FontMetrics fm = DEF_LABEL.getFontMetrics(DEF_LABEL.getFont());
			int w = SwingUtilities.computeStringWidth(fm, s);
			t.getColumnModel().getColumn(col).setMinWidth(w);
		}
		return DEF_LABEL;
	};

	private static final TableCellRenderer DEF_BUTTON_RENDERER = (table,o,isSelected,hasFocus,row,col)->
	{	if(o instanceof XGModule)
		{	String s = o.toString();

			DEF_BUTTON.setText(s);

			XGModuleTable t = (XGModuleTable)table;
			FontMetrics fm = DEF_BUTTON.getFontMetrics(DEF_BUTTON.getFont());
			int w = SwingUtilities.computeStringWidth(fm, s);
			t.getColumnModel().getColumn(col).setMinWidth(w);
		}
		return DEF_BUTTON;
	};

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
		this.setDefaultRenderer(XGModule.class, (JTable table,Object o,boolean b,boolean b1,int i,int i1)->{ DEF_BUTTON.setText(o.toString()); return DEF_BUTTON;});
		this.setDefaultRenderer(XGValue.class, DEF_LABEL_RENDERER);
		this.setDefaultRenderer(XGModule.class, DEF_BUTTON_RENDERER);
		((DefaultTableCellRenderer)this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);//wird von Nimbus und GTK+ überschrieben
	}

	@Override public void valueChanged(ListSelectionEvent e)//ListSelectionListener
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		String valueString = null;
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
					{	valueString = JOptionPane.showInputDialog(this, this.getColumnName(col), "0");
						answered = true;
						if(valueString == null)
						{	this.clearSelection();
							return;
						}
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