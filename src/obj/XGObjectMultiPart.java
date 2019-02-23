package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import parm.TranslationMap;
import parm.ValueTranslation;
import parm.XGParameter;

public class XGObjectMultiPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;

	public static Vector<XGObjectMultiPart> multiparts = new Vector<XGObjectMultiPart>();
	public static Map<Tags, XGParameter> parameters = new HashMap<Tags, XGParameter>()
	{	private static final long serialVersionUID=1L;
		{	put(Tags.mp_elRes, new XGParameter(Tags.mp_elRes, HI, 0x00, 0, 32, "Element Reserve", "EleRes"));
			put(Tags.mp_ch, new XGParameter(Tags.mp_ch, HI, 0x04, 1, ByteType.MIDIBYTE, TranslationMap.channelMap, 0, 127, "MIDI Channel", "Ch"));
		}
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

	XGParameter prg = null, ch = getParameter(Tags.mp_ch), pm = null;
	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);
		ch.bind(this);
		multiparts.add(adr.getMid(), this);
	}

	public XGParameter getParameter(Tags t)
	{	return parameters.get(t);
	}

	@Override public String toString()
	{	XGObject old = ch.obj;
		ch.bind(this);
		String s =  String.format("%02d", this.id + 1) + " | " + "PartMode" + " | " + ch.getValueAsText() + " | " + "Program";
		ch.bind(old);
		return s;
	}
}
