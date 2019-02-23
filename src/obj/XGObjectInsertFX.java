package obj;

import java.util.ArrayList;
import java.util.List;

public class XGObjectInsertFX extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 0;

	private static List<XGObjectInsertFX> instances = new ArrayList<>();

	public static XGObjectInsertFX getInstance(XGAdress adr)
	{	try
		{	return instances.get(adr.getMid());
		}
		catch(IndexOutOfBoundsException e)
		{	return new XGObjectInsertFX(adr);
		}
	}

/********************* Instance ***************************************************************************************/

	public XGObjectInsertFX(XGAdress adr)
	{	super(adr);
		instances.add(adr.getMid(), this);
	}

	@Override public String toString()
	{	return "FX " + this.id;
	}
}
