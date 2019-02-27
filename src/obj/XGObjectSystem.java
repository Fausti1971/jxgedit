package obj;

public class XGObjectSystem extends XGObject
{	private static XGObjectSystem instance = null;

	public static XGObjectSystem getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectSystem(adr);
		return instance;
	}
/*********************************************************************************************************/

	public XGObjectSystem(XGAdress adr)
	{	super(adr);
		instance = this;
	}

	protected void initParameters()
	{}
}
