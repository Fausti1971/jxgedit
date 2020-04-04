package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;

public class XGDrum extends XGSuperModule
{	private static final String NAME = "Drum";

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
}
