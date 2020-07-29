package module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.XGDevice;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindowSource;
import xml.XMLNode;

public interface XGModule extends XGAddressable, XGModuleConstants, XGTreeNode, XGWindowSource
{
	public static void init(XGDevice dev) throws InvalidXGAddressException
	{
		File file;
		try
		{	file = dev.getResourceFile(XML_MODULE);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return;
		}
		XMLNode xml = XMLNode.parse(file);
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
			XGModule hi =
			null;
			String cat = n.getStringAttribute(ATTR_NAME);
			if(adr.getHi().getMin() > 47)//	falls DrumModule
			{	for(int h : adr.getHi())
				{	String name = cat + " " + (h - 47);
					hi = new XGSuperModule(dev, dev, name, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo()), n);
					for(int m : adr.getMid()) hi.getChildModule().add(new XGSuperModule(dev, hi, name, new XGAddress(new XGAddressField(h), new XGAddressField(m), adr.getLo()), n));
					dev.getModules().add(hi);
				}
			}
			else
			{	hi = new XGSuperModule(dev, dev, cat, adr, n);
				for(int m : adr.getAddress().getMid()) hi.getChildModule().add(new XGSuperModule(dev, hi, cat, new XGAddress(adr.getHi(), new XGAddressField(m), adr.getLo()), n));
				dev.getModules().add(hi);
			}
		}
	}

/********************************************************************************************************************/

	void request();
	XGDevice getDevice();
	XGTreeNode getParentNode();
	XGAddressableSet<XGModule> getChildModule();
	XGAddressableSet<XGBulkDump> getBulks();
	XGTemplate getGuiTemplate();
	String getCategory();

	default boolean isInstance()
	{	return this.getAddress().getMid().isFix();
	}

	public default int getID()
	{	XGAddress adr = this.getAddress();
		try
		{	return adr.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return adr.getMid().getMin();
		}
	}

	@Override default boolean isLeaf()
	{	return this.getChildCount() < 2;
	}

	@Override public default XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	@Override public default TreeNode getParent()
	{	return this.getParentNode();
	}

	@Override public default boolean getAllowsChildren()
	{	return true;
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildModule());
	}
}