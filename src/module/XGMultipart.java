package module;

import adress.XGAddress;
import device.XGDevice;

public class XGMultipart extends XGSuperModule
{	private static final XGModuleTag TAG = XGModuleTag.multipart;

/*********************************************************************************/


	protected XGMultipart(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public XGModuleTag getTag()
	{	return TAG;
	}


	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
