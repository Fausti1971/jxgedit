package module;

import adress.XGAddress;
import device.XGDevice;

public class XGMultiEQ extends XGSuperModule
{	private static final XGModuleTag TAG =XGModuleTag.syseq;

	protected XGMultiEQ(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, TAG, adr);
	}

	@Override public String getNodeText()
	{	return this.getTag().name();
	}
}
