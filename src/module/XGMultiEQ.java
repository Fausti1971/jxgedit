package module;

import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGMultiEQ extends XGSuperModule
{	private static final XGModuleTag TAG =XGModuleTag.syseq;

	public XGMultiEQ(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	@Override public String toString()
	{	return this.getName();
	}

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
