package obj;

public class XGObjectDisplayBitmap extends XGObject
{	private static XGObjectDisplayBitmap instance = null;
	private static int HI = 0x07;

	public static XGObjectDisplayBitmap getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectDisplayBitmap(adr);
		return instance;
	}

/***************************************************************************************************************/

	public XGObjectDisplayBitmap(XGAdress adr)
	{	super(adr);
		instance = this;
	}
}
