package obj;

import java.util.Map;
import parm.ParameterMap;
import parm.XGParameter;

public class XGObjectMultiPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 31, HI = 0x08;
	private static final Map<Integer, XGParameter> PARAMETERS = ParameterMap.getParameterMap("mp_parameters");

/********************************************************************************************************************/

	public XGObjectMultiPart(XGAdress adr)
	{	super(adr);}

	@Override public String toString()
	{	String m = "--";
		String ch = "--";
		try
		{	m = "" + this.adress.getMid() + 1;
			ch = getXGValue(MP_CH).getTranslatedValue();
		}
		catch(Exception e)
		{	e.printStackTrace();
			}
		return String.format("%02d", m) + " | " + "PartMode" + " | " + ch + " | " + "Program";}

	public XGParameter getParameter(int offset)
	{	return PARAMETERS.get(offset);}
}
