package obj;

import java.util.HashMap;
import java.util.Map;
import memory.Bytes.ByteType;
import parm.TranslationMap;
import parm.ValueTranslation;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectMultiPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;

	private static final Map<Integer, XGParameter> parameters = initParameters();

	private static Map<Integer, XGParameter> initParameters()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		m.put(MP_ELRES, new XGParameter(new XGOpcode(MP_ELRES), 0, 32, "Element Reserve", "Rsrv"));
		m.put(MP_CH, new XGParameter(new XGOpcode(MP_CH), TranslationMap.channelMap, 0, 127, "MIDI Channel", "Ch"));
		m.put(MP_TUNE, new XGParameter(new XGOpcode(MP_TUNE, 2, ByteType.NIBBLE), ValueTranslation::translateSub128Div10, 0, 255, "Detune", "Tune"));
		return m;
	};

/********************************************************************************************************************/

	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);}

	@Override public String toString()
	{	return String.format("%02d", this.adress.getMid() + 1) + " | " + "PartMode" + " | " + getParameter(MP_CH).getValueAsText(this) + " | " + "Program";}

	public XGParameter getParameter(int offset)
	{	return parameters.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return parameters;
	}
}
