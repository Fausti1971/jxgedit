package module;

import device.XGDevice;
import xml.XMLNode;

public class XGSystem extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.system;

/****************************************************************************************************/

	public XGSystem(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
