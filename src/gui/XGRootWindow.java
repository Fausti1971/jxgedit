package gui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import application.Configurable;
import application.JXG;
import xml.XMLNode;

public class XGRootWindow extends XGWindow implements Configurable, WindowListener, ComponentListener, XGColorable
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
			this.getConfig().parseChildNodeIntegerContent(TAG_WINX, 20),
			this.getConfig().parseChildNodeIntegerContent(TAG_WINY, 20),
			this.getConfig().parseChildNodeIntegerContent(TAG_WINW, MIN_W),
			this.getConfig().parseChildNodeIntegerContent(TAG_WINH, MIN_H));
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
	{	this.getConfig().getChildNodeOrNew(TAG_WINW).setTextContent(e.getComponent().getWidth());
		this.getConfig().getChildNodeOrNew(TAG_WINH).setTextContent(e.getComponent().getHeight());
	}

	@Override public void componentMoved(ComponentEvent e)
	{	this.getConfig().getChildNodeOrNew(TAG_WINX).setTextContent(e.getComponent().getX());
		this.getConfig().getChildNodeOrNew(TAG_WINY).setTextContent(e.getComponent().getY());
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}

	@Override public JComponent getGuiComponent()
	{	return this.getRootComponent();
	}
}
