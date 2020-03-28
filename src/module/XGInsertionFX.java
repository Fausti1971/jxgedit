package module;

import adress.XGAddress;
import device.XGDevice;

public class XGInsertionFX extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.insfx;

	protected XGInsertionFX(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
		// TODO Auto-generated constructor stub
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
