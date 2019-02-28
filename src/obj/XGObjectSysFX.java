package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectSysFX extends XGObject
{//	private static XGObjectSysFX instance = null;
	private static final Map<Integer, XGParameter> PARAMETERS = initParams();

	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		//TODO
		return m;
	}

/*	public static XGObjectSysFX getInstance(XGAdress adr)
	{	if (instance == null) return new XGObjectSysFX(adr);
		return instance;
	}
*/
/************** Instance ************************************************************************/

	public XGObjectSysFX(XGAdress adr)
	{	super(adr);
//		instance = this;
	}

	public XGParameter getParameter(int offset)
	{	return PARAMETERS.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown"));}
}