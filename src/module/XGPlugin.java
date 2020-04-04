package module;

import device.XGDevice;
import xml.XMLNode;

public class XGPlugin extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.plugin;

	public XGPlugin(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
