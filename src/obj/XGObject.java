package obj;

import java.util.logging.Logger;
import adress.XGAdress;
import parm.XGParameterConstants;

public class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}
/*
	public Set<XGValue> getValues() throws InvalidXGAdressException
	{	return XGValueStorage.getValues(this.adress);}

	public XGValue getXGValue(int offset) throws InvalidXGAdressException
	{	Map<XGAdress, XGValue> map = this.getValues();
		XGAdress adr = new XGAdress(this.adress.getHi(), this.adress.getMid(), offset);
		if(map.containsKey(adr)) return map.get(adr);
		return null;
	}
*/
	public XGAdress getAdress()
	{	return adress;}
}
