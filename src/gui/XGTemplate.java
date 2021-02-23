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
