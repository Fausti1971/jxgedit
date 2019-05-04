package obj;

import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectInsertFXVL extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 127, DATASIZE = 0;
	private static final String MAP_NAME = "fxvl_parameters";

/******************************************************************************************************************/

	public XGObjectInsertFXVL(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return XGParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
