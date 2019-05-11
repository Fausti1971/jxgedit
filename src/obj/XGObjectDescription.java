package obj;

import java.util.HashSet;
import java.util.Map;
import application.InvalidXGAdressException;
import java.util.Set;
import java.util.logging.Logger;
import parm.XGParameter;
import parm.XGParameterMap;

public class XGObjectDescription
{	private static Logger log = Logger.getAnonymousLogger();
	private static Map<XGAdress, XGObjectDescription> objectDescriptions = XGObjectDescriptionMap.getObjectDescriptionMap();

	public static XGObjectDescription getXGObjectDescription(XGAdress adr) throws InvalidXGAdressException
	{	if(objectDescriptions.containsKey(adr)) return objectDescriptions.get(adr);
		else 
		{	XGObjectDescription desc = new XGObjectDescription(adr);
			objectDescriptions.put(adr, desc);
			return desc;
		}
	}

/******************************************************************************************************************/

	final XGAdress adress;//0,0,0=System; 2,1=FX1; 2,64=EQ; 3=FX2, 8=MultiPart;  
	final String objectName;
	final Map<Integer, XGParameter> parameterMap;
	final Set<XGDumpDescription> dumpSequence;

	public XGObjectDescription(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, "unknown object", "unknown_parameter", new HashSet(){{add(adr); add(adr);}});
	}

	public XGObjectDescription(XGAdress adr, String name, String pMapName, Set<XGDumpDescription> dseq)
	{	this.adress = adr;
		this.objectName = name;
		this.parameterMap = XGParameterMap.getParameterMap(pMapName);
		this.dumpSequence = dseq;
		log.info("" + this);
	}

	@Override public String toString()
	{	return objectName + adress + ", " + parameterMap + ", " + dumpSequence;}
}
