package module;

import device.XGDevice;
import xml.XMLNode;

public class XGMultiEQ extends XGSuperModule
{
	public XGMultiEQ(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
