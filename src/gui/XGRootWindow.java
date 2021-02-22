package gui;

import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import application.Configurable;
import application.JXG;
import xml.XMLNode;

public class XGMainWindow extends JFrame implements Configurable, WindowListener, ComponentListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/**********************************************************************************************************************/

	private JMenuBar menu = new JMenuBar();
	private XGStatusBar status = new XGStatusBar();

	public XGMainWindow()
	{
		this.setJMenuBar(menu);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.addComponentListener(this);
	
		this.setTitle(APPNAME);
		this.getContentPane().add(new JScrollPane(this.getRootComponent()), BorderLayout.CENTER);
		this.getContentPane().add(this.status, BorderLayout.SOUTH);

		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds(
			JXG
			this.config.getIntegerAttribute(ATTR_Y, 20),
			this.config.getIntegerAttribute(ATTR_W, MIN_W),
			this.config.getIntegerAttribute(ATTR_H, MIN_H));
		this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.setVisible(true);
	}

	public XGStatusBar getStatusBar()
	{	return this.status;
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
	{	JXG.getApp().quit();
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
	{	this.config.setIntegerAttribute(ATTR_W, e.getComponent().getWidth());
		this.config.setIntegerAttribute(ATTR_H, e.getComponent().getHeight());
	}

	@Override public void componentMoved(ComponentEvent e)
	{	this.config.setIntegerAttribute(ATTR_X, e.getComponent().getX());
		this.config.setIntegerAttribute(ATTR_Y, e.getComponent().getY());
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}
}
