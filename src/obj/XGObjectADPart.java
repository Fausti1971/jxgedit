package obj;

import java.util.ArrayList;
import java.util.List;

public class XGObjectADPart extends XGObject
{	private static final int MIDMIN = 0, MIDMAX = 1, DATASIZE = 0x61, HI = 0x10;
	private static final XGAdress[] XGDUMPADRESSES = new XGAdress[]{new XGAdress(HI, 0, 0),
																	new XGAdress(HI, 0, 0x30),
																	};

	private static List<XGObjectADPart> instances = new ArrayList<>();

	public static XGObjectADPart getInstance(XGAdress adr)
	{	try
		{	return instances.get(adr.getMid());
		}
		catch(IndexOutOfBoundsException e)
		{	return new XGObjectADPart(adr);
		}
	}

/******************* Instance ****************************************************************************************************************/

	public XGObjectADPart(XGAdress adr)
	{	super(adr);
		instances.add(adr.getMid(), this);
	}
}
