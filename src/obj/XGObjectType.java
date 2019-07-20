package obj;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;
import parm.XGParameter;
import parm.XGParameterMap;

public class XGObjectType implements XGAdressable
{	private static Logger log = Logger.getAnonymousLogger();
	private static XGAdressableSet<XGObjectType> objectTypes = XGObjectDescriptionMap.getObjectDescriptionMap();

	public static XGObjectType getObjectTypeOrNew(XGAdress adr) throws InvalidXGAdressException
	{	for(XGObjectType d : objectTypes) if(adr.equalsValidFields(d.adress)) return d;
		return new XGObjectType(adr);
	}

	public static XGObjectType getObjectType(XGAdress adr)
	{	return objectTypes.get(adr);}

/******************************************************************************************************************/

	private final XGAdress adress;//0,0=System; 2,1=FX1; 2,64=EQ; 3=FX2, 8=MultiPart;  
	private final String objectName;
	private final String parameterMapName;
	private final XGAdressableSet<XGParameter> parameterSet;
	private final Set<XGBulkDumpDescription> dumpSequence;
	private XGAdressableSet<XGObject> instances = new XGAdressableSet<>();

	public XGObjectType(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, "unknown object-type", "unknown parameter-map", new HashSet<XGBulkDumpDescription>()
			{/**
			 * 
			 */
			private static final long serialVersionUID=1L;
			{	add(new XGBulkDumpDescription(adr));}});}

	public XGObjectType(XGAdress adr, String name, String pMapName, Set<XGBulkDumpDescription> dseq)
	{	this.adress = adr;
		this.objectName = name;
		this.parameterMapName = pMapName;
		this.parameterSet = XGParameterMap.getParameterMap(pMapName);
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
*/
	public XGAdress getAdress()
	{	return this.adress;}

	public XGObject getInstanceOrNew(XGAdress adr)
	{	XGObject o;
		if(this.instances.contains(adr)) return this.instances.get(adr);
		else this.instances.add(o = new XGObject(adr));
		return o;
	}

	public XGAdressableSet<XGObject> getInstances()
	{	return this.instances;}

	public XGAdressableSet<XGParameter> getParameterMap()
	{	return this.parameterSet;}

	public XGParameter getParameter(XGAdress adr)
	{	return this.parameterSet.getOrDefault(adr, new XGParameter(adr));}

	public String getName()
	{	return this.objectName;}

	@Override public String toString()
	{	return objectName + adress + ", " + parameterMapName + ", " + dumpSequence;}
}
