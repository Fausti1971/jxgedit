package module;

import adress.XGAddress;
import device.XGDevice;

public class XGSystemFX extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.sysfx;

	protected XGSystemFX(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
