package gui;

import application.JXG;import static application.JXG.APPNAME;import config.XGConfigurable;import config.XGPropertyChangeListener;import device.XGDevice;import tag.XGTagable;import xml.XGProperty;import xml.XMLNode;import xml.XMLNodeConstants;import static xml.XMLNodeConstants.*;import static xml.XMLNodeConstants.ATTR_Y;import java.awt.*;
import java.awt.event.ComponentEvent;import java.awt.event.ComponentListener;import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;

public abstract class XGWindow extends JFrame implements XGUI, WindowListener, XGPropertyChangeListener, ComponentListener, XGTagable
{
	int MIN_W = 200, MIN_H = 200, MIN_X = 20, MIN_Y = 20;
	private static final Image LOGO = XGUI.loadImage("XGLogo32.gif");
	public static XGMainWindow MAINWINDOW = null;
	private static XMLNode CONFIG;

	public static void init()
	{	CONFIG = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_WIN);
		MAINWINDOW = new XGMainWindow(CONFIG);}

/*************************************************************************************************************/

	final Set<XGWindow> childWindows = new LinkedHashSet<>();
	private final XGWindow owner;
	final XMLNode config;

	public XGWindow(XGWindow own, String id)
	{	super();
		this.config = CONFIG.getChildNodeWithAttributeOrNew(TAG_ITEM, ATTR_ID, id);
		this.owner = own;
		if(this.owner != null) own.childWindows.add(this);
		XGDevice.device.getName().getListeners().add((XGProperty p)->{this.setTitle(this.getTitle());});
		this.setUndecorated(false);//um den Rahmen durch den WindowManager des Systems darstellen zu lassen
		this.setIconImage(LOGO);
		this.setResizable(true);
		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds
		(	this.config.getIntegerAttribute(ATTR_X, MIN_X),
			this.config.getIntegerAttribute(ATTR_Y, MIN_Y),
			this.config.getIntegerAttribute(ATTR_W, MIN_W),
			this.config.getIntegerAttribute(ATTR_H, MIN_H)
		);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addComponentListener(this);
		this.addWindowListener(this);
	}

	abstract JComponent createContent();

	protected void updateUI()
	{	javax.swing.SwingUtilities.updateComponentTreeUI(this);
		for(XGWindow w : this.childWindows) javax.swing.SwingUtilities.updateComponentTreeUI(w);
	}

	@Override public XGWindow getOwner(){	return this.owner;}

//	@Override public Window[] getOwnedWindows(){	return this.childWindows.toArray(new Window[]{});}

	@Override public String getTitle(){	return APPNAME + " - " + XGDevice.device;}

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

	@Override public void componentResized(ComponentEvent e)
	{	Component c = e.getComponent();
		this.config.setIntegerAttribute(ATTR_W, c.getWidth());
		this.config.setIntegerAttribute(ATTR_H, c.getHeight());
	}

	@Override public void componentMoved(ComponentEvent e)
	{	Component c = e.getComponent();
		this.config.setIntegerAttribute(ATTR_X, c.getX());
		this.config.setIntegerAttribute(ATTR_Y, c.getY());
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}

	@Override public void windowOpened(WindowEvent e)
	{
	}

	@Override public void windowClosing(WindowEvent e)
	{	//this.owner.childWindows.remove(this);
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
