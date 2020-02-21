package application;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;
import device.XGDevice;
import gui.XGContext;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import xml.XMLNode;

public class JXG implements Configurable, XGTreeNode, XGContext
{	static
	{	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		//	%1 = date+time (tb = mon, td = tag, tY = jahr, tl = std, tM = min, tS = sec) %2 = class+method, %3 = null, %4 = level, %5 = msg
	}

	private final static Set<String> actions = new LinkedHashSet<>();
	static final String
		ACTION_CONFIGURE = "configure...",
		ACTION_ADDNEWDEVICE = "add new device...",
		ACTION_REFRESHDEVICELIST = "refresh devicelist";

	private static final Logger log = Logger.getAnonymousLogger();
	public static final XMLNode config = initConfig(); 
	private final static JXG jxg = new JXG();

	private static XMLNode  initConfig()
	{	XMLNode x = new XMLNode(APPNAME, null);
		HOMEPATH.toFile().mkdirs();
		File f = CONFIGFILEPATH.toFile();
		if(f.exists()) x = XMLNode.parse(f);
		return x;
	}

	public static JXG getJXG()
	{	return jxg;
	}

	public static void main(String[] args)
	{	
//		Runtime.getRuntime().addShutdownHook
//		(	new Thread()
//			{	@Override public void run()
//				{	log.info("application exited");
//				}
//			}
//		);
		XGWindow.getRootWindow().setVisible(true);
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

	private XGTree tree;
	private boolean isSelected = false;

	private JXG()
	{	//System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tl:%1$tM:%1$tS %4$s %2$s: %5$s %n");
		JXG.actions.add(ACTION_CONFIGURE);
		JXG.actions.add(ACTION_ADDNEWDEVICE);
		JXG.actions.add(ACTION_REFRESHDEVICELIST);
		log.info("JXG config initialized");
	}

	@Override public TreeNode getParent()
	{	return null;
	}

	@Override public boolean getAllowsChildren()
	{	return true;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(XGDevice.getDevices());
	}

	@Override public String toString()
	{	return APPNAME;
	}

	@Override public XMLNode getConfig()
	{	return JXG.config;
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setSelected(boolean s)
	{	this.isSelected = s;
	}

	@Override public Set<String> getContexts()
	{	return JXG.actions;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	if(e == null || e.getActionCommand() == null) return;
		switch(e.getActionCommand())
		{	case ACTION_CONFIGURE:
				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
				break;
			case ACTION_ADDNEWDEVICE:
				XGDevice dev = new XGDevice(null);
				if(XGDevice.getDevices().add(dev))
				{	config.addChildNode(dev.getConfig());
					dev.reloadTree();
				};
				break;
			case ACTION_REFRESHDEVICELIST:
				XGDevice.init();
				break;
			default:
				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
		}
	}

	@Override public void setTree(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTree()
	{	return this.tree;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}
}