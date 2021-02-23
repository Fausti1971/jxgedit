package gui;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import application.*;

public class XGMainWindow extends JFrame implements WindowListener, ComponentListener, ConfigurationConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/**********************************************************************************************************************/

	private final JMenuBar menu = this.createMenu();
	private final XGStatusBar status = new XGStatusBar();

	public XGMainWindow()
	{
		this.setJMenuBar(menu);
		this.setContentPane(this.createContent());

		this.setTitle(APPNAME);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.addComponentListener(this);

		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds(
			JXG.mainWindowBounds.x,
			JXG.mainWindowBounds.y,
			JXG.mainWindowBounds.width,
			JXG.mainWindowBounds.height);
//		this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.setVisible(true);
	}

	public XGStatusBar getStatusBar()
	{	return this.status;
	}

	private JMenuBar createMenu()
	{	JMenuBar bar = new JMenuBar();
		return bar;
	}

	private JComponent createContent()
	{	JPanel content = new JPanel();
		return content; 
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
	{	JXG.mainWindowBounds.width = e.getComponent().getWidth();
		JXG.mainWindowBounds.height = e.getComponent().getHeight();
	}

	@Override public void componentMoved(ComponentEvent e)
	{	JXG.mainWindowBounds.x = e.getComponent().getX();
		JXG.mainWindowBounds.y = e.getComponent().getY();
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}
}
