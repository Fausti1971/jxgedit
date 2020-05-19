package parm;

import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGTableTranslator implements XGValueTranslator
{
	private final XGTable table;

	public XGTableTranslator(XGDevice dev, XMLNode n) throws XGTableNotFoundException
	{	String name = n.getStringAttribute(ATTR_TRANSLATIONTABLE);
		XGTable t = dev.getTables().get(name);
		if(t != null)
		{	if(n.hasAttribute(ATTR_TABLEFILTER)) t = t.filter(n);
		}
		else throw new XGTableNotFoundException("table not found " + name);
		this.table = t;
	}

	@Override public XGTable getTable(XGValue v)
	{	return this.table;
	}

	@Override public String translate(XGValue v)
	{	XGParameter p = v.getParameter();
		if(p == null) return "n/a";
		XGTableEntry e;
		e = this.table.get(v.getContent());
		if(e == null)
		{	log.info("value " + v.getContent() + " not found in " + this.table);
			return "(" + v.getContent() + ")";
		}
		String unit = this.table.getUnit();
		if(unit == null || unit.isEmpty()) unit = p.getUnit();
		return e.getName() + unit;
	}
	@Override public int translate(XGValue v, String s)
	{
		return 0;
	}
}
