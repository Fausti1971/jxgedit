package obj;

public class XGObjectSysFX extends XGObject
{	private static XGObjectSysFX instance = null;

	public static XGObjectSysFX getInstance(XGAdress adr)
	{	if (instance == null) return new XGObjectSysFX(adr);
		return instance;
	}

/************** Instance ************************************************************************/

	public XGObjectSysFX(XGAdress adr)
	{	super(adr);
		instance = this;
	}
}