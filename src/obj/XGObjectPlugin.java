package obj;

import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectPlugin extends XGObject
{	private static final int NNMIN = 0, NNMAX = 127, MMMIN = 0, MMMAX = 127, PPMIN = 0, PPMAX = 15, DATASIZE = 0;

	public static XGObjectPlugin getInstance(XGAdress adr)
	{	return null;
	}

	/***********************************************************************************************************/

	public XGObjectPlugin(XGAdress adr)
	{	super(adr);
	}

	public XGParameter getParameter(int offset)
	{	return new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown");}

}
