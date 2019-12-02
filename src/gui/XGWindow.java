package gui;

import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JTree;
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

	private final Component rootComponent;
	XGWindow()	//nur für Root-Window
	{	this.rootComponent = new JTree(JXG.getJXG());
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, String name)
	{	super(own, name, mod);
		this.rootComponent = src.getWindowContent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(src);
		this.getContentPane().add(this.rootComponent);
//		this.setLocation(src.getSourceComponent().getLocationOnScreen());	//java/mac-bug?
		int x = (int)own.getLocation().getX() + own.getWidth();
		int y = (int)own.getLocation().getY();
		this.setLocation(x, y);
		this.pack();
		this.setResizable(true);
		this.setVisible(true);
	}

	public Component getRootComponent()
	{	return this.rootComponent;
	}

	@Override public String toString()
	{	return this.getTitle();
	}
}
