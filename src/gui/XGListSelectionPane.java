package gui;

import javax.swing.*;import javax.swing.event.ListSelectionEvent;import javax.swing.event.ListSelectionListener;import java.awt.*;

public class XGListSelectionPane<T> extends JList<T> implements ListSelectionListener
{
	private final JDialog dialog = new JDialog();

	@SafeVarargs public XGListSelectionPane(Component parent, String text, T... selectionValues)
	{	super(selectionValues);
		this.dialog.setModal(true);
		this.dialog.setTitle(text);
		this.addListSelectionListener(this);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.dialog.add(new JScrollPane(this));
		this.dialog.setLocationRelativeTo(parent);
		this.dialog.pack();
		this.dialog.setVisible(true);
	}

	public void valueChanged(ListSelectionEvent event)
	{	if(this.dialog.isVisible()) this.dialog.setVisible(false);
	}
}
