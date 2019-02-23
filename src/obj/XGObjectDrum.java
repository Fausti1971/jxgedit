package obj;

import java.util.HashMap;
import java.util.Map;

public class XGObjectDrum extends XGObject
{	private static final int MIDMIN = 13, MIDMAX = 91, DATASIZE = 0x10;

	private static Map<Integer, Map<Integer, XGObjectDrum>> drums = new HashMap<>();

	private static Map<Integer,XGObjectDrum> getDrumset(XGAdress adr)
	{	return drums.getOrDefault(adr.getHi(), new HashMap<>());
	}

	public static XGObjectDrum getInstance(XGAdress adr)
	{	return getDrumset(adr).getOrDefault(adr.getMid(),new XGObjectDrum(adr));
	}

/*********************************************************************************************************/

	protected XGObjectDrum(XGAdress adr)
	{	super(adr);
		getDrumset(adr).put(adr.getMid(),this);
		
	}
}
