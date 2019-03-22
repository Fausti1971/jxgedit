package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import parm.*;

public abstract class XGObject implements XGObjectConstants, XGOpcodeConstants
{	protected static final Logger log = Logger.getAnonymousLogger();
	protected static Map<Integer, Map<Integer, XGObject>> instances = new HashMap<>();
	protected static ObjectChangeListener listener = null;

	public static XGObject getXGObjectInstance(XGAdress adr)
	{	Map<Integer, XGObject> m = getXGObjectInstances(adr);
		if(m.containsKey(adr.getMid())) return m.get(adr.getMid());
		else
		{	XGObject o = XGObjectConstants.newXGObjectInstance(adr);
			m.put(adr.getMid(), o);
			return o;
		}
	}

	public static Map<Integer, XGObject> getXGObjectInstances(XGAdress adr)
	{	if(instances.containsKey(adr.getHi())) return instances.get(adr.getHi());
		else
		{	Map<Integer, XGObject> m = new HashMap<>();
			instances.put(adr.getHi(), m);
			return m;
		}
	}

	public static XGOpcode getOpcode(XGAdress adr)
	{	return getXGObjectInstance(adr).getOpcode(adr.getLo());}

	public static XGParameter getParameter(XGAdress adr)
	{	return getXGObjectInstance(adr).getParameter(adr.getLo());}

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;
	protected final Map<Integer,XGValue> values = new HashMap<>();

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}

	public XGValue getValue(int offset)
	{	return this.values.getOrDefault(offset, new XGValue(new XGAdress(this.adress.getHi(), this.adress.getMid(), offset)));}

	public XGAdress getAdress()
	{	return adress;}

/*********** abstract *******************************************************************************************************************/

	public abstract XGParameter getParameter(int offset);
	public abstract XGOpcode getOpcode(int offset);
}
