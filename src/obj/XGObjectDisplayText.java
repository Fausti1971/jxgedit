package obj;

public class XGObjectDisplayText extends XGObject
{	private static XGObjectDisplayText instance = null;

	public static XGObjectDisplayText getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectDisplayText(adr);
		return instance;
	}

/**************************************************************************************************************/

	public XGObjectDisplayText(XGAdress adr)
	{	super(adr);
		instance = this;
	}
}
