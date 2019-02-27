package obj;

import java.util.HashMap;
import java.util.Map;

public class XGObjectUnknown extends XGObject
{	private static Map<Integer, XGObjectUnknown> instances = new HashMap<>();

	public static XGObjectUnknown getInstance(XGAdress adr)
	{	return instances.getOrDefault(adr.hashCode(),new XGObjectUnknown(adr));
	}

/************************************************************************************************************************************/

	public XGObjectUnknown(XGAdress adr)
	{	super(adr);
		instances.put(adr.hashCode(), this);
		log.info("unknown object received: " + adr);
	}

	protected void initParameters()
	{}
}
