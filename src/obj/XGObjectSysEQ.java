package obj;

import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectSysEQ extends XGObject
{	private static final String MAP_NAME = "eq_parameters";

/***********************************************************************************************************************************************/

	protected XGObjectSysEQ(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return ParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
