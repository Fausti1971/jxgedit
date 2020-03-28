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
import device.XGDevice;
import gui.XGTree;
import gui.XGTreeNode;
import msg.XGMessenger;
import tag.XGTagable;
import tag.XGTagableSet;
import xml.XMLNode;

public interface XGModule extends XGAddressable, XGTagable, XGModuleConstants, XGMessenger, XGTreeNode
{
	public static XGTagableSet<XGModule> init(XGDevice dev)
	{
		XGTagableSet<XGModule> set = new XGTagableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return set;
		}
		XMLNode xml = XMLNode.parse(file);
		XGModule o = null;
		if(xml.getTag().equals(TAG_MODULES))
		{	for(XMLNode n : xml.getChildNodes())
			{	if(n.getTag().equals(TAG_MODULE))
				{	o = newInstance(dev, n);
					set.add(o);
				}
			}
		}
		log.info(set.size() + " opcodes initialized from: " + file);
		return set;
	};

	public static XGModuleTag getModuleTag(XGAddress adr)
	{	try
		{	switch(adr.getHi())
			{	case 0:		return XGModuleTag.system;
				case 1:		return XGModuleTag.info;
				case 2:
					switch(adr.getMid())
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
				default:	return XGModuleTag.unknown;
			}
		}
		catch(InvalidXGAddressException e)
		{	return XGModuleTag.unknown;
		}
	}

	static XGModule newInstance(XGDevice dev, XMLNode n)
	{	XGModuleTag tag = XGModuleTag.valueOf(n.getStringAttribute(ATTR_ID));
		String name = n.getStringAttribute(ATTR_NAME);
		
	}

/********************************************************************************************************************/

	Set<XGModule> getChildModules();
	XGModule getParentModule();
	XMLNode getGuiTemplate();
//	XGTagableAddressableSet<XGOpcode>getOpcodes();
//	XGTagableAddressableSet<XGValue> getValues();

	@Override public default XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	public default XGModule getModule(XGAddress adr) throws XGModuleNotFoundException
	{	for(XGModule m : this.getChildModules())
		{	if(m.getAddress().equalsValidFields(adr))
			{	if(m.getChildCount() == 0) return m;
				else return m.getModule(adr);
			}
		}
		throw new XGModuleNotFoundException("module not found " + adr);
	}

	public default XGModule getRootModule()
	{	if(this.getParent() != null) return ((XGModule)this.getParent()).getRootModule();
		else return this;
	}

	@Override public default boolean getAllowsChildren()
	{	return this.getAddress().isRange();
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildModules());
	}
}