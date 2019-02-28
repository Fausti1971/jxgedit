package obj;

import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectInsertFXVL extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 127, DATASIZE = 0;

	public static XGObjectInsertFXVL getInstance(XGAdress adr)
	{	return null;
	}
/******************************************************************************************************************/

	public XGObjectInsertFXVL(XGAdress adr)
	{	super(adr);
	}

	protected void initParameters()
	{}
	public XGOpcode getOpcode(int offset)
	{	return new XGOpcode(offset);
	}
	public XGParameter getParameter(int offset)
	{	return new XGParameter(new XGOpcode(offset), 0, 127, "parameter " + offset, "unknown");}
}
