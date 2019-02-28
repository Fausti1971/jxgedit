package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectSysEQ extends XGObject
{//	private static XGObjectSysEQ instance = null;
	private static final Map<Integer, XGParameter> PARAMETER = initParams();

	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		//TODO
		return m;
	}

/*	public static XGObjectSysEQ getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectSysEQ(adr);
		return instance;
	}
*/
/***********************************************************************************************************************************************/

	protected XGObjectSysEQ(XGAdress adr)
	{	super(adr);
//		instance = this;
	}

	public XGParameter getParameter(int offset)
	{	return PARAMETER.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 0, "parameter " + offset, "unknown"));}
}
