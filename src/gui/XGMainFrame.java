package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import adress.InvalidXGAdressException;
import application.DomNode;
import application.ConfigurationChangeListener;
import application.ConfigurationConstants.ConfigurationEvent;
import application.JXG;

public class XGMainFrame extends JFrame implements WindowListener, ComponentListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static XGMainFrame MAINFRAME;

	public static XGMainFrame getMainFrame()
	{	return MAINFRAME;
	}

	public static void initMainFrame()
	{	MAINFRAME = new XGMainFrame();
	}

	/*********************************************************************************************************/

	Set<ConfigurationChangeListener> listeners = new HashSet<>();

	public XGMainFrame()
	{	setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		addWindowListener(this);

		Container root = super.getContentPane();
		root.setLayout(new BorderLayout());
		root.add(new MainMenuBar(), BorderLayout.NORTH);
		try
		{	root.add(new XGMainTab(), BorderLayout.CENTER);}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}

		root.add(new MainProgressBar(), BorderLayout.SOUTH);

//		this.listeners.add(Configuration.getCurrentConfig());
		this.pack();
		this.setVisible(true);
	}

	public void addConfigurationListener(ConfigurationChangeListener l)
	{	this.listeners.add(l);
	}

	public void notifyListeners()
	{	for(ConfigurationChangeListener l : this.listeners) l.configurationChanged(ConfigurationEvent.Gui);
	}

	public void windowOpened(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{	JXG.quit();
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

	public void componentResized(ComponentEvent e)
	{	this.notifyListeners();
	}

	public void componentMoved(ComponentEvent e)
	{	this.notifyListeners();
	}

	public void componentShown(ComponentEvent e)
	{
	}

	public void componentHidden(ComponentEvent e)
	{
	}
}
