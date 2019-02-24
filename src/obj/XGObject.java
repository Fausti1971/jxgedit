package obj;

import java.util.Map;
import java.util.logging.Logger;
import application.ChangeListener;
import parm.XGParameter;
import parm.XGParameterConstants;

public abstract class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();

	protected static ChangeListener listener = null;

	public static void setListener(ChangeListener l)
	{	XGObject.listener = l;
	}

/******************** Instance ********************************************************************************************/

	public int hi, id;
	private byte[] data = new byte[127];
	public Map<Tags, XGParameter> parameters = null;

	protected XGObject(XGAdress adr)
	{	this.id = adr.getMid();
		this.hi = adr.getHi();
	}

	public XGObject getXGObject()
	{	return this;
	}

	public void read(int offset, byte[] array)
	{	encodeByteArray(offset, array);
	}

	public byte[] getByteArray()
	{	return this.data;
	}

/************* Overrides ***********************************************************************************************************/

/*********** abstract *******************************************************************************************************************/


}
