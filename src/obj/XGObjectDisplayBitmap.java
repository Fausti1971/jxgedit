package obj;

import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectDisplayBitmap extends XGObject
{	private static int HI = 0x07;
	private static String MAP_NAME = "display_bitmap_parameters";

/***************************************************************************************************************/

	public XGObjectDisplayBitmap(XGAdress adr)
	{	super(adr);
	}

	public XGParameter getParameter(int offset)
	{	return XGParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
