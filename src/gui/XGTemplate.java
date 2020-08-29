package gui;

import java.io.FileNotFoundException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import device.XGDevice;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGTemplate implements XGAddressable, XGLoggable, XMLNodeConstants
{
	public static void init(XGDevice dev)
	{	try
		{	XMLNode x = XMLNode.parse(dev.getResourceFile(XML_TEMPLATES));
			for(XMLNode t : x.getChildNodes(TAG_FRAME))
				dev.getTemplates().add(new XGTemplate(t));
			LOG.info(dev + " has " + dev.getTemplates().size() + " templates");
		}
		catch(FileNotFoundException e)
		{	LOG.info(dev + " has no templates: " + e.getMessage());
		}
	}

/*******************************************************************************************/

	private final XGAddress address;
	private final XMLNode xml;

	public XGTemplate(XMLNode n)
	{	this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
		this.xml = n;
//		this.logInitSuccess();
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XMLNode getXMLNode()
	{	return this.xml;
	}
}
