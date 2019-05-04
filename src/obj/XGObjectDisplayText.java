package obj;

import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectDisplayText extends XGObject
{	private static final int HI = 0x06;
	private static final String MAP_NAME = "display_text_parameters";

/**************************************************************************************************************/

	public XGObjectDisplayText(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return ParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
