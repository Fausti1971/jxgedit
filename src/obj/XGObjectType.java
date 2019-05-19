package obj;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import parm.XGParameter;
import parm.XGParameterMap;

public class XGObjectType
{	private static Logger log = Logger.getAnonymousLogger();
	private static Set<XGObjectType> objectTypes = XGObjectDescriptionMap.getObjectDescriptionMap();

	public static XGObjectType getObjectType(XGAdress adr) throws InvalidXGAdressException
	{	for(XGObjectType d : objectTypes) if(adr.isMemberOf(d.adress)) return d;
		return new XGObjectType(adr);
	}

	public static XGObject getObjectInstance(XGAdress adr)
	{	try
		{	return getObjectType(adr).getObject(adr);}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
	}

/******************************************************************************************************************/

	private final XGAdress adress;//0,0=System; 2,1=FX1; 2,64=EQ; 3=FX2, 8=MultiPart;  
	private final String objectName;
	private final String parameterMapName;
	private final Map<Integer, XGParameter> parameterMap;
	private final Set<XGDumpDescription> dumpSequence;
	private Map<Integer, XGObject> objects = new TreeMap<>();

	public XGObjectType(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, "unknown object", "unknown_parametermap", new HashSet(){{add(adr); add(adr);}});}

	public XGObjectType(XGAdress adr, String name, String pMapName, Set<XGDumpDescription> dseq)
	{	this.adress = adr;
		this.objectName = name;
		this.parameterMapName = pMapName;
		this.parameterMap = XGParameterMap.getParameterMap(pMapName);
		this.dumpSequence = dseq;
		log.info("" + this);
	}

	public XGObject getObject(XGAdress adr)
	{	try
		{	XGObject o;
			if(this.objects.containsKey(adr.getMid())) return this.objects.get(adr.getMid());
			else
			{	this.objects.put(adr.getMid(), o = new XGObject(adr));
				return o;
			}
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
	}

	public Map<Integer, XGObject> getObjects()
	{	return this.objects;}

	public Map<Integer, XGParameter> getParameterMap()
	{	return this.parameterMap;}

	public XGParameter getParameter(int offs)
	{	return this.parameterMap.getOrDefault(offs, new XGParameter(offs));}

	public String getName()
	{	return this.objectName;}

	@Override public String toString()
	{	return objectName + adress + ", " + parameterMapName + ", " + dumpSequence;}
}
