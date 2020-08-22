package gui;

import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import application.ConfigurationConstants;
import application.JXG;

public class XGWindow extends JDialog implements GuiConstants, ConfigurationConstants
{	static
	{	JDialog.setDefaultLookAndFeelDecorated(true);
	}
	private static XGRootWindow ROOT;
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public static XGRootWindow getRootWindow()
	{	if(ROOT == null) ROOT = new XGRootWindow();
		return ROOT;
	}

/*************************************************************************************************************/

	private final JComponent rootComponent;
	private final XGWindowSource source;

	protected XGWindow()	//nur für Root-Window
	{	this.source = null;
		this.rootComponent = new XGTree(JXG.getApp(), true);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setIconImage(new ImageIcon(this.getClass().getResource("XGLogo32.gif")).getImage());
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, String name)
	{	super(own, name, mod);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.source = src;
		this.source.setChildWindow(this);
		this.rootComponent = src.getChildWindowContent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(src);
		this.getContentPane().add(new JScrollPane(this.rootComponent));
		int x = (int)own.getLocation().getX() + own.getWidth();
		int y = (int)own.getLocation().getY();
		this.setLocation(x, y);
//		this.setIconImage(new ImageIcon(this.getClass().getResource("XGLogo32.gif")).getImage());
		this.pack();
		this.setResizable(false);
		this.setAlwaysOnTop(mod);
		this.setVisible(true);
		this.toFront();
//		System.out.println(this.getModalityType());
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
