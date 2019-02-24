package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import application.MU80;

public class MainFrame extends JFrame implements WindowListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MainFrame()
	{	setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		addWindowListener(this);

		Container root = super.getContentPane();
		root.setLayout(new BorderLayout());
		root.add(new MainMenuBar(), BorderLayout.NORTH);
		root.add(new MainTab(), BorderLayout.CENTER);
		root.add(new MainProgressBar(), BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	public void windowOpened(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{	MU80.exit();
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