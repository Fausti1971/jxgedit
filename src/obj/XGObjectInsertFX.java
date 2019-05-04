package obj;

import parm.XGParameterMap;
import parm.XGParameter;

public class XGObjectInsertFX extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 0;
	private static final String MAP_NAME = "fx2_parameters";

/********************* Instance ***************************************************************************************/

	public XGObjectInsertFX(XGAdress adr)
	{	super(adr);}

	@Override public String toString()
	{	String m = "--";
		try
		{	m = "" + this.adress.getMid();}
		catch(Exception e)
		{	e.printStackTrace();}
		return "FX " + m;}

	public XGParameter getParameter(int offset)
	{	return XGParameterMap.getParameterMap(MAP_NAME).get(offset);}
}
