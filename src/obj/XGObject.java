package obj;

import java.util.HashMap;
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
	protected Map<Integer, XGParameter> parameters = new HashMap<>();

	protected XGObject(XGAdress adr)
	{	this.adr = adr;
		this.initParameters();
	}

	public XGObject getXGObject()
	{	return this;}

	public XGParameter getParameter(int offset)
	{	return parameters.getOrDefault(offset, new XGParameter(this, new Opcode(offset), 0, 0, "parameter " + offset, "unknow"));}

	public XGParameter getParameter(Opcode opc)
	{	return getParameter(opc.getOffset());}

	public Map<Integer, XGParameter> getParameters()
	{	return this.parameters;}

	public XGAdress getAdr()
	{	return adr;}

/************* Overrides ***********************************************************************************************************/

/*********** abstract *******************************************************************************************************************/

	protected abstract void initParameters();

}
