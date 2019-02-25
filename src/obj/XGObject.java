package obj;

import java.util.Map;
import java.util.logging.Logger;
import application.ChangeListener;
import parm.Opcode;
import parm.XGParameter;

public abstract class XGObject implements XGObjectConstants
{	protected static final Logger log = Logger.getAnonymousLogger();

	protected static ChangeListener listener = null;

/******************** Instance ********************************************************************************************/

	protected final XGAdress adr;
	protected Map<Integer, XGParameter> parameters;

	protected XGObject(XGAdress adr)
	{	this.adr = adr;}

	public XGObject getXGObject()
	{	return this;}

	public XGParameter getParameter(int offset)
	{	if(parameters == null) return null;
		return parameters.getOrDefault(offset, new XGParameter(this, new Opcode(offset), 0, 0, "unknown parameter", "unknown"));
	}

	public XGParameter getParameter(Opcode opc)
	{	return getParameter(opc.getOffset());}

	public XGAdress getAdr()
	{	return adr;
	}

/************* Overrides ***********************************************************************************************************/

/*********** abstract *******************************************************************************************************************/


}
