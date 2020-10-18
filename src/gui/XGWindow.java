package gui;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import application.ConfigurationConstants;
import application.JXG;

public class XGWindow extends JFrame implements GuiConstants, ConfigurationConstants, WindowListener
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
	private final Set<XGWindow> childWindows = new LinkedHashSet<>();
	private final XGWindow owner;

	protected XGWindow()	//nur für Root-Window
	{	this.source = null;
		this.owner = null;
		this.rootComponent = new XGTree(JXG.getApp(), true);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setIconImage(new ImageIcon(this.getClass().getResource("XGLogo32.gif")).getImage());
		this.addWindowListener(this);
	}

	public XGWindow(XGWindowSource src, XGWindow own, boolean mod, boolean resize, String name)
	{	super(name);
		this.owner = own;
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.source = src;
		this.source.setChildWindow(this);
		this.rootComponent = src.getChildWindowContent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		this.addWindowListener(src);
		this.addWindowListener(this);
		this.getContentPane().add(new JScrollPane(this.rootComponent));
		this.setLocation(src.childWindowLocationOnScreen());

		this.pack();
		this.setResizable(resize);
		if(mod) this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.setAlwaysOnTop(mod);
		this.setVisible(true);
		this.toFront();
	}

	public XGWindowSource getSource()
	{	return this.source;
	}

	public JComponent getRootComponent()
	{	return this.rootComponent;
	}

	private void disposeChildWindows()
	{	for(XGWindow w : this.childWindows)
		{	w.setVisible(false);
			w.dispose();
			this.childWindows.remove(w);
		}
	}

	@Override public String toString()
	{	return this.getTitle();
	}

	@Override public void windowOpened(WindowEvent e)
	{	if(this.source != null) this.source.windowOpened(e);
		if(this.owner != null) this.owner.childWindows.add(this);
	}

	@Override public void windowClosing(WindowEvent e)
	{	if(this.source != null) this.source.windowClosing(e);
		if(this.owner != null) this.owner.childWindows.remove(this);
		this.disposeChildWindows();
	}

	@Override public void windowClosed(WindowEvent e)
	{	if(this.source != null) this.source.windowClosed(e);
	}

	@Override public void windowIconified(WindowEvent e)
	{	if(this.source != null) this.source.windowIconified(e);
	}

	@Override public void windowDeiconified(WindowEvent e)
	{	if(this.source != null) this.source.windowDeiconified(e);
	}

	@Override public void windowActivated(WindowEvent e)
	{	if(this.source != null) this.source.windowActivated(e);
	}

	@Override public void windowDeactivated(WindowEvent e)
	{	if(this.source != null) this.source.windowDeactivated(e);
	}
}
