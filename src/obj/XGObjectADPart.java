package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectADPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 1, DATASIZE = 0x61, HI = 0x10;

	private static final Map<Integer, XGParameter> parameters = initParams();
	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		return m;
	}

/******************* Instance ****************************************************************************************************************/

	public XGObjectADPart(XGAdress adr)
	{	super(adr);}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return parameters.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameters " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return parameters;}
}
