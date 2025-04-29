package gui;

import application.JXG;
import tag.XGTagable;import tag.XGTagableSet;import xml.XMLNode;import xml.XMLNodeConstants;import java.awt.*;
import java.awt.event.*;import java.util.Set;
import javax.swing.*;

public abstract class XGWindow extends JFrame implements XGUI, WindowListener, ComponentListener, XGTagable
{
	private static final int GAP = 10;
	public static XGMainWindow MAINWINDOW = null;
	final int MIN_W = 400; final int MIN_H = 200; final int MIN_X = 20; final int MIN_Y = 20;
	private static final ImageIcon LOGO = XGUI.loadImage("images/XGLogo32.gif");
	private static XMLNode CONFIG;
	public static Window FOCUSSED;

	public static void init()
	{	CONFIG = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_WIN);
		XGMainWindow.MAINWINDOW = new XGMainWindow();
	}

	public static void exit()
	{	if(XGWindow.MAINWINDOW != null) XGWindow.MAINWINDOW.dispose();
		if(XGWindow.FOCUSSED != null)
		{	XGWindow.FOCUSSED.dispose();
			XGWindow.FOCUSSED = null;
		}
	}

/**************************************************************************************************/

	final XMLNode config;
	final String tag;
	final XGWindow parentWindow;
	final Set<XGWindow> ownedWindows = new XGTagableSet<>();

	public XGWindow(XGWindow parent, String tag)
	{	super();
		this.tag = tag;
		this.parentWindow = parent;
		if(parent != null)
		{	parent.ownedWindows.add(this);
		}
		this.config = CONFIG.getChildNodeWithAttributeOrNew(TAG_ITEM, ATTR_ID, tag);
		this.setUndecorated(false);//um den Rahmen durch den WindowManager des Systems darstellen zu lassen
		this.setIconImage(LOGO.getImage());
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
		for(Window w : this.getOwnedWindows()) javax.swing.SwingUtilities.updateComponentTreeUI(w);
	}

	@Override public String getTag()
	{	return this.tag;
	}

	@Override public void dispose()
	{	synchronized(this)
		{	for(Window w : this.getOwnedWindows())
			{	w.setVisible(false);
				w.dispose();
			}
		}
		super.dispose();
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

	@Override public void componentShown(ComponentEvent e){}

	@Override public void componentHidden(ComponentEvent e){}

	@Override public void windowOpened(WindowEvent e){	XGWindow.FOCUSSED = e.getWindow();}

	@Override public void windowClosing(WindowEvent e){}

	@Override public void windowClosed(WindowEvent e){}

	@Override public void windowIconified(WindowEvent e){}

	@Override public void windowDeiconified(WindowEvent e){	XGWindow.FOCUSSED = e.getWindow();}

	@Override public void windowActivated(WindowEvent e){	XGWindow.FOCUSSED = e.getWindow();}

	@Override public void windowDeactivated(WindowEvent e){}
}