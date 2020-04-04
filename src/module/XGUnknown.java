package module;

import adress.XGAddress;
import device.XGDevice;
import xml.XMLNode;

public class XGUnknown extends XGSuperModule
{
	public XGUnknown(XGDevice dev, XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	public XGUnknown(XGDevice dev)
	{	super(dev);
	}

	public XGUnknown(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName() + " (" + getTag().name() + ")";
	}
}
