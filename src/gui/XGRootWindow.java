package gui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JScrollPane;
import application.Configurable;
import application.JXG;
import xml.XMLNode;

public class XGRootWindow extends XGWindow implements Configurable, WindowListener, ComponentListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/**********************************************************************************************************************/

	private final XMLNode config = JXG.config.getChildNodeOrNew(TAG_WIN);

	public XGRootWindow()
	{	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.addComponentListener(this);
	
		this.setTitle(APPNAME);
		this.getContentPane().add(new JScrollPane(this.getRootComponent()));

		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds(
			this.config.getIntegerAttribute(ATTR_WINX, 20),
			this.config.getIntegerAttribute(ATTR_WINY, 20),
			this.config.getIntegerAttribute(ATTR_WINW, MIN_W),
			this.config.getIntegerAttribute(ATTR_WINH, MIN_H));
		this.setModal(false);
		this.setVisible(true);
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void windowOpened(WindowEvent e)
	{
	}

	@Override public void windowClosing(WindowEvent e)
	{
	}

	@Override public void windowClosed(WindowEvent e)
	{	JXG.quit();
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

	@Override public void componentResized(ComponentEvent e)
	{	this.config.setIntegerAttribute(ATTR_WINW, e.getComponent().getWidth());
		this.config.setIntegerAttribute(ATTR_WINH, e.getComponent().getHeight());
	}

	@Override public void componentMoved(ComponentEvent e)
	{	this.config.setIntegerAttribute(ATTR_WINX, e.getComponent().getX());
		this.config.setIntegerAttribute(ATTR_WINY, e.getComponent().getY());
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}
}
