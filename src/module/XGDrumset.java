package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGDrumset extends XGSuperModule
{

	protected XGDrumset(XGDevice dev, XMLNode n)
	{	super(dev, n);
		for(int h : this.getAddress().getHi())
		{	XGModule ds = new XGDrumset(this, new XGAddress(new XGAddressField(h), this.getAddress().getMid(), this.getAddress().getLo()));
			for(int m : ds.getAddress().getMid())
			{	new XGDrumset(ds, new XGAddress(ds.getAddress().getHi(), new XGAddressField(m), ds.getAddress().getLo()));
			}
		}
	}

	public XGDrumset(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getHi() + ")";
	}
}
