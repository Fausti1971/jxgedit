package obj;

import java.util.HashMap;
import java.util.Map;
import parm.TranslationMap;
import parm.XGParameter;
import parm.XGParameterConstants;

public class XGObjectMultiPart extends XGObject implements XGParameterConstants
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;

	private static Map<Integer, XGObjectMultiPart> multiparts = new HashMap<>();

	private static Map<Integer, XGParameter> initParameters(XGObject o)
	{	Map<Integer, XGParameter> map = new HashMap<>();
//		map.setSize(0x6F);
		map.put(MP_ELRES.getOffset(), new XGParameter(o, MP_ELRES.clone(), 0, 32, "Element Reserve", "EleRes"));
		map.put(MP_CH.getOffset(), new XGParameter(o, MP_CH.clone(), TranslationMap.channelMap, 0, 127, "MIDI Channel", "Ch"));
		return map;
	};

	public static Map<Integer, XGObjectMultiPart> getMultiParts()
	{	return multiparts;}

	public static XGObjectMultiPart getInstance(XGAdress adr)
	{	return multiparts.getOrDefault(adr.getMid(), new XGObjectMultiPart(adr));
	}

/********************************************************************************************************************/

	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);
		parameters = initParameters(this); 
		multiparts.put(adr.getMid(), this);
	}

	@Override public String toString()
	{	return String.format("%02d", this.adr.getMid() + 1) + " | " + "PartMode" + " | " + getParameter(MP_CH).getValueAsText() + " | " + "Program";
	}
}
