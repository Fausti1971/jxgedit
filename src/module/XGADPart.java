package module;

import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGADPart extends XGSuperModule
{
	private static final XGModuleTag TAG = XGModuleTag.adpart;

/********************************************************************************************************/

//	private XGValue info;

	protected XGADPart(XGDevice dev, XMLNode n)
	{	super(dev, n);
		for(int m : this.getAddress().getMid())
		{	new XGADPart(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(m), this.getAddress().getLo()));
		}
	}

	public XGADPart(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{	return null;
	}
}
