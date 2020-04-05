package module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.XGDevice;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindowSource;
import msg.XGMessenger;
import tag.XGTagable;
import xml.XMLNode;

public interface XGModule extends XGAddressable, XGTagable, XGModuleConstants, XGMessenger, XGTreeNode, XGWindowSource
{
	public static XGAddressableSet<XGModule> init(XGDevice dev)
	{
		XGAddressableSet<XGModule> set = new XGAddressableSet<XGModule>();
		File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return set;
		}
		XMLNode xml = XMLNode.parse(file);
		if(xml == null) return set;
		if(xml.getTag().equals(TAG_MODULES))
		{	for(XMLNode n : xml.getChildNodes())
			{	if(n.getTag().equals(TAG_MODULE))
				{	set.add(XGModule.newInstances(dev, null, n));
				}
			}
		}
		log.info(set.size() + " modules initialized from: " + file);
		return set;
	};

	private static XGModule newInstances(XGDevice dev, XGModule par, XMLNode n)//TODO: vielleicht nochmal mittels Reflection probieren (inkl. XGModuleTag)
	{	XGModuleTag t = XGModuleTag.valueOf(n.getStringAttribute(ATTR_ID));
		switch(t)
		{	case adpart:		return new XGADPart(dev, n); 
			case display:		return new XGDisplay(dev, n); 
			case drumset:		return new XGDrumset(dev, n); 
			case info:			return new XGInfo(dev, n); 
			case insfx:			return new XGInsertionFX(dev, n); 
			case multipart:		return new XGMultipart(dev, n); 
			case plugin:		return new XGPlugin(dev, n); 
			case syseq:			return new XGMultiEQ(dev, n); 
			case sysfx:			return new XGSystemFX(dev, n); 
			case system:		return new XGSystem(dev, n); 
			default:			return null;
		}
	}

	public static XGModuleTag getModuleTag(XGAddress adr)
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

/********************************************************************************************************************/

	String getName();
	Set<XGModule> getChildModules();
	XGModule getParentModule();
	XGAddressableSet<XGBulkDump> getBulks();
	XMLNode getGuiTemplate();
//	XGAddressableSet<XGBulkDump> getBulks();
//	XGTagableAddressableSet<XGOpcode>getOpcodes();
//	XGTagableAddressableSet<XGValue> getValues();

	@Override public default XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	public default XGModule getModule(XGAddress adr) throws XGModuleNotFoundException
	{	for(XGModule m : this.getChildModules())
		{	if(m.getAddress().contains(adr))
			{	if(m.getChildCount() == 0) return m;
				else return m.getModule(adr);
			}
		}
		throw new XGModuleNotFoundException("module not found " + adr);
	}

	@Override public default TreeNode getParent()
	{	if(this.getParentModule() == null) return this.getDevice();
		else return this.getParentModule();
	}

	@Override public default boolean getAllowsChildren()
	{	return this.getAddress().isFixed();
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildModules());
	}
}