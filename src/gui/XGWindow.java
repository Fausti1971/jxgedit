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

public class XGWindow extends JFrame implements XGUI, ConfigurationConstants, WindowListener
{	static
	{	JDialog.setDefaultLookAndFeelDecorated(true);
	}

/*************************************************************************************************************/

	private final Set<XGWindow> childWindows = new LinkedHashSet<>();
	private final JFrame owner;

	public XGWindow(JFrame own, String name)
	{	super(name);
		this.owner = own;
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		this.addWindowListener(src);
		this.addWindowListener(this);

		this.pack();
		this.setVisible(true);
		this.toFront();
	}

	private void disposeChildWindows()
	{	for(XGWindow w : this.childWindows)
		{	w.setVisible(false);
			w.dispose();
			this.childWindows.remove(w);
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
