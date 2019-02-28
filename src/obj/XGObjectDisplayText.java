package obj;

import memory.Bytes.ByteType;
import parm.ValueTranslation;
import parm.XGOpcode;
import parm.XGParameter;

public class XGObjectDisplayText extends XGObject
{	private static XGObjectDisplayText instance = null;
	private static final int HI = 0x06;
	private static final XGParameter param = new XGParameter(new XGOpcode(0x00, 0x20, ByteType.MIDIBYTE), ValueTranslation::translateNot, 0, 0, "Display Text", "Text");

	public static XGObjectDisplayText getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectDisplayText(adr);
		return instance;
	}

/**************************************************************************************************************/

	public XGObjectDisplayText(XGAdress adr)
	{	super(adr);
		instance = this;
	}

	protected void initParameters()
	{}

	public XGParameter getParameter(int offset)
	{	return param;
	}
}
