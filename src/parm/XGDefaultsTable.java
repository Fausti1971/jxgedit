package parm;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import application.*;
import static application.JXG.XMLPATH;import tag.*;
import xml.XGProperties;import xml.XMLNode;
/**
 * simple taggable HashMap<Integer, HashMap<Integer, Integer>>, deren erster int der Wert des Selektors, der zweite int der Wert des zugehörigen Defaults ist
 * @author thomas
 *
 */
public class XGDefaultsTable implements XGParameterConstants, XGLoggable, XGTagable
{
	public static final int DEF_DRUMSETPROGRAM = XGStrings.parseValue("127.0.0", 127 << 14);
	public static final int NO_ID = DEF_SELECTORVALUE;
	public static final XGTagableSet<XGDefaultsTable> DEFAULTSTABLE = new XGTagableSet<>();

	public static void init()
	{
		try
		{	XMLNode n = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_DEFAULTS));
			for(XMLNode t : n.getChildNodes(TAG_DEFAULTSTABLE))
			{	DEFAULTSTABLE.add(new XGDefaultsTable(t));
			}
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
		}

		DEFAULTSTABLE.add(new XGDefaultsTable("id")
			{	@Override public int get(int id, int sel)
				{	return (id);
				}
			}
		);

		DEFAULTSTABLE.add(new XGDefaultsTable(TABLE_PARTMODE)
			{	@Override public int get(int id, int sel)
				{	switch(id)
					{	case 9:		return 2;
						case 25:	return 4;
						case 41:
						case 57:	return 1;
						default:	return 0;
					}
				}
			}
		);

		DEFAULTSTABLE.add(new XGDefaultsTable("mp_program")
			{	@Override public int get(int id, int sel)
				{	switch(id)
					{	case 9:
						case 25:
						case 41:
						case 57:	return DEF_DRUMSETPROGRAM;
						default:	return 0;
					}
				}
			}
		);
	}

/*************************************************************************************************************/

	private final String tag;
	private final Map<Integer, Map<Integer, Integer>> idMap = new HashMap<>();

	public XGDefaultsTable(String tag)
	{	this.tag = tag;
	}

	public XGDefaultsTable(XMLNode n)
	{	this(n.getStringAttribute(ATTR_NAME));

		int id;
		this.put(NO_ID, DEF_SELECTORVALUE, n.getValueAttribute(ATTR_DEFAULT, 0));//für XGOpcodes ohne mutableDefaults

		if(n.hasChildNode(TAG_ID))
		{	for(XMLNode i : n.getChildNodes(TAG_ID))
			{	id = i.getIntegerAttribute(ATTR_VALUE, NO_ID);
				this.put(id, DEF_SELECTORVALUE, 0);
				for(XMLNode d : i.getChildNodes(TAG_ITEM))
				{	this.put(id, d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_VALUE, 0));
				}
			}
		}
		else
			for(XMLNode d : n.getChildNodes(TAG_ITEM))
				this.put(NO_ID, d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_VALUE, 0));

//		if(!this.idMap.containsKey(NO_ID)) throw new RuntimeException("table " + this.tag + " has no fallback");
		LOG.info(this.getClass().getSimpleName() + " " + this.tag + " initialized");
	}

	

	public void put(int id, int sel, int def)
	{	if(this.idMap.containsKey(id))
			this.idMap.get(id).put(sel, def);
		else
		{	Map<Integer, Integer> defMap = new HashMap<>();
			defMap.put(sel, def);
			this.idMap.put(id, defMap);
		}
	}

	public int get(int id, int sel)
	{	if(!this.idMap.containsKey(id)) id = NO_ID;
		if(!this.idMap.get(id).containsKey(sel))
		{	LOG.info(this.getClass().getSimpleName() + " (" + this.tag + ") contains no selector (" + XGStrings.valueToString(sel) + ") for id (" + id + "), using default (" + DEF_SELECTORVALUE + ")");
			sel = DEF_SELECTORVALUE;
		}
		return this.idMap.get(id).get(sel);
	}

	@Override public String getTag()
	{	return this.tag;
	}

	public XMLNode toXMLNode()
	{	int min = -1, max = -1;
		XMLNode table = new XMLNode(TAG_DEFAULTSTABLE, new XGProperties(ATTR_NAME, this.tag));
		for(int m : this.idMap.keySet())
		{	XMLNode id = new XMLNode(TAG_ID, new XGProperties(ATTR_VALUE, XGStrings.valueToString(m)));
			table.addChildNode(id);
			for(int s : this.idMap.get(m).keySet())
			{	XMLNode item = new XMLNode(TAG_ITEM, new XGProperties(ATTR_SELECTORVALUE, XGStrings.valueToString(s)));
				int v = this.idMap.get(m).get(s);
				if(min == -1) min = v;
				min = Math.min(min, v);
				if(max == -1) max = v;
				max = Math.max(max, v);
				item.getAttributes().put(ATTR_VALUE, XGStrings.valueToString(v));
				id.addChildNode(item);
			}
		}
		table.getAttributes().put("range", min + "-" + max);
//		System.out.println("table=" + this.tag + " range = " + min + "/" + max);
		return table;
	}
}