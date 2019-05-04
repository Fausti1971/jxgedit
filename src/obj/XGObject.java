package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import application.InvalidXGAdressException;
import parm.XGParameter;
import parm.XGParameterConstants;
import parm.XGValue;

public abstract class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();
	protected static Map<Integer, Map<Integer, XGObject>> instances = new HashMap<>();
	protected static ObjectChangeListener listener = null;

	public static XGObject getXGObjectInstance(XGAdress adr) throws InvalidXGAdressException
	{	Map<Integer, XGObject> m = getXGObjectInstances(adr);
		if(m.containsKey(adr.getMid())) return m.get(adr.getMid());
		else
		{	XGObject o = XGObjectConstants.newXGObjectInstance(adr);
			m.put(adr.getMid(), o);
			return o;
		}
	}

	public static Map<Integer, XGObject> getXGObjectInstances(XGAdress adr)
	{	try
		{	if(instances.containsKey(adr.getHi())) return instances.get(adr.getHi());
			else
			{	Map<Integer, XGObject> m = new HashMap<>();
				instances.put(adr.getHi(), m);
				return m;
			}
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
	}

	public static XGParameter getParameter(XGAdress adr)
	{	try
		{	return XGObject.getXGObjectInstance(adr).getParameter(adr.getLo());}
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

/*********** abstract *******************************************************************************************************************/

	public abstract XGParameter getParameter(int offset); //TODO Umbau auf statische Methode mit String MAP_NAME im XGObjectConstants (Instance-Methoden liefern bei fehlender ParameterMap null)
}
