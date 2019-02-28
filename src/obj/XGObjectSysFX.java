package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectSysFX extends XGObject
{	private static final Map<Integer, XGParameter> PARAMETERS = initParams();

	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		//TODO
		return m;
	}

/************** Instance ************************************************************************/

	public XGObjectSysFX(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return PARAMETERS.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return PARAMETERS;}
}