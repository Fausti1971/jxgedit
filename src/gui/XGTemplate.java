package gui;

import java.io.FileNotFoundException;
import adress.XGAddress;
import adress.XGAddressable;
import application.Configurable;
import application.XGLoggable;
import device.XGDevice;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGTemplate implements XGAddressable, XGLoggable, XMLNodeConstants, Configurable
{
	public static void init(XGDevice dev)
	{	try
		{	XMLNode x = XMLNode.parse(dev.getResourceFile(XML_TEMPLATE));
			for(XMLNode t : x.getChildNodes(TAG_FRAME))
				dev.getTemplates().add(new XGTemplate(t));
			LOG.info(dev + " has " + dev.getTemplates().size() + " templates");
		}
		catch(FileNotFoundException e)
		{	LOG.info(dev + " has no GUI: " + e.getMessage());
		}
	}

/*******************************************************************************************/

	private final XGAddress address;
	private final XMLNode config;

	public XGTemplate(XMLNode n)
	{	this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS));
		this.config = n;
//		this.logInitSuccess();
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}
}
