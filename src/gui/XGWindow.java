package gui;

import java.util.logging.Logger;
import javax.swing.JDialog;
import application.ConfigurationConstants;

public class XGWindow extends JDialog implements GuiConstants, ConfigurationConstants
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

	XGWindow()	//nur für Root-Window
	{
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, String name)
	{	super(own, name, mod);
//		src.setWindow(this);
		this.addWindowListener(src);
		this.getContentPane().add(src.getWindowContent());
//		this.setLocation(src.getSourceComponent().getLocationOnScreen());	//java/mac-bug?
		int x = (int)own.getLocation().getX() + own.getWidth();
		int y = (int)own.getLocation().getY();
		this.setLocation(x, y);
		this.pack();
		this.setResizable(true);
		this.setVisible(true);
	}

	@Override public String toString()
	{	return this.getTitle();
	}
}
