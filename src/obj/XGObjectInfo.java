package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectInfo extends XGObject
{	private static XGObjectInfo instance = null;
	private static final Map<Integer, XGParameter> params = initParams();
	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		return m;
	}

	public static XGObjectInfo getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectInfo(adr);
		return instance;
	}

/*******************************************************************************************/

	public XGObjectInfo(XGAdress adr)
	{	super(adr);
	}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return params.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 0, "parameter " + offset, "unknown"));}
}
