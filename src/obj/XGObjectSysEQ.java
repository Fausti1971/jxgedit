package obj;

public class XGObjectSysEQ extends XGObject
{	private static XGObjectSysEQ instance = null;

	public static XGObjectSysEQ getInstance(XGAdress adr)
	{	if(instance == null) return new XGObjectSysEQ(adr);
		return instance;
	}

/***********************************************************************************************************************************************/

	protected XGObjectSysEQ(XGAdress adr)
	{	super(adr);
		instance = this;
	}

	protected void initParameters()
	{}
}
