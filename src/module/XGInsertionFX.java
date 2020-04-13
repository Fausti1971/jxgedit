package module;

import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGInsertionFX extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.insfx;

	public XGInsertionFX(XGDevice dev, XMLNode n)
	{	super(dev, n);
		for(int m : this.getAddress().getMid())
			new XGInsertionFX(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(m), this.getAddress().getLo()));
	}

	public XGInsertionFX(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
