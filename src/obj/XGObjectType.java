package obj;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import parm.XGParameter;
import parm.XGParameterMap;

public class XGObjectType
{	private static Logger log = Logger.getAnonymousLogger();
	private static Set<XGObjectType> objectTypes = XGObjectDescriptionMap.getObjectDescriptionMap();

	public static XGObjectType getObjectType(XGAdress adr) throws InvalidXGAdressException
	{	for(XGObjectType d : objectTypes) if(adr.equalsValidFields(d.adress)) return d;
		return new XGObjectType(adr);
	}
/*
	public static XGObject getObjectInstance(XGAdress adr)
	{	try
		{	return getObjectType(adr).getObjectOrNew(adr);}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
	}
*/
	public static XGParameter getParameter(XGAdress adr) throws InvalidXGAdressException
	{	return getObjectType(adr).getParameter(adr.getLo());}

/******************************************************************************************************************/

	private final XGAdress adress;//0,0=System; 2,1=FX1; 2,64=EQ; 3=FX2, 8=MultiPart;  
	private final String objectName;
	private final String parameterMapName;
	private final Map<Integer, XGParameter> parameterMap;
	private final Set<XGBulkDumpDescription> dumpSequence;
//	private Set<XGAdress> objects = new TreeSet<>();

	public XGObjectType(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, "unknown object-type", "unknown parameter-map", new HashSet(){{add(adr); add(adr);}});}

	public XGObjectType(XGAdress adr, String name, String pMapName, Set<XGBulkDumpDescription> dseq)
	{	this.adress = adr;
		this.objectName = name;
		this.parameterMapName = pMapName;
		this.parameterMap = XGParameterMap.getParameterMap(pMapName);
		this.dumpSequence = dseq;
		log.info("" + this);
	}
/*
	public XGObject getObjectOrNew(XGAdress adr)
	{	try
		{	XGObject o;
			if(this.objects.contains(adr)) return this.objects.get(adr.getMid());
			else
			{	this.objects.add(o = new XGObject(new XGAdress(this.adress.getHi(), adr.getMid())));
				return o;
			}
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
	}

	public Set<XGAdress> getObjects()
	{	return this.objects;}
*/
	public Map<Integer, XGParameter> getParameterMap()
	{	return this.parameterMap;}

	public XGParameter getParameter(int offs)
	{	return this.parameterMap.getOrDefault(offs, new XGParameter(offs));}

	public String getName()
	{	return this.objectName;}

	@Override public String toString()
	{	return objectName + adress + ", " + parameterMapName + ", " + dumpSequence;}
}
