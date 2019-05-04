package obj;

import java.util.Map;
import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectADPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 1, DATASIZE = 0x61, HI = 0x10;
	private static final Map<Integer, XGParameter> PARAMETERS = XGParameterMap.getParameterMap("ad_parameters"); 

/******************* Instance ****************************************************************************************************************/

	public XGObjectADPart(XGAdress adr)
	{	super(adr);}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offs)
	{	return PARAMETERS.getOrDefault(offs, new XGParameter(offs));}
}
