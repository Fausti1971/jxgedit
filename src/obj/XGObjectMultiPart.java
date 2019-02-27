package obj;

import java.util.HashMap;
import java.util.Map;
import memory.Bytes.ByteType;
import parm.Opcode;
import parm.TranslationMap;
import parm.ValueTranslation;
import parm.XGParameter;
import parm.XGParameterConstants;

public class XGObjectMultiPart extends XGObject implements XGParameterConstants
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;

	private static Map<Integer, XGObjectMultiPart> multiparts = new HashMap<>();

	public static Map<Integer, XGObjectMultiPart> getMultiParts()
	{	return multiparts;}

	public static XGObjectMultiPart getInstance(XGAdress adr)
	{	return multiparts.getOrDefault(adr.getMid(), new XGObjectMultiPart(adr));
	}

/********************************************************************************************************************/

	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);
		multiparts.put(adr.getMid(), this);
	}

	protected void initParameters()
	{	parameters.put(MP_ELRES, new XGParameter(this, new Opcode(MP_ELRES), 0, 32, "Element Reserve", "Rsrv"));
		parameters.put(MP_CH, new XGParameter(this, new Opcode(MP_CH), TranslationMap.channelMap, 0, 127, "MIDI Channel", "Ch"));
		parameters.put(MP_TUNE, new XGParameter(this, new Opcode(MP_TUNE, 2, ByteType.NIBBLE), ValueTranslation::translateSub128Div10, 0, 255, "Detune", "Tune"));
		return;
	};

	@Override public String toString()
	{	return String.format("%02d", this.adr.getMid() + 1) + " | " + "PartMode" + " | " + getParameter(MP_CH).getValueAsText() + " | " + "Program";
	}
}
