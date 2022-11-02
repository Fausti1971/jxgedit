package gui;
import module.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;import javax.swing.event.ListSelectionListener;import javax.swing.table.*;
import java.awt.*;
import java.util.HashSet;import java.util.Set;

public class XGModuleTable extends JTable implements java.awt.event.MouseListener
{
	private final int GAP = 12;
	private Set<ListSelectionListener> listeners = new HashSet<>();

	public XGModuleTable(XGModuleType type)
	{	super(new XGModuleTableModel(type));
		this.getModel().addTableModelListener(this);
		this.addMouseListener(this);
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

	public Set<ListSelectionListener> getSelectionListener()
	{	return this.listeners;
	}

	public XGModule getSelectedModule()//nur das erste selektierte
	{	if(this.getModel() instanceof XGModuleTableModel)
		{	XGModuleTableModel model = (XGModuleTableModel)this.getModel();
			for(int r : this.getSelectedRows())
			{	Object o = model.getValueAt(this.getRowSorter().convertRowIndexToModel(r), 0);
				if(o instanceof XGModule) return (XGModule)o;
			}
		}
		return null;
	}

	public void mouseClicked(java.awt.event.MouseEvent event)
	{	for(ListSelectionListener l : this.listeners) l.valueChanged(new ListSelectionEvent(this.getSelectedModule(), 0, 0, false));
		if(event.getClickCount() == 2) XGEditWindow.getEditWindow(this.getSelectedModule()).setVisible(true);
	}

	public void mousePressed(java.awt.event.MouseEvent event){}

	public void mouseReleased(java.awt.event.MouseEvent event){}

	public void mouseEntered(java.awt.event.MouseEvent event){}

	public void mouseExited(java.awt.event.MouseEvent event){}
}
