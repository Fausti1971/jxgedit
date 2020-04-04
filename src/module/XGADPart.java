package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGADPart extends XGSuperModule
{
	private static final XGModuleTag TAG = XGModuleTag.adpart;

/********************************************************************************************************/

	protected XGADPart(XGDevice dev, XMLNode n)
	{	super(dev, n);
		if(this.getAddress().getMid().isRange())
		{	for(int i : this.getAddress().getMid())
				this.getChildModules().add(new XGADPart(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(i), this.getAddress().getLo())));
		}
	}

	public XGADPart(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}
}
