package module;

import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
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

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{	return this.getDevice().getValues().getAllValid(this.getAddress());
	}
}
