package obj;

import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectUnknown extends XGObject
{

/************************************************************************************************************************************/

	public XGObjectUnknown(XGAdress adr)
	{	super(adr);
		log.info("unknown object received: " + adr);
	}

	public XGParameter getParameter(int offset)
	{	return new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown");}

	public Map<Integer,XGParameter> getParamters()
	{	return null;}
}
