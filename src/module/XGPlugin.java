package module;

import adress.XGAddress;
import device.XGDevice;

public class XGPlugin extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.plugin;

	protected XGPlugin(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
