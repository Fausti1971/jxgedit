package obj;

import memory.Bytes.ByteType;
import parm.ValueTranslation;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectDisplayBitmap extends XGObject
{//	private static XGObjectDisplayBitmap instance = null;
	private static int HI = 0x07;
	private static XGParameter  param = new XGParameter(new XGOpcode(0x00, 0x30, ByteType.MIDIBYTE), ValueTranslation::translateNot, 0, 0, "Display Bitmap", "Disp");

/*	public static XGObjectDisplayBitmap getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectDisplayBitmap(adr);
		return instance;
	}
*/
/***************************************************************************************************************/

	public XGObjectDisplayBitmap(XGAdress adr)
	{	super(adr);
//		instance = this;
	}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return param;
	}
}
