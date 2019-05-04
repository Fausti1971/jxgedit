package obj;

import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectInfo extends XGObject
{	private static final String MAP_NAME = "info_parameters";

/*******************************************************************************************/

	public XGObjectInfo(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return ParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
