package obj;

import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import device.XGDevice;

public class XGObjectInstance implements XGAdressable
{	private final XGObjectType type;
	private final XGAdress adress;

	public XGObjectInstance(XGDevice dev, XGAdress adr) throws InvalidXGAdressException
	{	this.type = XGObjectType.getObjectTypeOrNew(dev, adr);
		this.adress = new XGAdress(INVALIDFIELD, adr.getMid(), INVALIDFIELD);
	}

	public XGAdress getAdress()
	{	return this.adress;}

	public XGObjectType getType()
	{	return this.type;}

	@Override public String toString()
	{	try
		{	return String.format("%03d", this.adress.getMid() + 1);}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return this.type.getName();
		}
	}

	public String getInfo()
	{	return this.getType().getName() + " " + this.toString();
	}
}
