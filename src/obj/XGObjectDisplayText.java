package obj;

import java.util.Map;
import msg.Bytes.ByteType;
import parm.ValueTranslation;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectDisplayText extends XGObject
{	private static final int HI = 0x06;
	private static final XGParameter param = new XGParameter(new XGOpcode(0x00, 0x20, ByteType.MIDIBYTE), ValueTranslation::translateNot, 0, 0, "Display Text", "Text");

/**************************************************************************************************************/

	public XGObjectDisplayText(XGAdress adr)
	{	super(adr);}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return param;}

	public Map<Integer,XGParameter> getParamters()
	{	return null;}
}
