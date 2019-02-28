package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectDrum extends XGObject
{	private static final int MIDMIN = 13, MIDMAX = 91, DATASIZE = 0x10;

	private static final Map<Integer, XGParameter> params = initParams();
	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		m.put(DR_COARSE, new XGParameter(new XGOpcode(DR_COARSE), 0, 127, "pitch coarse", "coarse"));
		m.put(DR_FINE, new XGParameter(new XGOpcode(DR_FINE), 0, 127, "pitch fine", "fine"));
		m.put(DR_VOL, new XGParameter(new XGOpcode(DR_VOL), 0, 127, "level", "vol"));
		m.put(DR_GRP, new XGParameter(new XGOpcode(DR_GRP), 0, 127, "alternate group", "alt"));
		m.put(DR_PAN, new XGParameter(new XGOpcode(DR_PAN), 0, 127, "panorama", "pan"));
		m.put(DR_REV, new XGParameter(new XGOpcode(DR_REV), 0, 127, "reverb send", "rev"));
		m.put(DR_CHO, new XGParameter(new XGOpcode(DR_CHO), 0, 127, "chorus send", "cho"));
		m.put(DR_VAR, new XGParameter(new XGOpcode(DR_VAR), 0, 127, "variation send", "var"));
		m.put(DR_ASSIGN, new XGParameter(new XGOpcode(DR_ASSIGN), 0, 1, "key assign", "key"));
		m.put(DR_RCVOFF, new XGParameter(new XGOpcode(DR_RCVOFF), 0, 1, "receive note off", "rcvoff"));
		m.put(DR_RCVON, new XGParameter(new XGOpcode(DR_RCVON), 0, 1, "receive note on", "rcvon"));
		m.put(DR_CUTPOFF, new XGParameter(new XGOpcode(DR_CUTPOFF), 0, 127, "filter cutoff", "cut"));
		m.put(DR_RESO, new XGParameter(new XGOpcode(DR_RESO), 0, 127, "filter resonance", "res"));
		m.put(DR_ATTACK, new XGParameter(new XGOpcode(DR_ATTACK), 0, 127, "eg attack time", "atck"));
		m.put(DR_DECAY, new XGParameter(new XGOpcode(DR_DECAY), 0, 127, "eg decay time", "decy"));
		m.put(DR_RELEASE, new XGParameter(new XGOpcode(DR_RELEASE), 0, 127, "eg release time", "rels"));
		return m;
	}

/*********************************************************************************************************/

	protected XGObjectDrum(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return params.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return params;}
}
