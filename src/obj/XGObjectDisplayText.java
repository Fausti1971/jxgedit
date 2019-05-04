package obj;

import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectDisplayText extends XGObject
{	private static final int HI = 0x06;
	private static final String MAP_NAME = "display_text_parameters";

/**************************************************************************************************************/

	public XGObjectDisplayText(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return XGParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
