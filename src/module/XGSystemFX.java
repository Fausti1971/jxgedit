package module;

import device.XGDevice;
import xml.XMLNode;

public class XGSystemFX extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.sysfx;

	public XGSystemFX(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
