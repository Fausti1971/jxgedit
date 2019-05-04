package obj;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import application.InvalidXGAdressException;
import parm.XGParameter;
import parm.XGParameterMap;

public class XGObjectDescription
{
	private static Set<XGObjectDescription> objectDescriptions = XGObjectDescriptionSet.getObjectDescriptionSet();

	static XGObjectDescription getXGObjectDescription(XGAdress adr)
	{	XGObjectDescription desc = null;
		for(XGObjectDescription d : objectDescriptions)
		{	try
			{	if(adr.getHi() == d.adress.getHi())
				{	desc = d;
					if(adr.getMid() == d.adress.getMid())
					{	desc = d;
						if(adr.getLo() == d.adress.getLo()) desc = d;
					}
				}
			}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();
				if(desc == null) continue;
				else break;//entspringt der Schleife, wenn der angeforderte ODER der hinterlegte Adresspart nicht valide ist
			}
		}
		if(desc == null)
		{	try
			{	desc = new XGObjectDescription(adr);
				objectDescriptions.add(desc);
			}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();}
		}
		return desc;
	}

/******************************************************************************************************************/

	final XGAdress adress;//0,0,0=System; 2,1,0=FX1Reverb; 2,1,32=FX1Chorus; 2,64=FX1EQ; 3=FX2, 8=MultiPart;  
	final String objectName;
	final String parameterMapName;
	final Map<Integer, XGParameter> parameterMap;
	final Set<XGAdress> dumpSequence = new HashSet<>();

	public XGObjectDescription(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, "unknown object", "unknown_parameter", adr);
	}

	public XGObjectDescription(XGAdress adr, String name, String pMapName, XGAdress... dseq)
	{	this.adress = adr;
		this.objectName = name;
		this.parameterMapName = pMapName;
		this.parameterMap = XGParameterMap.getParameterMap(this.parameterMapName);
		for(XGAdress a : dseq) this.dumpSequence.add(a);
	}
}
