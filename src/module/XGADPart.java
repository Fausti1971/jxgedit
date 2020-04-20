package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGADPart extends XGSuperModule
{

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
}
