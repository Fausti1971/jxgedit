package obj;

import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectPlugin extends XGObject
{	private static final int NNMIN = 0, NNMAX = 127, MMMIN = 0, MMMAX = 127, PPMIN = 0, PPMAX = 15, DATASIZE = 0;
	private static final String MAP_NAME = "plugin_parameters";

	/***********************************************************************************************************/

	public XGObjectPlugin(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return ParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
