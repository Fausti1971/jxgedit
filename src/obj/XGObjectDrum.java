package obj;

import java.util.HashMap;
import java.util.Map;
import parm.Opcode;
import parm.XGParameter;

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

	protected void initParameters()
	{	parameters.put(0x00, new XGParameter(this, new Opcode(0x00), 0, 127, "pitch coarse", "coarse"));
		parameters.put(0x01, new XGParameter(this, new Opcode(0x01), 0, 127, "pitch fine", "fine"));
		parameters.put(0x02, new XGParameter(this, new Opcode(0x02), 0, 127, "level", "vol"));
		parameters.put(0x03, new XGParameter(this, new Opcode(0x03), 0, 127, "alternate group", "alt"));
		parameters.put(0x04, new XGParameter(this, new Opcode(0x04), 0, 127, "panorama", "pan"));
		parameters.put(0x05, new XGParameter(this, new Opcode(0x05), 0, 127, "reverb send", "rev"));
		parameters.put(0x06, new XGParameter(this, new Opcode(0x06), 0, 127, "chorus send", "cho"));
		parameters.put(0x07, new XGParameter(this, new Opcode(0x07), 0, 127, "variation send", "var"));
		parameters.put(0x08, new XGParameter(this, new Opcode(0x08), 0, 1, "key assign", "key"));
		parameters.put(0x09, new XGParameter(this, new Opcode(0x09), 0, 1, "receive note off", "rcvoff"));
		parameters.put(0x0A, new XGParameter(this, new Opcode(0x0A), 0, 1, "receive note on", "rcvon"));
		parameters.put(0x0B, new XGParameter(this, new Opcode(0x0B), 0, 127, "filter cutoff", "cut"));
		parameters.put(0x0C, new XGParameter(this, new Opcode(0x0C), 0, 127, "filter resonance", "res"));
		parameters.put(0x0D, new XGParameter(this, new Opcode(0x0D), 0, 127, "eg attack time", "atck"));
		parameters.put(0x0E, new XGParameter(this, new Opcode(0x0E), 0, 127, "eg decay time", "decy"));
		parameters.put(0x0F, new XGParameter(this, new Opcode(0x0F), 0, 127, "eg release time", "rels"));
	}
}
