package module;

import device.XGDevice;
import xml.XMLNode;

public class XGDisplay extends XGSuperModule
{	private static final XGModuleTag TAG = XGModuleTag.display;


	public XGDisplay(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}
}
