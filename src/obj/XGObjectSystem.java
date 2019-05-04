package obj;

import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectSystem extends XGObject
{	private static final String MAP_NAME = "system_parameters";

/*********************************************************************************************************/

	public XGObjectSystem(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offs)
	{	return ParameterMap.getParameterMap(MAP_NAME).get(offs);}
}
