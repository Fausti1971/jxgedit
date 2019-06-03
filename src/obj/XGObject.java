package obj;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueStorage;

public class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}

	public Set<XGValue> getValues() throws InvalidXGAdressException
	{	return XGValueStorage.getValues(this.adress);}
/*
	public XGValue getXGValue(int offset) throws InvalidXGAdressException
	{	Map<XGAdress, XGValue> map = this.getValues();
		XGAdress adr = new XGAdress(this.adress.getHi(), this.adress.getMid(), offset);
		if(map.containsKey(adr)) return map.get(adr);
		return null;
	}
*/
	public XGAdress getAdress()
	{	return adress;}

	@Override public String toString()
	{	try
		{	return XGObjectType.getObjectType(this.adress).getName() + this.adress;
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return "?";
		}
	}
}
