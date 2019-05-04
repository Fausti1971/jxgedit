package obj;

import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectDrum extends XGObject
{	private static final int MIDMIN = 13, MIDMAX = 91, DATASIZE = 0x10;
	private static final String MAP_NAME = "dr_parameters";

/*********************************************************************************************************/

	protected XGObjectDrum(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return XGParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
