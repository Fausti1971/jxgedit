package module;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;
import adress.XGAddress;
import adress.XGAddressableSet;
import device.XGDevice;
import msg.XGBulkDump;
import xml.XMLNode;

public abstract class XGSuperModule implements XGModule
{
	private final XGDevice device;
	private final XGAddress address;
	private final XGModule parent;
	private final Set<XGModule> children = new TreeSet<XGModule>();

	private XGAddressableSet<XGBulkDump> bulks = new XGAddressableSet<XGBulkDump>();

	private final XMLNode guiTemplate;

	protected XGSuperModule(XGDevice dev, XGModule par, XGAddress adr)
	{	this.device = dev;
		this.address = adr;
		this.parent = par;
		XMLNode temp = null;
		try
		{	temp = XMLNode.parse(dev.getResourceFile(this.getTag() + ".xml"));
		}
		catch(FileNotFoundException e)
		{	e.printStackTrace();
		}
		this.guiTemplate = temp;
	}

	@Override public XGAddress getAdress()
	{	return this.address;
	}

	@Override public XGModule getParent()
	{	return this.parent;
	}

	@Override public Set<XGModule> getChildren()
	{	return this.children;
	}

	public XGDevice getDevice()
	{	return device;
	}

	public XMLNode getGuiTemplate()
	{	return guiTemplate;
	}
}
