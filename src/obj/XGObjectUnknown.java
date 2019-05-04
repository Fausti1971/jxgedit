package obj;

import parm.XGParameter;

public class XGObjectUnknown extends XGObject
{

/************************************************************************************************************************************/

	public XGObjectUnknown(XGAdress adr)
	{	super(adr);
		log.info("unknown object received: " + adr);
	}

	public XGParameter getParameter(int offset)
	{	return new XGParameter(offset);}
}
