package gui;

import static application.JXG.APPNAME;import config.XGPropertyChangeListener;import device.XGDevice;import xml.XGProperty;import xml.XMLNode;import static xml.XMLNodeConstants.ATTR_NAME;import static xml.XMLNodeConstants.TAG_DEVICE;import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JFrame;

public abstract class XGWindow extends JFrame implements XGUI, WindowListener, XGPropertyChangeListener
{	static
	{	JFrame.setDefaultLookAndFeelDecorated(true);
	}
	private static final String LOGO_NAME = "XGLogo32.gif";

/*************************************************************************************************************/

	final Set<XGWindow> childWindows = new LinkedHashSet<>();
	private final XGWindow owner;
	final XMLNode config;

	public XGWindow(XGWindow own, XMLNode cfg)
	{	super();
		this.config = cfg;
		this.owner = own;
		if(this.owner != null) own.childWindows.add(this);
		XGDevice.device.getName().getListeners().add((XGProperty p)->{this.setTitle(this.getTitle());});
		this.setIconImage(gui.XGUI.loadImage(LOGO_NAME));
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(javax.swing.JDialog.HIDE_ON_CLOSE);
		this.setLocationRelativeTo(this.owner);
		this.addWindowListener(this);
	}

	protected void updateUI()
	{	javax.swing.SwingUtilities.updateComponentTreeUI(this);
		for(XGWindow w : this.childWindows) javax.swing.SwingUtilities.updateComponentTreeUI(w);
	}

	@Override public String getTitle()
	{	return APPNAME + " - " + XGDevice.device;
	}

	@Override public void dispose()
	{	synchronized(this.childWindows)
		{	for(XGWindow w : this.childWindows)
			{	w.setVisible(false);
				w.dispose();
			}
			this.childWindows.clear();
			super.dispose();
		}
	}

	@Override public void windowOpened(WindowEvent e)
	{
	}

	@Override public void windowClosing(WindowEvent e)
	{
	}

	@Override public void windowClosed(WindowEvent e)
	{
	}

	@Override public void windowIconified(WindowEvent e)
	{
	}

	@Override public void windowDeiconified(WindowEvent e)
	{
	}

	@Override public void windowActivated(WindowEvent e)
	{
	}

	@Override public void windowDeactivated(WindowEvent e)
	{
	}
}
