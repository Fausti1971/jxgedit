package gui;

import java.awt.Toolkit;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import application.ConfigurationConstants;
import application.JXG;

public class XGWindow extends JDialog implements GuiConstants, ConfigurationConstants
{	static
	{	JDialog.setDefaultLookAndFeelDecorated(true);
	}
	private static final Logger log = Logger.getAnonymousLogger();
	private static XGRootWindow root;
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public static XGRootWindow getRootWindow()
	{	if(root == null) root = new XGRootWindow();
		return root;
	}

/*************************************************************************************************************/

	private final JComponent rootComponent;
	private final XGWindowSource source;

	XGWindow()	//nur für Root-Window
	{	this.source = null;
		this.rootComponent = new XGTree(JXG.getJXG());
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, String name)
	{	super(own, name, mod);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.source = src;
		this.rootComponent = src.getChildWindowContent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(src);
		this.getContentPane().add(this.rootComponent);
		int x = (int)own.getLocation().getX() + own.getWidth();
		int y = (int)own.getLocation().getY();
		this.setLocation(x, y);
		this.pack();
		this.setResizable(true);
	}

	public XGWindowSource getSource()
	{	return this.source;
	}

	public JComponent getRootComponent()
	{	return this.rootComponent;
	}

	@Override public String toString()
	{	return this.getTitle();
	}
}
