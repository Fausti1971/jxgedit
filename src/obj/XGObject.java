package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.logging.Logger;
import application.InvalidXGAdressException;
import parm.XGParameter;
import parm.XGParameterConstants;
import parm.XGValue;

public class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();
	protected static Map<XGAdress, XGObject> instances = new HashMap<>();
	protected static ObjectChangeListener listener = null;

	public static XGObject getXGObjectInstance(XGAdress adr) throws InvalidXGAdressException
	{	if(instances.containsKey(adr)) return instances.get(adr);
		else
		{	XGObject o = new XGObject(adr);
			instances.put(adr, o);
			return o;
		}
	}

	public static Set<XGObject> getXGObjectInstances(XGAdress adr)
	{	Set<XGObject> s = new HashSet<>();
		for(XGAdress o : instances.keySet()) if(o.isPartOf(adr)) s.add(instances.get(o));
		return s;
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

	public XGParameter getParameter(int offset)
	{	try
		{	return XGObjectDescription.getXGObjectDescription(this.adress).parameterMap.get(offset);}
		catch(InvalidXGAdressException | NullPointerException | ExceptionInInitializerError e)
		{	e.printStackTrace();
			return new XGParameter(offset);
		}
	};
}
