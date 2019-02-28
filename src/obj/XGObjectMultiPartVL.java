package obj;

import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectMultiPartVL extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 127, DATASIZE = 0;

/***************************************************************************************************************/

	public XGObjectMultiPartVL(XGAdress adr)
	{	super(adr);}

	public XGParameter getParameter(int offset)
	{	return new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown");}
}
