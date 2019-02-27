package obj;

public class XGObjectInfo extends XGObject
{	private static XGObjectInfo instance = null;

	public static XGObjectInfo getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectInfo(adr);
		return instance;
	}

/*******************************************************************************************/

	public XGObjectInfo(XGAdress adr)
	{	super(adr);
	}

	protected void initParameters()
	{}
}
