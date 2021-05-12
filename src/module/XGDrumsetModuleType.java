package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import static parm.XGParameterConstants.TABLE_PARTMODE;import parm.XGRealTable;
import parm.XGTableEntry;import xml.XMLNode;

public class XGDrumsetModuleType extends XGModuleType
{

/****************************************************************************************************************************/

	public XGDrumsetModuleType(XMLNode n, XGAddress adr)
	{	super(n, adr, n.getStringAttributeOrDefault(ATTR_NAME, "Drumset"));
		try
		{	int nr = this.address.getHi().getValue() - 47;
			this.id += nr;
			this.name.append(" ").append(nr);
			((XGRealTable)parm.XGTable.TABLES.get(TABLE_PARTMODE)).add(new XGTableEntry(nr + 1, this.name.toString()));
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}
	}
}
