package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import parm.TranslationMap;
import parm.XGParameter;

public class XGObjectMultiPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;

	public static Vector<XGObjectMultiPart> multiparts = new Vector<XGObjectMultiPart>();

	public static Map<Tags, XGParameter> initParameters(XGObject o)
	{	Map<Tags, XGParameter> map = new HashMap<>();
		map.put(Tags.mp_elRes, new XGParameter(Tags.mp_elRes, o, 0x00, 0, 32, "Element Reserve", "EleRes"));
		map.put(Tags.mp_ch, new XGParameter(Tags.mp_ch, o, 0x04, 1, ByteType.MIDIBYTE, TranslationMap.channelMap, 0, 127, "MIDI Channel", "Ch"));
		return map;
	};

	public static XGObjectMultiPart getInstance(XGAdress adr)
	{	try
		{	return multiparts.get(adr.getMid());
		}
		catch(IndexOutOfBoundsException e)
		{	return new XGObjectMultiPart(adr);
		}
	}

	/********************************************************************************************************************/

	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);
		parameters = initParameters(this); 
		multiparts.add(adr.getMid(), this);
	}

	public XGParameter getParameter(Tags t)
	{	return parameters.get(t);
	}

	@Override public String toString()
	{	return String.format("%02d", this.id + 1) + " | " + "PartMode" + " | " + getParameter(Tags.mp_ch).getValueAsText() + " | " + "Program";
	}
}
