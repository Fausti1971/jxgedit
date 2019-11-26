package gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import application.ConfigurationConstants;
import xml.XMLNode;

public class XGWindow extends JDialog implements GuiConstants, WindowListener, ComponentListener, ConfigurationConstants
{	private static final Logger log = Logger.getAnonymousLogger();
	private static final XGRootWindow root = new XGRootWindow();
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public static XGRootWindow getRootWindow()
	{	return root;
	}

/*************************************************************************************************************/

	private final XGWindowSource source;

	XGWindow()	//nur für Root-Window
	{	this.source = null;
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, XMLNode t)	//für XML-Templates
	{	super(own, mod);
		this.source = src;
		src.setWindow(this);
		this.getContentPane().add(this.createComponent(t));
		this.setLocationRelativeTo(own);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, Displayable comp, String name)	//für Swing-Templates
	{	super(own, name, mod);
		this.source = src;
		src.setWindow(this);
		JComponent c = comp.getGuiComponents();
		JPanel root = new JPanel();
		root.add(c);
		this.getContentPane().add(root);
		this.setLocationRelativeTo(own);
		this.pack();
		this.setResizable(true);
		this.setVisible(true);
	}

	private JComponent createComponent(XMLNode template)
	{	JComponent c = new JButton(template.getChildNodeTextContent(TAG_NAME, "no name"));
		return c;
	}

	public void windowOpened(WindowEvent e)
	{	this.source.windowOpened();
	}

	public void windowClosing(WindowEvent e)
	{
	}

	public void windowClosed(WindowEvent e)
	{	this.source.windowClosed();
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
	{	log.info(e.getComponent().toString());
	}

	public void componentMoved(ComponentEvent e)
	{
	}

	public void componentShown(ComponentEvent e)
	{
	}

	public void componentHidden(ComponentEvent e)
	{
	}

	public void open()
	{	this.setVisible(true);
	}
}
