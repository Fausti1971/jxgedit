package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGDrumset extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.drumset;

	protected XGDrumset(XGDevice dev, XMLNode n)
	{	super(dev, n);
		if(this.getAddress().getHi().isRange())
		{	for(int i : this.getAddress().getHi())
			{	XGModule ds = new XGDrumset(this, new XGAddress(new XGAddressField(i), this.getAddress().getMid(), this.getAddress().getLo()));
				if(this.getAddress().getMid().isRange())
					for(int m : this.getAddress().getMid())
					{	new XGDrum(ds, new XGAddress(new XGAddressField(i), new XGAddressField(m), this.getAddress().getLo()));
					}
			}
			return;
		}
		if(this.getAddress().getMid().isRange())
		for(int m : this.getAddress().getMid())
		{	new XGDrum(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(m), this.getAddress().getLo()));
		}

	}

	protected XGDrumset(XGModule par, XGAddress adr)
	{	super(par, adr);
		
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getHi() + ")";
	}
}
