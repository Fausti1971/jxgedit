package module;

import adress.XGAddress;
import device.XGDevice;

public class XGUnknown extends XGSuperModule
{
	public XGUnknown(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, XGModuleTag.unknown, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
