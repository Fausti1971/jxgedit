package obj;

import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;
import parm.XGParameterConstants;
import value.XGValue;

public class XGObject implements XGObjectConstants, XGParameterConstants, XGAdressable
{	protected static final Logger log = Logger.getAnonymousLogger();

	public static XGObject getXGObjectOrNew(XGAdress adr) throws InvalidXGAdressException
	{	return XGObjectType.getObjectTypeOrNew(adr).getInstanceOrNew(adr);}

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;
	private XGAdressableSet<XGValue> values = new XGAdressableSet<XGValue>();

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}

	public XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValueAdress()) throw new InvalidXGAdressException("no valid value-adress: " + adr);
		if(this.values.contains(adr)) return this.values.get(adr);
		return null;
	}

	public XGValue getValueOrNew(XGAdress adr) throws InvalidXGAdressException
	{	XGValue v = this.getValue(adr);
		if(v == null);
		{	v = new XGValue(adr);
			this.values.add(v);
//			notifyListeners(v);
		}
		return v;
	}

	public XGAdress getAdress()
	{	return adress;}

	@Override public String toString()
	{	try
		{	return XGObjectType.getObjectTypeOrNew(this.adress).getName() + this.adress;
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return "?";
		}
	}
}
