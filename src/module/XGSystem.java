package module;

import adress.XGAddress;
import device.XGDevice;

public class XGSystem extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.system;

/****************************************************************************************************/

	protected XGSystem(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
