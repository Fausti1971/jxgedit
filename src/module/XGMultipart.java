package module;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressableSet;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGMultipart extends XGSuperModule
{	private static final XGModuleTag TAG = XGModuleTag.multipart;

/**
 * @param dev *******************************************************************************/

	Set<String> infos = new LinkedHashSet<>();

	public XGMultipart(XGDevice dev, XMLNode n)
	{	super(dev, n);
		for(int m : this.getAddress().getMid())
			new XGMultipart(this, new XGAddress(this.getAddress().getHi(), new XGAddressField(m), this.getAddress().getLo()));
		this.infos.add("mp_midichannel");
	}

	public XGMultipart(XGModule par, XGAddress adr)
	{	super(par, adr);
	}

	@Override public String toString()
	{	return this.getName() + " (" + this.getAddress().getMid() + ")";
	}

	@Override public XGAddressableSet<XGValue> getFilteredSet()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
