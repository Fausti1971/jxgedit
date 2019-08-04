package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import value.XGValue;

public class TreeFrame extends JFrame implements WindowListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static int GAP = 10;

	public TreeFrame()
	{	setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		addWindowListener(this);

		Container root = super.getContentPane();

		BorderLayout layout = new BorderLayout();
		layout.setVgap(10);
		root.setLayout(layout);

		root.add(new MainMenuBar(), BorderLayout.NORTH);

		JPanel center = new JPanel(new GridLayout(1, 3, GAP, GAP));
		XGTreeModel model = new XGTreeModel(XGValue.getValues());
		JTree tree = new JTree(model);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		center.add(new JScrollPane(tree));
		root.add(center, BorderLayout.CENTER);

		root.add(new MainProgressBar(), BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	public void windowOpened(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{	System.exit(0);
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowIconified(WindowEvent e)
	{
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowActivated(WindowEvent e)
	{
	}

	public void windowDeactivated(WindowEvent e)
	{
	}
}
