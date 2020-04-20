package module;

import adress.XGAddressableSet;
import device.XGDevice;
import xml.XMLNode;

public class XGPlugin extends XGSuperModule
{

	public XGPlugin(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}

	@Override public XGAddressableSet<XGModule> getChildModules()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
