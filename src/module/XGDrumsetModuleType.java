package module;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;import parm.XGDrumNames;import static parm.XGParameterConstants.TABLE_PARTMODE;import parm.XGRealTable;
import parm.XGTableEntry;import parm.XGVirtualTable;import tag.XGTagableAddressableSet;import value.XGDrumsetProgramValue;import value.XGValue;import xml.XMLNode;import java.util.HashMap;import java.util.Map;

public class XGDrumsetModuleType extends XGModuleType
{
	public static Map<Integer, XGDrumsetModuleType> DRUMSETS = new HashMap<>();

/****************************************************************************************************************************/

	private int program = DEF_DRUMSETPROGRAM;
	private final int partmode;
	private final XGDrumsetProgramValue programListener;

	public XGDrumsetModuleType(XMLNode n, XGAddress adr) throws InvalidXGAddressException
	{	super(n, adr, n.getStringAttributeOrDefault(ATTR_NAME, "Drumset"));
		this.partmode = this.getAddress().getHi().getValue() - 46;
		this.tag += this.partmode - 1;
		this.name.append(" ").append(this.partmode - 1);
		((XGRealTable)parm.XGTable.TABLES.get(TABLE_PARTMODE)).add(new XGTableEntry(this.partmode, this.name.toString()));
		this.idTranslator = new XGVirtualTable(this.partmode, this.partmode, this.tag, (Integer i)->this.getDrumname(i), (String s)->0);
		this.programListener = new XGDrumsetProgramValue(this);
		DRUMSETS.put(this.partmode, this);
	}

	public String getDrumname(int key)
	{	XGRealTable t = XGDrumNames.DRUMNAMES.get(key);
		if(t != null) return t.getByValue(this.program).getName();
		else return "No Sound";
	}

	public int getPartmode()
	{	return this.partmode;
	}

	public XGDrumsetProgramValue getProgramListener()
	{	return this.programListener;
	}

	public int getProgram()
	{	return this.program;
	}

	public void setProgram(int prg)
	{	this.program = prg;
		for(XGModule mod : TYPES.get("mp").getModules())
		{	XGTagableAddressableSet<XGValue> vals = mod.getValues();
			if(vals.get("mp_partmode").getValue() == this.partmode)
			{	vals.get("mp_program").setValue(prg);
			}
		}
		this.programListener.notifyValueListeners(this.programListener);
	}

	
}
