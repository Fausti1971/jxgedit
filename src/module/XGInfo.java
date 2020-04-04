package module;

import device.XGDevice;
import xml.XMLNode;

public class XGInfo extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.info;

	public XGInfo(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
