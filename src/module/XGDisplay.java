package module;

import adress.XGAddress;
import device.XGDevice;

public class XGDisplay extends XGSuperModule
{	private static final XGModuleTag TAG = XGModuleTag.display;

	protected XGDisplay(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
