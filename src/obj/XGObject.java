package obj;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import application.InvalidXGAdressException;
import parm.XGParameter;
import parm.XGParameterConstants;
import parm.XGValue;

public class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();
	protected static ObjectChangeListener listener = null;

	public static XGObject getXGObjectInstance(XGAdress adr) throws InvalidXGAdressException
	{	return XGObjectType.getObjectInstance(adr);}

	public static XGObjectType getXGObjectType(XGAdress adr) throws InvalidXGAdressException
	{	return XGObjectType.getObjectType(adr);}

	public static Set<XGObject> getXGObjectInstances(XGAdress adr) throws InvalidXGAdressException
	{	return new TreeSet<>(getXGObjectType(adr).getObjects().values());}

	public static XGParameter getParameter(XGAdress adr)
	{	try
		{	return XGObjectType.getObjectType(adr).getParameter(adr.getLo());}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null; 
		}
	}

	public static XGValue getValue(XGAdress adr) throws InvalidXGAdressException
	{	return XGObject.getXGObjectInstance(adr).getXGValue(adr.getLo());}

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;
	protected final Map<Integer, XGValue> values = new TreeMap<>();

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}

	public XGValue getXGValue(int offset)
	{	if(values.containsKey(offset)) return this.values.get(offset);
		else
		{	try
			{	XGValue v = new XGValue(new XGAdress(this.adress.getHi(), this.adress.getMid(), offset));
				values.put(offset, v);
				return v;
			}
			catch(InvalidXGAdressException e1)
			{	e1.printStackTrace();
				return null;
			}
		}
	}

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
