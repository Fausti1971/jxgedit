package module;

import adress.XGAddress;
import device.XGDevice;

public class XGADPart extends XGSuperModule
{
	private static final XGModuleTag TAG = XGModuleTag.adpart;

/********************************************************************************************************/

	protected XGADPart(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
