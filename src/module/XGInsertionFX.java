package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGInsertionFX extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.insfx;

	public XGInsertionFX(XGDevice dev, XMLNode n)
	{	super(dev, n);
		if(this.getAddress().getMid().isRange())
		{	for(int i : this.getAddress().getMid())
				new XGInsertionFX(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(i), this.getAddress().getLo()));
		}
	}

	public XGInsertionFX(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}
}
