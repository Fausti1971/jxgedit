package obj;

public class XGObjectInsertFXVL extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 127, DATASIZE = 0;

	private static final XGAdress[] XGDUMPADRESSES = new XGAdress[]{new XGAdress(4, 0, 0),
																	new XGAdress(4, 0, 0x14),
																	new XGAdress(4, 0, 0x20)
																	};

	public static XGObjectInsertFXVL getInstance(XGAdress adr)
	{	return null;
	}
/******************************************************************************************************************/

	public XGObjectInsertFXVL(XGAdress adr)
	{	super(adr);
	}
}
