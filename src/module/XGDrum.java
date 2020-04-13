package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGDrum extends XGSuperModule
{	private static final String NAME = "Drum";

	public XGDrum(XGDevice dev, XMLNode n)
	{	super(dev, n);
	}

	public XGDrum(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	try
		{	return NAME + " (" + this.getAddress().getMid().getValue() + ")";
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return NAME + " (" + this.getAddress().getMid() + ")";
		}
	}

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
