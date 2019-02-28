package obj;

import java.util.Map;
import memory.Bytes.ByteType;
import parm.ValueTranslation;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectDisplayBitmap extends XGObject
{	private static int HI = 0x07;
	private static XGParameter  param = new XGParameter(new XGOpcode(0x00, 0x30, ByteType.MIDIBYTE), ValueTranslation::translateNot, 0, 0, "Display Bitmap", "Disp");

/***************************************************************************************************************/

	public XGObjectDisplayBitmap(XGAdress adr)
	{	super(adr);
	}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return param;}

	public Map<Integer,XGParameter> getParamters()
	{	return null;}
}
