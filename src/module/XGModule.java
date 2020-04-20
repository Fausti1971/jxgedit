package module;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import device.TimeoutException;
import device.XGDevice;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindowSource;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;
import tag.XGTagable;
import xml.XMLNode;

public interface XGModule extends XGAddressable, XGTagable, XGModuleConstants, XGMessenger, XGTreeNode, XGWindowSource
{
	public static XGModule factory(XGDevice dev, XMLNode n)//TODO: vielleicht nochmal mittels Reflection probieren (ohne XGModuleTag sondern mittels XGAddress)
	{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
		String t = n.getStringAttribute(ATTR_ID);
		switch(t)
		{	case "adpart":		return new XGADPart(dev, n); 
			case "display":		return new XGDisplay(dev, n); 
			case "drumset":		return new XGDrumset(dev, n); 
			case "info":			return new XGInfo(dev, n); 
			case "insfx":			return new XGInsertionFX(dev, n); 
			case "multipart":		return new XGMultipart(dev, n); 
			case "plugin":		return new XGPlugin(dev, n); 
			case "syseq":			return new XGMultiEQ(dev, n); 
			case "sysfx":			return new XGSystemFX(dev, n); 
			case "system":		return new XGSystem(dev, n); 
			default:			return null;
		}
	}
/*
	public static String getModuleTag(XGAddress adr)
	{	try
		{	switch(adr.getHi().getValue())
			{	case 0:		return XGModuleTag.system;
				case 1:		return XGModuleTag.info;
				case 2:
					switch(adr.getMid().getValue())
					{	case 1:		return XGModuleTag.sysfx;
						case 64:	return XGModuleTag.syseq;
					}
				case 3:		return XGModuleTag.insfx;
				case 6:
				case 7:		return XGModuleTag.display;
				case 8:
				case 9:
				case 10:	return XGModuleTag.multipart;
				case 16:
				case 17:
				case 18:	return XGModuleTag.adpart;
				case 48:
				case 49:
				case 50:
				case 51:	return XGModuleTag.drumset;
				case 112:
				case 113:	return XGModuleTag.plugin;
				default:	return null;
			}
		}
		catch(InvalidXGAddressException e)
		{	return null;
		}
	}
*/
/********************************************************************************************************************/

	String getName();
	Set<XGModule> getChildModules();
	XGModule getParentModule();
	XMLNode getGuiTemplate();

	@Override public default void submit(XGResponse msg) throws InvalidXGAddressException
	{
	}

	@Override public default XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{	return null;
	}

	@Override default boolean isLeaf()
	{	return this.getChildCount() < 2;
	}

	@Override public default XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	@Override public default TreeNode getParent()
	{	if(this.getParentModule() == null) return this.getDevice();
		else return this.getParentModule();
	}

	@Override public default boolean getAllowsChildren()
	{	return true;
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	if(this.getChildModules() == null) return Collections.emptyEnumeration();
		else return Collections.enumeration(this.getChildModules());
	}
}