package application;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;
import adress.InvalidXGAdressException;
import device.TimeoutException;
import device.XGDevice;
import gui.Displayable;
import gui.GuiConstants;
import gui.XGTreeNode;
import gui.XGWindow;
import xml.XMLNode;

public class JXG implements Configurable, Displayable, XGTreeNode, GuiConstants
{	private static final Logger log = Logger.getAnonymousLogger();
	private static final JXG jxg = new JXG();

	public static JXG getJXG()
	{	return jxg;
	}

	public static void main(String[] args)
	{	//System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
//		%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg

//		Runtime.getRuntime().addShutdownHook
//		(	new Thread()
//			{	@Override public void run()
//				{	log.info("application exited");
//				}
//			}
//		);
		
		XGWindow.getRootWindow().open();
		XGDevice.init();
//		quit();
	}

	public static void quit()
	{	log.info("exiting application");
		try
		{	jxg.getConfig().save(CONFIGFILEPATH.toFile());
		}
		catch(IOException|XMLStreamException e)
		{	e.printStackTrace();
		}
		System.exit(0);
	}

/***************************************************************************************************************/

	private XMLNode config;
	private XGWindow window;

	private JXG()
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		HOMEPATH.toFile().mkdirs();
		File f = CONFIGFILEPATH.toFile();
		if(f.exists())
			this.config = XMLNode.parse(f);
		else this.config = new XMLNode(APPNAME, null);

		log.info("JXG config initialized");
	}

	public int getChildCount()
	{	return XGDevice.getDevices().size();
	}

	public TreeNode getParent()
	{	return null;
	}

	public boolean getAllowsChildren()
	{	return true;
	}

	public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(XGDevice.getDevices());
	}

	public XGWindow getWindow()
	{	return this.window;
	}

	public XMLNode getTemplate()
	{	return null;
	}

	@Override public String toString()
	{	return APPNAME;
	}

	public void setWindow(XGWindow win)
	{	this.window = win;
	}

	public void nodeSelected()
	{	new XGWindow(this, XGWindow.getRootWindow(), true, this, "settings");
	}

	public void selectNode()
	{	System.out.println(this + " select");
	}

	public void unselectNode()
	{	System.out.println(this + " unselected");
	}

	public XMLNode getConfig()
	{	return this.config;
	}

	public JComponent getGuiComponents()
	{	JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBorder(getDefaultBorder("settings"));

		JButton btn = new JButton("add device...");
		btn.addActionListener(new AbstractAction()
		{	private static final long serialVersionUID=2717877286233170533L;
	
			public void actionPerformed(ActionEvent e)
			{	try
				{	XGDevice dev = new XGDevice(null);
					if(dev != null)
					{	if(XGDevice.getDevices().add(dev))
						{	config.addChild(dev.getConfig());
							reloadTree(XGWindow.getRootWindow().getTree());
						}
					}
				}
				catch(InvalidXGAdressException|MidiUnavailableException | TimeoutException e1)
				{	log.info(e1.getMessage());
				}
			}
		});
		root.add(btn);

		btn = new JButton("refresh");
		btn.addActionListener(new AbstractAction()
		{	private static final long serialVersionUID=-7638850235957790794L;

			public void actionPerformed(ActionEvent e)
			{	XGDevice.init();
			}
		});
		root.add(btn);

		return root;
	}
}