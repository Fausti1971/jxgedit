package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import device.XGDevice;

public class XGDrumset extends XGSuperModule
{	private static XGModuleTag TAG = XGModuleTag.drumset;

	protected XGDrumset(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	try
		{	return "Drumset " + ((this.getAddress().getHi() & 0x0F) + 1);
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return "Drumset";
		}
	}
}
