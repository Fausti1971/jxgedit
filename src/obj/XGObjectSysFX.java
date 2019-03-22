package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGValue;

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

	@Override public XGParameter getParameter(int offset)
	{	XGParameter p = PARAMETERS.getOrDefault(offset, new XGParameter(offset));
		if(p.isVariable())
		{	XGValue masterValue = getValue(p.getMasterValueOffset());
		}
	}

	@Override public XGOpcode getOpcode(int offset)
	{	return null;}
}