package gui;
import adress.*;import module.*;import javax.swing.*;import javax.swing.event.*;import javax.swing.table.*;import java.awt.*;

public class XGModuleTable extends JTable implements java.awt.event.MouseListener
{
	private final int GAP = 6;

	public XGModuleTable(XGModuleType type)
	{	super(new XGModuleTableModel(type));
		this.getModel().addTableModelListener(this);
		this.addMouseListener(this);
		this.setAutoCreateRowSorter(true);
		this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setIntercellSpacing(new java.awt.Dimension(GAP, GAP));
		this.setRowHeight(this.getRowHeight() + GAP);
//		this.setGridColor(java.awt.Color.lightGray);//wird vom L&F wieder überschrieben
//		this.setShowGrid(false);
		this.setAutoscrolls(true);
		((DefaultTableCellRenderer)this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);//wird von Nimbus und GTK+ überschrieben
	}

	@Override public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column)
	{	Component c = super.prepareRenderer(renderer, row, column);
		if(c instanceof JLabel) ((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
		return c;
	}

	private void openEditWindow()
	{	java.util.Vector<module.XGModule> vec = ((XGModuleTableModel)this.getModel()).getRows();
		for(int r : this.getSelectedRows())
		{	XGEditWindow.getEditWindow(vec.get(r)).setVisible(true);
		}
	}

	public void mouseClicked(java.awt.event.MouseEvent event)
	{	if(event.getClickCount() == 2) this.openEditWindow();
	}

	public void mousePressed(java.awt.event.MouseEvent event)
	{
	}

	public void mouseReleased(java.awt.event.MouseEvent event)
	{
	}

	public void mouseEntered(java.awt.event.MouseEvent event)
	{
	}

	public void mouseExited(java.awt.event.MouseEvent event)
	{
	}
}
