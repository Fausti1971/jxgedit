package obj;

import java.util.HashMap;
import java.util.Map;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectInsertFX extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 0;

	private static final Map<Integer, XGParameter> params = initParams();
	private static final Map<Integer, XGParameter> initParams()
	{	Map<Integer, XGParameter> m = new HashMap<>();
		//TODO Parameter anlegen
		return m;
	}

/********************* Instance ***************************************************************************************/

	public XGObjectInsertFX(XGAdress adr)
	{	super(adr);}

	@Override public String toString()
	{	return "FX " + this.adress.getMid();}

	public XGParameter getParameter(int offset)
	{	return params.getOrDefault(offset, new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown"));}

	public Map<Integer,XGParameter> getParamters()
	{	return params;}
}
