package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectInfo extends XGObject
{	private static final Map<Integer, XGParameter> params = initParams();
	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		return m;
	}

/*******************************************************************************************/

	public XGObjectInfo(XGAdress adr)
	{	super(adr);}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return params.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 0, "parameter " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return params;}
}
