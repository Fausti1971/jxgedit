package module;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.TimeoutException;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import gui.XGWindowSource;
import parm.XGTable;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public abstract class XGModule implements XGAddressable, XGModuleConstants, XGTreeNode, XGWindowSource, XGValueChangeListener
{
	public static void init(XGDevice dev) throws InvalidXGAddressException
	{
		File file;
		try
		{	file = dev.getResourceFile(XML_MODULE);
		}
		catch(FileNotFoundException e)
		{	LOG.warning(e.getMessage());
			return;
		}
		XMLNode xml = XMLNode.parse(file);
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
			XGModule hi = null;
			String cat = n.getStringAttribute(ATTR_NAME);
			if(adr.getHi().getMin() > 47)//	falls DrumModule
			{	for(int h : adr.getHi())
				{	String name = cat + " " + (h - 47);
					if(adr.getMid().isRange())
					{	hi = new XGModuleFolder(dev, dev, name, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo()), n);
						for(int m : adr.getMid()) hi.getChildModule().add(new XGModuleInstance(dev, hi, name, new XGAddress(new XGAddressField(h), new XGAddressField(m), adr.getLo()), n));
					}
					else hi = new XGModuleInstance(dev, dev, name, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo()), n);
					dev.getModules().add(hi);
				}
			}
			else
			{	if(adr.getMid().isRange())
				{	hi = new XGModuleFolder(dev, dev, cat, adr, n);
					for(int m : adr.getAddress().getMid()) hi.getChildModule().add(new XGModuleInstance(dev, hi, cat, new XGAddress(adr.getHi(), new XGAddressField(m), adr.getLo()), n));
				}
				else hi = new XGModuleInstance(dev, dev, cat, new XGAddress(adr.getHi(), adr.getMid(), adr.getLo()), n);
				dev.getModules().add(hi);
			}
		}
	}

/********************************************************************************************************************/

	private boolean selected;
	protected XGWindow window;
	private final XGTreeNode parentNode;
	protected final XGAddressableSet<XGModule> childModules = new XGAddressableSet<>();
	protected final String category;
	protected final XGAddress address;
	protected final XGDevice device;
	private final XGTemplate guiTemplate;
	protected final XGTable idTranslator;

	public XGModule(XGDevice dev, XGTreeNode par, String cat, XGAddress adr, XMLNode cfg)
	{	this.parentNode = par;
		this.category = cat;
		this.address = adr;
		this.device = dev;
		this.idTranslator = dev.getTables().get(cfg.getStringAttribute(ATTR_TRANSLATOR));
		this.guiTemplate = dev.getTemplates().getFirstIncluding(this.address);
	}

	public void requestAll()
	{	int missed = 0;
		long time = System.currentTimeMillis();
		for(XGBulkDump b : this.getBulks())
		try
		{	this.getDevice().getMidi().request(b.getRequest());
		}
		catch(TimeoutException e)
		{	LOG.severe(e.getMessage());
			missed++;
		}
		if(missed == 0) LOG.info(this.getBulks().size() + " dumps requested within " + (System.currentTimeMillis() - time) + " ms");
		else LOG.severe(this.getBulks().size() + " dumps requested within " + (System.currentTimeMillis() - time) + " ms (" + missed + " failed)");
	}

	public abstract XGAddressableSet<XGBulkDump> getBulks();

	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public void setSelected(boolean s)
	{	this.selected = s;
	}

	@Override public boolean isSelected()
	{	return this.selected;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public void windowOpened(WindowEvent e)
	{	this.setSelected(true);
		this.repaintNode();
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.setSelected(false);
		this.setChildWindow(null);
		this.repaintNode();
	}


	public XGDevice getDevice()
	{	return this.device;
	}

	XGTreeNode getParentNode()
	{	return this.parentNode;
	}

	XGAddressableSet<XGModule> getChildModule()
	{	return this.childModules;
	}

	public XGTemplate getGuiTemplate()
	{	return this.guiTemplate;
	}

	public int getID()
	{	XGAddress adr = this.getAddress();
		try
		{	return adr.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return adr.getMid().getMin();
		}
	}

	public String getCategory()
	{	return this.category;
	}

	public String getTranslatedID()
	{	if(this.idTranslator == null) return "";
		else return this.idTranslator.getByIndex(this.getID()).getName();
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return XGComponent.init(this);
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	@Override public TreeNode getParent()
	{	return this.getParentNode();
	}

	@Override public boolean getAllowsChildren()
	{	return true;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildModule());
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaintNode();
	}

}