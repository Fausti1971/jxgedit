package module;

import adress.XGAddress;
import adress.XGAddressField;
import device.XGDevice;
import xml.XMLNode;

public class XGMultipart extends XGSuperModule
{	private static final XGModuleTag TAG = XGModuleTag.multipart;

/**
 * @param dev *******************************************************************************/

	public XGMultipart(XGDevice dev, XMLNode n)
	{	super(dev, n);
		for(int i : this.getAddress().getMid())
			new XGMultipart(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(i), this.getAddress().getLo()));
	}

	public XGMultipart(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}
}
